package com.redhat.lightblue.camel;

import org.apache.camel.builder.RouteBuilder;

import com.redhat.lightblue.camel.model.User;
import com.redhat.lightblue.camel.request.LightblueRequestFactory;
import com.redhat.lightblue.camel.utils.JacksonXmlDataFormat;
import com.redhat.lightblue.client.response.LightblueException;

public class SampleProducerRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        onException(LightblueException.class)
            .transform(simple("${body.getText}"))
            .to("mock:exception")
            .handled(true);

        from("direct:start")
            .unmarshal(new JacksonXmlDataFormat(User[].class))
            .setHeader(LightblueRequestFactory.HEADER_ENTITY_NAME, constant("user"))
            .setHeader(LightblueRequestFactory.HEADER_ENTITY_VERSION, constant("1.0.0"))
            .bean(LightblueRequestFactory.class, "createInsert")
            .to("lightblue://inboundTest")
            .transform(simple("${body.getText}"))
            .to("mock:result");
    }
}
