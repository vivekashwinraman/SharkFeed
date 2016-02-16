package com.sharkfeed.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.sharkfeed.business.managers.BaseBusinessManager;
import com.sharkfeed.interfaces.ServerResponseListener;
import com.sharkfeed.modelobjects.ServerResponseObject;

/**
 * Created by vraman on 2/13/16.
 */
public class ServerInteractionTask extends AsyncTask<Void, Void, ServerResponseObject> {

    private static final String TAG = "ServerInteractionTask";
    private ServerResponseListener serverResponseListener;
    private Class<?> type;
    private Object requestManager;

    /************************************************************************************************/
    public ServerInteractionTask(Object requestManager, ServerResponseListener serverResponseListener, Class<?> type) {
        this.serverResponseListener = serverResponseListener;
        this.type = type;
        this.requestManager = requestManager;
    }

    /************************************************************************************************/
    @Override
    protected ServerResponseObject doInBackground(Void... params) {
        return makeServerRequest();
    }

    /************************************************************************************************/
    @Override
    protected void onPostExecute(final ServerResponseObject responseObject) {
        if (serverResponseListener != null) {
            serverResponseListener.onResponseReceived(responseObject);
        }
    }

    /************************************************************************************************/
    private ServerResponseObject makeServerRequest() {
        ServerResponseObject serverResponseObject = new ServerResponseObject(false);
        try {
            Object object = Class.forName(type.getName()).cast(requestManager);
            if (object instanceof BaseBusinessManager) {
                Log.v(TAG, type.getName());
                serverResponseObject = ((BaseBusinessManager) object).sendRequest();
            }
        } catch (Exception e) {
            Log.v(TAG, e.getLocalizedMessage());
        }
        return serverResponseObject;
    }
    /************************************************************************************************/
}
