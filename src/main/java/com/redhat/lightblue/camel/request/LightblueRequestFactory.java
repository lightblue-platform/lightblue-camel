package com.redhat.lightblue.camel.request;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.request.LightblueRequest;

@Deprecated
public abstract class LightblueRequestFactory<R extends LightblueRequest> implements Processor {

    public static final String HEADER_ENTITY_NAME = "entityName";
    public static final String HEADER_ENTITY_VERSION = "entityVersion";
    public static final String HEADER_PROJECTIONS = "projections";
    public static final String HEADER_QUERY = "query";

    static final Projection[] DEFAULT_PROJECTIONS = new Projection[]{
        Projection.includeFieldRecursively("*")
    };

    private Exchange exchange;
    private final String entityName;
    private final String entityVersion;
    private Projection[] projections;
    private Query query;

    /**
     * @return the entityName set in the constructor if not null, otherwise will look for a header.
     * @see #HEADER_ENTITY_NAME
     */
    public String getEntityName() {
        if (entityName == null) {
            return getHeader(HEADER_ENTITY_NAME, String.class);
        }
        return entityName;
    }

    /**
     * @return the entityVersion set in the constructor if not null, otherwise will look for a header.
     * @see #HEADER_ENTITY_VERSION
     */
    public String getEntityVersion() {
        if (entityVersion == null) {
            return getHeader(HEADER_ENTITY_VERSION, String.class);
        }
        return entityVersion;
    }

    /**
     * <p><b>NOTE:</b> Not all CRUD operations use a projection.</p>
     */
    public void setProjections(Projection[] projections) {
        this.projections = projections;
    }

    /**
     * <p>Will return the set projection. If not set, will attempt to find and return the projections on the header.
     * Finally, if still not found then will return the default projection
     * that will return all fields recursively.</p>
     * <p><b>NOTE:</b> Not all CRUD operations use a projection.</p>
     * @return projections
     * @see #HEADER_PROJECTIONS
     */
    public Projection[] getProjections() {
        if (projections != null) {
            return projections;
        }
        if (exchange.getIn().getHeader(HEADER_PROJECTIONS) == null) {
            return DEFAULT_PROJECTIONS;
        }
        return getHeader(HEADER_PROJECTIONS, Projection[].class);
    }

    /**
     * <p><b>NOTE:</b> Not all CRUD operations use a query.</p>
     */
    public void setQuery(Query query) {
        this.query = query;
    }

    /**
     * <b>NOTE:</b> Not all CRUD operations use a query.
     * @return the Query from the headers.
     * @see #HEADER_QUERY
     */
    public Query getQuery() {
        if (query == null) {
            return getHeader(HEADER_QUERY, Query.class);
        }
        return query;
    }

    /**
     * Shortcut method to return a header off the IN on the exchange.
     */
    protected <T> T getHeader(String headerName, Class<T> type) {
        return exchange.getIn().getHeader(headerName, type);
    }

    /**
     * Shortcut method to return the body off the IN on the exchange.
     */
    protected <T> T getBody(Class<T> type) {
        return exchange.getIn().getBody(type);
    }

    /**
     * Constructor assumes that the entityName and entityVersion are both set as headers.
     * @see #HEADER_ENTITY_NAME
     * @see #HEADER_ENTITY_VERSION
     */
    public LightblueRequestFactory() {
        this(null, null);
    }

    /**
     * Constructor will use the passed in entityName and the default entityVersion.
     * 
     * @param entityName
     */
    public LightblueRequestFactory(String entityName) {
        this(entityName, null);
    }

    /**
     * Constructor will use the passed in entityName and entityVersion.
     * @param entityName
     * @param entityVersion
     */
    public LightblueRequestFactory(String entityName, String entityVersion) {
        this.entityName = entityName;
        this.entityVersion = entityVersion;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        this.exchange = exchange;

        R request = createRequest(getEntityName(), getEntityVersion());
        exchange.getIn().setBody(request);
    }

    protected abstract R createRequest(String entityName, String entityVersion);

}
