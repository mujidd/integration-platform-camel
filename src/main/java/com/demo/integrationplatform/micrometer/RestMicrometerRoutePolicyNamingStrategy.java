package com.demo.integrationplatform.micrometer;

import io.micrometer.core.instrument.Tags;
import org.apache.camel.Exchange;
import org.apache.camel.Route;
import org.apache.camel.component.micrometer.routepolicy.MicrometerRoutePolicyNamingStrategy;
import org.apache.camel.component.micrometer.routepolicy.MicrometerRoutePolicyService;
import org.apache.camel.component.rest.RestEndpoint;

import static org.apache.camel.component.micrometer.MicrometerConstants.*;

public class RestMicrometerRoutePolicyNamingStrategy implements MicrometerRoutePolicyNamingStrategy {

    public static final String REST_ROUTER_SECONDS_NAME = "RestRouterSeconds";
    public static final String REST_ROUTER_URI_TAG = "uri";
    public static final String REST_ROUTER_URI_METHOD = "method";
    public static final String REST_ROUTER_URI_STATUS = "status";

    @Override
    public String getName(Route route) {
        return null;
    }

    @Override
    public String getExchangesSucceededName(Route route) {
        return MicrometerRoutePolicyNamingStrategy.super.getExchangesSucceededName(route);
    }

    @Override
    public String getExchangesFailedName(Route route) {
        return MicrometerRoutePolicyNamingStrategy.super.getExchangesFailedName(route);
    }

    @Override
    public String getExchangesTotalName(Route route) {
        return REST_ROUTER_SECONDS_NAME;
    }

    @Override
    public String getFailuresHandledName(Route route) {
        return MicrometerRoutePolicyNamingStrategy.super.getFailuresHandledName(route);
    }

    @Override
    public String getExternalRedeliveriesName(Route route) {
        return MicrometerRoutePolicyNamingStrategy.super.getExternalRedeliveriesName(route);
    }

    @Override
    public Tags getTags(Route route, Exchange exchange) {
        return Tags.of(
                CAMEL_CONTEXT_TAG, route.getCamelContext().getName(),
                SERVICE_NAME, MicrometerRoutePolicyService.class.getSimpleName(),
                ROUTE_ID_TAG, route.getId(),
                FAILED_TAG, Boolean.toString(exchange.isFailed()));
    }

    @Override
    public Tags getExchangeStatusTags(Route route) {
            RestEndpoint endpoint = (RestEndpoint)route.getEndpoint();
            return Tags.of(
                    CAMEL_CONTEXT_TAG, route.getCamelContext().getName(),
                    SERVICE_NAME, MicrometerRoutePolicyService.class.getSimpleName(),
                    ROUTE_ID_TAG, route.getId(),
                    REST_ROUTER_URI_TAG,endpoint.getEndpointUri(),
                    REST_ROUTER_URI_METHOD,endpoint.getMethod());
    }
}
