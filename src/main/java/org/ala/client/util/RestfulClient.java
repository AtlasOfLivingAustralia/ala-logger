/**************************************************************************
 *  Copyright (C) 2010 Atlas of Living Australia
 *  All Rights Reserved.
 *
 *  The contents of this file are subject to the Mozilla Public
 *  License Version 1.1 (the "License"); you may not use this file
 *  except in compliance with the License. You may obtain a copy of
 *  the License at http://www.mozilla.org/MPL/
 *
 *  Software distributed under the License is distributed on an "AS
 *  IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 *  implied. See the License for the specific language governing
 *  rights and limitations under the License.
 ***************************************************************************/

package org.ala.client.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Restful Web Service Client.
 *
 * @author MOK011
 */
public class RestfulClient {
    private static MultiThreadedHttpConnectionManager connManager = new MultiThreadedHttpConnectionManager();
    private static final String JSON_MIME_TYPE = "application/json";
    private static final String ENCODE_TYPE = "utf-8";
    private ObjectMapper serMapper;
    private HttpClient client;

    //client connection timeout.
    public int timeout;

    public RestfulClient() {
        //default is no timeout.
        this(0);
    }

    public RestfulClient(int timeout) {
        super();
        this.timeout = timeout;
        //create the client to call the logger REST api
        client = new HttpClient(connManager);
        //set connection timeout
        client.getParams().setSoTimeout(timeout);

        serMapper = new ObjectMapper();
        serMapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    }

    /**
     * Makes a POST request to the specified URL and passes the provided JSON Object
     *
     * @param url             URL Endpoint
     * @param mimeType        return mimeType
     * @param jsonRequestBody JSON Object to post to URL
     * @return [0]: status code; [1]: a JSON encoded response
     * @throws IOException
     * @throws HttpException
     */
    public Object[] restPost(String url, String contentType, String jsonRequestBody) throws HttpException, IOException {
        PostMethod post = null;
        String resp = null;
        int statusCode = 0;

        try {
            post = new PostMethod(url);

            String userAgent = (String) MDC.get(Constants.USER_AGENT_PARAM);
            if (StringUtils.isBlank(userAgent)) {
                userAgent = Constants.UNDEFINED_USER_AGENT_VALUE;
            }
            post.setRequestHeader(new Header(Constants.USER_AGENT_PARAM, userAgent));

            RequestEntity entity = new StringRequestEntity(jsonRequestBody, contentType, ENCODE_TYPE);
            post.setRequestEntity(entity);

            statusCode = client.executeMethod(post);
            if (statusCode == HttpStatus.SC_OK) {
                resp = post.getResponseBodyAsString();
            }
        } finally {
            if (post != null) {
                post.releaseConnection();
            }
        }
        Object[] o = new Object[]{statusCode, resp};
        return o;
    }

    /**
     * Makes a POST request to the specified URL and passes the provided JSON Object
     *
     * @param url             URL Endpoint
     * @param jsonRequestBody JSON Object to post to URL
     * @return [0]: status code; [1]: a JSON encoded response
     * @throws IOException
     * @throws HttpException
     */
    public Object[] restPost(String url, String jsonRequestBody) throws HttpException, IOException {
        return this.restPost(url, JSON_MIME_TYPE, jsonRequestBody);
    }

    /**
     * Makes a POST request to the specified URL and passes the provided JSON Object
     *
     * @param url             URL Endpoint
     * @param jsonRequestBody JSON Object to post to URL
     * @return [0]: status code; [1]: a JSON encoded response
     * @throws IOException
     * @throws HttpException
     */
    public Object[] restPost(String url, String mimeType, Collection object) throws HttpException, IOException {
        return this.restPost(url, mimeType, serMapper.writeValueAsString(object.toArray()));
    }

    /**
     * Makes a GET request to the specified url.
     *
     * @param url URL Endpoint with request parameters.
     * @return [0]: status code; [1]: a JSON encoded response.
     * @throws IOException
     * @throws HttpException
     */
    public Object[] restGet(String url) throws HttpException, IOException {
        return restGet(url, null);
    }

    /**
     * Makes a GET request to the specified url.
     * if header is null then server will return default data type.
     *
     * @param url    URL Endpoint with request parameters.
     * @param header eg: "Accept" "application/json"; "Accept" "text/xml"
     * @return [0]: status code; [1]: a JSON encoded response.
     * @throws IOException
     * @throws HttpException
     */
    public Object[] restGet(String url, Map<String, String> header) throws HttpException, IOException {
        GetMethod get = null;
        String resp = "";
        int statusCode = 0;

        try {
            get = new GetMethod(url);
            if (header != null && !header.isEmpty()) {
                Set<String> keys = header.keySet();
                Iterator<String> it = keys.iterator();
                while (it.hasNext()) {
                    String key = it.next();
                    get.setRequestHeader(key, header.get(key));
                }
            }
            statusCode = client.executeMethod(get);
            resp = get.getResponseBodyAsString();
        } finally {
            if (get != null) {
                get.releaseConnection();
            }
        }
        Object[] o = new Object[]{statusCode, resp};
        return o;
    }
}
