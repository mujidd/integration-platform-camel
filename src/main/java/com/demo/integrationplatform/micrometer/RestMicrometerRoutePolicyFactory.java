package com.demo.integrationplatform.micrometer;

import io.micrometer.core.instrument.MeterRegistry;
import org.apache.camel.CamelContext;
import org.apache.camel.NamedNode;
import org.apache.camel.component.micrometer.routepolicy.MicrometerRoutePolicy;
import org.apache.camel.component.micrometer.routepolicy.MicrometerRoutePolicyNamingStrategy;
import org.apache.camel.spi.RoutePolicy;
import org.apache.camel.spi.RoutePolicyFactory;

import java.util.concurrent.TimeUnit;

public class RestMicrometerRoutePolicyFactory implements RoutePolicyFactory {

    private MeterRegistry meterRegistry;
    private boolean prettyPrint = true;
    private TimeUnit durationUnit = TimeUnit.MILLISECONDS;
    private MicrometerRoutePolicyNamingStrategy namingStrategy = MicrometerRoutePolicyNamingStrategy.DEFAULT;

    /**
     * To use a specific {@link io.micrometer.core.instrument.MeterRegistry} instance.
     * <p/>
     * If no instance has been configured, then Camel will create a shared instance to be used.
     */
    public void setMeterRegistry(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    public MeterRegistry getMeterRegistry() {
        return meterRegistry;
    }

    public boolean isPrettyPrint() {
        return prettyPrint;
    }

    /**
     * Whether to use pretty print when outputting JSon
     */
    public void setPrettyPrint(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    /**
     * Sets the time unit to use for requests per unit (eg requests per second)
     */
    public TimeUnit getDurationUnit() {
        return durationUnit;
    }

    /**
     * Sets the time unit to use for timing the duration of processing a message in the route
     */
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
    public RoutePolicy createRoutePolicy(CamelContext camelContext, String routeId, NamedNode routeDefinition) {
        RestMicrometerRoutePolicy answer = new RestMicrometerRoutePolicy();
        answer.setMeterRegistry(getMeterRegistry());
        answer.setPrettyPrint(isPrettyPrint());
        answer.setDurationUnit(getDurationUnit());
        answer.setNamingStrategy(getNamingStrategy());
        return answer;
    }

}
