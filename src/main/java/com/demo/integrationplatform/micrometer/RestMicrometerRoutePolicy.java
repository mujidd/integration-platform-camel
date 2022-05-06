package com.demo.integrationplatform.micrometer;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.Timer;
import org.apache.camel.*;
import org.apache.camel.component.micrometer.MicrometerUtils;
import org.apache.camel.component.micrometer.routepolicy.MicrometerRoutePolicyNamingStrategy;
import org.apache.camel.component.micrometer.routepolicy.MicrometerRoutePolicyService;
import org.apache.camel.component.rest.RestEndpoint;
import org.apache.camel.support.ExchangeHelper;
import org.apache.camel.support.RoutePolicySupport;
import org.apache.camel.support.service.ServiceHelper;
import org.apache.camel.util.ObjectHelper;

import java.util.concurrent.TimeUnit;

import static org.apache.camel.component.micrometer.MicrometerConstants.*;

public class RestMicrometerRoutePolicy extends RoutePolicySupport implements NonManagedService {

    private MeterRegistry meterRegistry;
    private boolean prettyPrint;
    private TimeUnit durationUnit = TimeUnit.MILLISECONDS;
    private RestMicrometerRoutePolicy.MetricsStatistics statistics;
    private MicrometerRoutePolicyNamingStrategy namingStrategy = new RestMicrometerRoutePolicyNamingStrategy();

    private static final class MetricsStatistics {
        private final MeterRegistry meterRegistry;
        private final Route route;
        private final MicrometerRoutePolicyNamingStrategy namingStrategy;
        private final Counter exchangesSucceeded;
        private final Counter exchangesFailed;
        private final Counter exchangesTotal;
        private final Counter externalRedeliveries;
        private final Counter failuresHandled;

        private MetricsStatistics(MeterRegistry meterRegistry, Route route,
                                  MicrometerRoutePolicyNamingStrategy namingStrategy) {
            this.meterRegistry = ObjectHelper.notNull(meterRegistry, "MeterRegistry", this);
            this.namingStrategy = ObjectHelper.notNull(namingStrategy, "MicrometerRoutePolicyNamingStrategy", this);
            this.route = route;
            this.exchangesSucceeded = createCounter(namingStrategy.getExchangesSucceededName(route));
            this.exchangesFailed = createCounter(namingStrategy.getExchangesFailedName(route));
            this.exchangesTotal = createCounter(namingStrategy.getExchangesTotalName(route));
            this.externalRedeliveries = createCounter(namingStrategy.getExternalRedeliveriesName(route));
            this.failuresHandled = createCounter(namingStrategy.getFailuresHandledName(route));
        }

        public void onExchangeBegin(Exchange exchange) {
            Timer.Sample sample = Timer.start(meterRegistry);
            exchange.setProperty(propertyName(exchange), sample);
        }

        public void onExchangeDone(Exchange exchange) {
            Timer.Sample sample = (Timer.Sample) exchange.removeProperty(propertyName(exchange));
            if (sample != null) {
                Timer timer = Timer.builder(namingStrategy.getName(route))
                        .tags(namingStrategy.getTags(route, exchange))
                        .description(route.getDescription())
                        .register(meterRegistry);
                sample.stop(timer);
            }
            exchangesTotal.increment();

            if (exchange.isFailed()) {
                exchangesFailed.increment();
            } else {
                exchangesSucceeded.increment();

                if (ExchangeHelper.isFailureHandled(exchange)) {
                    failuresHandled.increment();
                }

                if (exchange.isExternalRedelivered()) {
                    externalRedeliveries.increment();
                }
            }
        }

        private String propertyName(Exchange exchange) {
            return String.format("%s-%s-%s", DEFAULT_CAMEL_ROUTE_POLICY_METER_NAME, route.getId(), exchange.getExchangeId());
        }

        private Counter createCounter(String meterName) {
            return Counter.builder(meterName)
                    .tags(namingStrategy.getExchangeStatusTags(route))
                    .description(route.getDescription())
                    .register(meterRegistry);
        }
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }

    public void setMeterRegistry(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    public void setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public TimeUnit getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(TimeUnit durationUnit) {
        this.durationUnit = durationUnit;
    }

    public MicrometerRoutePolicyNamingStrategy getNamingStrategy() {
        return namingStrategy;
    }

    public void setNamingStrategy(MicrometerRoutePolicyNamingStrategy namingStrategy) {
        this.namingStrategy = namingStrategy;
    }

    @Override
    public void onInit(Route route) {
        super.onInit(route);
        if (getMeterRegistry() == null) {
            setMeterRegistry(MicrometerUtils.getOrCreateMeterRegistry(
                    route.getCamelContext().getRegistry(), METRICS_REGISTRY_NAME));
        }
        try {
            MicrometerRoutePolicyService registryService
                    = route.getCamelContext().hasService(MicrometerRoutePolicyService.class);
            if (registryService == null) {
                registryService = new MicrometerRoutePolicyService();
                registryService.setMeterRegistry(getMeterRegistry());
                registryService.setPrettyPrint(isPrettyPrint());
                registryService.setDurationUnit(getDurationUnit());
                registryService.setMatchingTags(Tags.of(SERVICE_NAME, MicrometerRoutePolicyService.class.getSimpleName()));
                route.getCamelContext().addService(registryService);
                ServiceHelper.startService(registryService);
            }
        } catch (Exception e) {
            throw RuntimeCamelException.wrapRuntimeCamelException(e);
        }

        // create statistics holder for rest endpoint
        if (route.getEndpoint() instanceof RestEndpoint)
        {
            statistics = new RestMicrometerRoutePolicy.MetricsStatistics(getMeterRegistry(), route, getNamingStrategy());
        }
    }

    @Override
    public void onExchangeBegin(Route route, Exchange exchange) {
        if (statistics != null) {
            statistics.onExchangeBegin(exchange);
        }
    }

    @Override
    public void onExchangeDone(Route route, Exchange exchange) {
        if (statistics != null) {
            statistics.onExchangeDone(exchange);
        }
    }

}