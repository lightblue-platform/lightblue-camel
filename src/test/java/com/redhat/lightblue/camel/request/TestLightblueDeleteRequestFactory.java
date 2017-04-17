package com.redhat.lightblue.camel.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Test;

import com.redhat.lightblue.client.expression.query.ValueQuery;
import com.redhat.lightblue.client.request.data.DataDeleteRequest;

public class TestLightblueDeleteRequestFactory {

    @Test
    public void test() throws Exception {
        LightblueDeleteRequestFactory factory = new LightblueDeleteRequestFactory("entity", "1.2.3");

        Exchange exchange = new DefaultExchange(new DefaultCamelContext());
        exchange.getIn().setHeader(LightblueRequestFactory.HEADER_QUERY, ValueQuery.withValue("field = value"));
        factory.process(exchange);

        DataDeleteRequest request = exchange.getIn().getBody(DataDeleteRequest.class);

        assertNotNull(request);
        assertEquals(
                "{\"query\":{\"field\":\"field\",\"op\":\"=\",\"rvalue\":\"value\"}}",
                request.getBody());
    }

}
