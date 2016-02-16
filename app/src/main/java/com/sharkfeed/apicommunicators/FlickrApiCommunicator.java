package com.sharkfeed.apicommunicators;

import com.ning.http.client.Response;

/**
 * Created by vraman on 2/12/16.
 */
public class FlickrApiCommunicator {
    public static final String API_KEY = "949e98778755d1982f537d56236bbb42";
    private static final String BASE_URL = "https://api.flickr.com/services/rest/";
    private String endpoint;
    private Class<?> type;

    /*****************************************************************/
    public FlickrApiCommunicator(Class<?> type, String endpoint) {
        this.endpoint = endpoint;
        this.type = type;
    }

    /*****************************************************************/

    public Object httpGet() {
        HttpCommunicator httpCommunicator = new HttpCommunicator();
        Response response = null;
        try {
            response = httpCommunicator.makeHttpGet(BASE_URL + endpoint);
            return httpCommunicator.parseHttpResponseToJsonObject(response, type);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return null;
    }
    /*****************************************************************/
}
