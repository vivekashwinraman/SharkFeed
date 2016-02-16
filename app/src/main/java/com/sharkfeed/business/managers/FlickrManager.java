package com.sharkfeed.business.managers;

import android.util.Log;

import com.sharkfeed.apicommunicators.FlickrApiCommunicator;
import com.sharkfeed.modelobjects.ServerResponseObject;

/**
 * Created by vraman on 2/14/16.
 */
public class FlickrManager extends BaseBusinessManager {

    private static final String TAG = "FlickerManager";
    private String endpoint;
    private Class<?> type;

    /*****************************************************************/
    public FlickrManager(Class<?> type, String endpoint) {
        this.type = type;
        this.endpoint = endpoint;
    }

    /*****************************************************************/
    @Override
    public ServerResponseObject sendRequest() {
        ServerResponseObject serverResponseObject = new ServerResponseObject(false);
        try {
            FlickrApiCommunicator flickrApiCommunicator = new FlickrApiCommunicator(this.type, this.endpoint);
            Object object = flickrApiCommunicator.httpGet();
            if (object != null) {
                serverResponseObject.setResponseObject(object);
                serverResponseObject.setSuccess(true);
            }
        } catch (Exception e) {
            Log.v(TAG, e.getLocalizedMessage());
        }
        return serverResponseObject;
    }
    /*****************************************************************/
}
