package com.demo.integrationplatform.aggregate;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

/**
 * An {@link AggregationStrategy} which just uses the original exchange which can be needed when you want to preserve
 * the original Exchange. For example when splitting an {@link Exchange} and then you may want to keep routing using the
 * original {@link Exchange}.
 *
 * @see org.apache.camel.processor.Splitter
 */
@Component("useHeadsAggregationStrategy")
public class UseHeadsAggregationStrategy implements AggregationStrategy {
    /**
     * judge whether exception occur?stop routing:replace the origin exchange inbound heads by the new exchange heads
     *
     * @param oldExchange origin exchange
     * @param newExchange new exchange
     * @return exchange process by business
     */
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        Exchange exchange;
        if (newExchange.isRouteStop()) {
            exchange = newExchange;
        } else {
            oldExchange.getIn().setHeaders(newExchange.getIn().getHeaders());
            exchange = oldExchange;
        }
        return exchange;
    }
}
