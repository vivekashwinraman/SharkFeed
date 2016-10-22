package com.sharkfeed.apicommunicators;

/**
 * Created by vraman on 2/12/16.
 */

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.sharkfeed.managers.JsonManager;


public class HttpCommunicator {

    private AsyncHttpClient asyncClient;

    /*****************************************************************/
    public HttpCommunicator() {
        asyncClient = new AsyncHttpClient();
    }

    /*****************************************************************/
    public Response makeHttpGet(String apiEndpoint) throws Exception {
        Response retResp = null;
        retResp = asyncClient.prepareGet(apiEndpoint).execute().get();
        return retResp;
    }

    /*****************************************************************/

    public Object parseHttpResponseToJsonObject(Response response, Class<?> type) {
        try {
            if (response.getStatusCode() == 200) {
                return JsonManager.parseFromJSONToObject(response.getResponseBody(), type);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /*****************************************************************/
}

