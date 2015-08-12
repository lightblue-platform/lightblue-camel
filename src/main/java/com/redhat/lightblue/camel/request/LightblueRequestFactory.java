package com.redhat.lightblue.camel.request;

import javax.annotation.Nullable;

import org.apache.camel.Body;
import org.apache.camel.Header;

import com.redhat.lightblue.client.Projection;
import com.redhat.lightblue.client.Query;
import com.redhat.lightblue.client.Sort;
import com.redhat.lightblue.client.Update;
import com.redhat.lightblue.client.request.data.DataDeleteRequest;
import com.redhat.lightblue.client.request.data.DataFindRequest;
import com.redhat.lightblue.client.request.data.DataInsertRequest;
import com.redhat.lightblue.client.request.data.DataSaveRequest;
import com.redhat.lightblue.client.request.data.DataUpdateRequest;

public class LightblueRequestFactory {

    public static final String HEADER_ENTITY_NAME = "entityName";
    public static final String HEADER_ENTITY_VERSION = "entityVersion";
    public static final String HEADER_PROJECTIONS = "projections";
    public static final String HEADER_QUERY = "query";
    public static final String HEADER_SORTS = "sorts";
    public static final String HEADER_RANGE_BEGIN = "rangeBegin";
    public static final String HEADER_RANGE_END = "endRange";

    public DataInsertRequest createInsert(
            @Header(HEADER_ENTITY_NAME) String entityName,
            @Header(HEADER_ENTITY_VERSION) String entityVersion,
            @Nullable @Header(HEADER_PROJECTIONS) Projection[] projections,
            @Body Object[] body){

        DataInsertRequest request = new DataInsertRequest(entityName, entityVersion);
        request.create(body);
        if(projections == null){
            projections = new Projection[]{Projection.includeFieldRecursively("*")};
        }
        request.returns(projections);

        return request;
    }

    public DataUpdateRequest createUpdate(
            @Header(HEADER_ENTITY_NAME) String entityName,
            @Header(HEADER_ENTITY_VERSION) String entityVersion,
            @Header(HEADER_QUERY) Query query,
            @Nullable @Header(HEADER_PROJECTIONS) Projection[] projections,
            @Body Update[] body){

        DataUpdateRequest request = new DataUpdateRequest(entityName, entityVersion);
        request.updates(body);
        request.where(query);
        if(projections == null){
            projections = new Projection[]{Projection.includeFieldRecursively("*")};
        }
        request.returns(projections);

        return request;
    }

    public DataSaveRequest createSave(
            @Header(HEADER_ENTITY_NAME) String entityName,
            @Header(HEADER_ENTITY_VERSION) String entityVersion,
            @Nullable @Header(HEADER_PROJECTIONS) Projection[] projections,
            @Body Object[] body){

        DataSaveRequest request = new DataSaveRequest(entityName, entityVersion);
        request.create(body);
        if(projections == null){
            projections = new Projection[]{Projection.includeFieldRecursively("*")};
        }
        request.returns(projections);

        return request;
    }

    public DataFindRequest createFind(
            @Header(HEADER_ENTITY_NAME) String entityName,
            @Header(HEADER_ENTITY_VERSION) String entityVersion,
            @Header(HEADER_QUERY) Query query,
            @Header(HEADER_PROJECTIONS) Projection[] projections,
            @Header(HEADER_SORTS) Sort[] sorts, 
            @Header(HEADER_RANGE_BEGIN) Integer beginRange,
            @Header(HEADER_RANGE_END) Integer endRange) {

        DataFindRequest request = new DataFindRequest(entityName, entityVersion);
        request.select(projections);
        request.where(query);

        if (sorts != null) {
            request.sort(sorts);
        }

        if (beginRange != null) {
            request.range(beginRange, endRange);
        }

        return request;
    }
    
    public DataDeleteRequest createDelete(
            @Header(HEADER_ENTITY_NAME) String entityName,
            @Header(HEADER_ENTITY_VERSION) String entityVersion,
            @Header(HEADER_QUERY) Query query) {

        DataDeleteRequest request = new DataDeleteRequest(entityName, entityVersion);
        request.where(query);

        return request;
    }

}
