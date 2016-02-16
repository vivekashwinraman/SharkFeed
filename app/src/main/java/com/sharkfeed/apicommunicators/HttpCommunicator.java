package com.sharkfeed.apicommunicators;

/**
 * Created by vraman on 2/12/16.
 */

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Response;
import com.sharkfeed.managers.JsonManager;


public class HttpCommunicator {

    private AsyncHttpClient asyncClient;

    /*****************************************************************/
    public HttpCommunicator() {
        AsyncHttpClientConfig.Builder builder = new AsyncHttpClientConfig.Builder();
        builder.setRequestTimeoutInMs(60 * 1000);
        asyncClient = new AsyncHttpClient(builder.build());
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
    public AsyncHttpClient getAsyncClient() {
        return asyncClient;
    }
    /*****************************************************************/
    public void setAsyncClient(AsyncHttpClient asyncClient) {
        this.asyncClient = asyncClient;
    }
    /*****************************************************************/
}

