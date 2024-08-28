package com.ericsson.cifwk.operators;

import com.ericsson.cifwk.taf.tools.http.HttpResponse;

public interface GenericRESTOperator {

    /**
     * Executing REST Calls GET or POST for REST call taken JSON object as body
     * parameter
     * 
     * @param baseUrl
     * @param path
     * @param type
     * @param Body
     * @param timeOut
     * @return
     */

    HttpResponse executeRESTwithJSONBody(String baseUrl, String path,
            String body, String type, int timeout);

    /**
     * Executing REST Calls GET or POST with TimeOut
     * 
     * @param baseUrl
     * @param path
     * @param restParameters
     * @param type
     * @return
     */

    HttpResponse executeREST(String baseUrl, String path,
            String restParameters, String type, int timeOut);

    /**
     * Executing REST Calls GET or POST with TimeOut
     * 
     * @param baseUrl
     * @param path
     * @param restParameters
     * @param type
     * @return
     */

    HttpResponse executeREST(String hostName, String path,
            String restParameters, String restType, int timeOut, boolean https);
}
