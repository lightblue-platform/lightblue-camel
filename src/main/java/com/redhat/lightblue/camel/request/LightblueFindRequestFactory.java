package com.redhat.lightblue.camel.request;

import com.redhat.lightblue.client.request.SortCondition;
import com.redhat.lightblue.client.request.data.DataFindRequest;

public class LightblueFindRequestFactory extends LightblueRequestFactory<DataFindRequest> {

    public static final String HEADER_SORTS = "sorts";
    public static final String HEADER_RANGE_BEGIN = "rangeBegin";
    public static final String HEADER_RANGE_END = "endRange";

    private SortCondition[] sorts;
    private Integer rangeBegin;
    private Integer rangeEnd;

    public LightblueFindRequestFactory() {
        super();
    }

    public LightblueFindRequestFactory(String entityName) {
        super(entityName);
    }

    public LightblueFindRequestFactory(String entityName, String entityVersion) {
        super(entityName, entityVersion);
    }

    @Override
    protected DataFindRequest createRequest(String entityName, String entityVersion) {
        DataFindRequest request = new DataFindRequest(entityName, entityVersion);
        request.select(getProjections());
        request.where(getQuery());
        request.sort(getSorts());
        request.range(getBeginRange(), getEndRange());

        return request;
    }

    public void setRange(int begin, int end) {
        rangeBegin = begin;
        rangeEnd = end;
    }

    public Integer getEndRange() {
        if (rangeEnd == null) {
            return getHeader(HEADER_RANGE_END, Integer.class);
        }
        return rangeEnd;
    }

    public Integer getBeginRange() {
        if (rangeBegin == null) {
            return getHeader(HEADER_RANGE_BEGIN, Integer.class);
        }
        return rangeBegin;
    }

    public void setSorts(SortCondition[] sorts) {
        this.sorts = sorts;
    }

    public SortCondition[] getSorts() {
        if (sorts == null) {
            return getHeader(HEADER_ENTITY_NAME, SortCondition[].class);
        }
        return sorts;
    }

}
