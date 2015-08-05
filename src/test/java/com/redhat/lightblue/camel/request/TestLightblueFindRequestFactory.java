package com.redhat.lightblue.camel.request;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Test;

import com.redhat.lightblue.client.enums.SortDirection;
import com.redhat.lightblue.client.expression.query.ValueQuery;
import com.redhat.lightblue.client.request.SortCondition;
import com.redhat.lightblue.client.request.data.DataFindRequest;

public class TestLightblueFindRequestFactory {

    @Test
    public void test() throws Exception {
        LightblueFindRequestFactory factory = new LightblueFindRequestFactory("entity", "1.2.3");
        factory.setQuery(ValueQuery.withValue("field = value"));
        factory.setSorts(new SortCondition[]{new SortCondition("field", SortDirection.ASC)});
        factory.setRange(1, 2);

        Exchange exchange = new DefaultExchange(new DefaultCamelContext());
        factory.process(exchange);

        DataFindRequest request = exchange.getIn().getBody(DataFindRequest.class);

        assertNotNull(request);
        assertEquals(
                "{\"query\":{\"field\":\"field\",\"op\":\"=\",\"rvalue\":\"value\"},\"projection\":[{\"field\":\"*\",\"include\":true,\"recursive\":true}],\"sort\":[{\"field\":\"$asc\"}], \"range\": [1,2]}",
                request.getBody());
    }

}
