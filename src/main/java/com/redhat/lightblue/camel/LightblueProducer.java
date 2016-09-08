package com.redhat.lightblue.camel;

import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

import com.redhat.lightblue.camel.exception.LightblueCamelProducerException;
import com.redhat.lightblue.client.LightblueException;
import com.redhat.lightblue.client.request.DataBulkRequest;
import com.redhat.lightblue.client.request.LightblueDataRequest;
import com.redhat.lightblue.client.request.LightblueMetadataRequest;
import com.redhat.lightblue.client.request.LightblueRequest;
import com.redhat.lightblue.client.response.LightblueResponse;

/**
 * The Lightblue producer.
 */
public class LightblueProducer extends DefaultProducer {

    private final LightblueScheduledPollEndpoint endpoint;

    public LightblueProducer(LightblueScheduledPollEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        LightblueRequest req = exchange.getIn().getBody(LightblueRequest.class);
        if (req == null) {
            throw new IllegalArgumentException("Unable to find an instance of LightblueRequest on the exchange in.");
        }

        try {
            Object response = sendRequest(req);
            exchange.getIn().setBody(response);
        } catch (Exception e) {
            // routing didn't start yet, so we can't expect camel to handle this error
            // set exception on exchange and pass it to camel
            exchange.setException(new LightblueCamelProducerException(e));
        }
    }

    private LightblueResponse sendRequest(LightblueRequest request) throws LightblueException {
        if (request instanceof LightblueDataRequest) {
            return endpoint.getLightblueClient().data((LightblueDataRequest) request);
        }
        else if (request instanceof DataBulkRequest){
            return endpoint.getLightblueClient().bulkData((DataBulkRequest) request);
        }
        else if (request instanceof LightblueMetadataRequest){
            return endpoint.getLightblueClient().metadata((LightblueMetadataRequest) request);
        }
        else{
            throw new IllegalArgumentException("Unknown LightblueRequest type: " + request.getClass().getName());
        }
    }

}
