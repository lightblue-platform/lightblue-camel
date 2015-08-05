package com.redhat.lightblue.camel.request;

import com.redhat.lightblue.client.request.data.DataDeleteRequest;

/**
 * Creates a {@link DataDeleteRequest}
 * 
 * @author dcrissma
 */
public class LightblueDeleteRequestFactory extends LightblueRequestFactory<DataDeleteRequest> {

    public LightblueDeleteRequestFactory() {
        super();
    }

    public LightblueDeleteRequestFactory(String entityName) {
        super(entityName);
    }

    public LightblueDeleteRequestFactory(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    @Override
    protected DataDeleteRequest createRequest(String entityName, String entityVersion) {
        DataDeleteRequest request = new DataDeleteRequest(entityName, entityVersion);
        request.where(getQuery());

        return request;
    }

}
