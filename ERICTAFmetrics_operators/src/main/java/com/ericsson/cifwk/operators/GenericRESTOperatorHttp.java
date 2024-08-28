package com.ericsson.cifwk.operators;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.annotations.VUserScoped;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.tools.http.HttpResponse;
import com.ericsson.cifwk.taf.tools.http.HttpTool;
import com.ericsson.cifwk.taf.tools.http.RequestBuilder;
import com.ericsson.cifwk.taf.tools.http.HttpToolBuilder;
import com.ericsson.cifwk.taf.tools.http.constants.ContentType;

@Operator(context = Context.REST)
@VUserScoped
public class GenericRESTOperatorHttp implements GenericRESTOperator {

    String[] parameters;
    HttpResponse response;
    Logger logger = Logger.getLogger(GenericRESTOperatorHttp.class);
    public static final String JSON = "application/json";
    public static final String XML = "application/xml";
    public static final String ANY = "*/*";

    @Override
    public HttpResponse executeRESTwithJSONBody(String baseUrl, String path,
            String jsonBody, String type, int timeOut) {
        Host host = null;
        HttpTool tool;
        RequestBuilder request;
        try {
            host = DataHandler.getHostByName(baseUrl);
        } catch (Exception error) {
            logger.debug("BaseUrl: " + baseUrl + " Unable to get Host object");
            error.printStackTrace();
        }

        tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(true)
                .trustSslCertificates(true).build();

        String url = "/" + path;
        request = tool.request().header("Accept", JSON)
                .contentType(ContentType.APPLICATION_JSON).body(jsonBody)
                .timeout(timeOut);
        if (type == "GET") {
            response = request.get(url);
        } else {
            response = request.post(url);
        }
        logger.info("Result: " + response.getBody());
        return response;
    }

    @Override
    public HttpResponse executeREST(String baseUrl, String path,
            String restParameters, String type, int timeOut) {
        Host host = null;
        HttpTool tool;
        try {
            host = DataHandler.getHostByName(baseUrl);
        } catch (Exception error) {
            logger.debug("BaseUrl: " + baseUrl + " Unable to get Host object");
            logger.error("Error with Host", error);
        }
        tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(true)
                .trustSslCertificates(true).build();

        String url = "/" + path;

        if (type.equals("POST")) {
            RequestBuilder request = tool.request().header("Accept", ANY)
                    .contentType(ContentType.APPLICATION_FORM_URLENCODED)
                    .timeout(timeOut);
            if (restParameters != null) {
                restParameters = restParameters.replaceAll("'", "\"");
                request.body(restParameters);
            }
            response = request.post(url);
        } else {
            RequestBuilder request = tool.request()
                    .header("Accept", XML)
                    .contentType(ContentType.APPLICATION_XML).timeout(timeOut);
            if (restParameters != null) {
                parameters = restParameters.split(",");
                for (String parameter : parameters) {
                    String[] split = parameter.split("=");
                    request.queryParam(split[0], split[1]);
                }
            }
            response = request.get(url);
        }
        logger.info("Result: " + response.getBody());
        return response;
    }

    @Override
    public HttpResponse executeREST(String baseUrl, String path,
            String restParameters, String type, int timeOut, boolean https) {
        Host host = null;
        HttpTool tool;
        try {
            host = DataHandler.getHostByName(baseUrl);
        } catch (Exception error) {
            logger.debug("BaseUrl: " + baseUrl + " Unable to get Host object");
            error.printStackTrace();
        }
        tool = HttpToolBuilder.newBuilder(host).useHttpsIfProvided(https)
                .trustSslCertificates(https).build();

        String url = "/" + path;

        if (type.equals("POST")) {
            RequestBuilder request = tool.request().header("Accept", ANY)
                    .contentType(ContentType.APPLICATION_FORM_URLENCODED)
                    .timeout(timeOut);
            if (restParameters != null) {
                restParameters = restParameters.replaceAll("'", "\"");
                request.body(restParameters);
            }
            response = request.post(url);
        } else {
            RequestBuilder request = tool.request()
            		.header("Accept", ANY)
                    .contentType(ContentType.APPLICATION_FORM_URLENCODED).timeout(timeOut);
            if (restParameters != null) {
                parameters = restParameters.split(",");
                for (String parameter : parameters) {
                    String[] split = parameter.split("=");
                    request.queryParam(split[0], split[1]);
                }
            }
            response = request.get(url);
            
        }
        logger.info("Result: " + response.getBody());
        return response;
    }
}
