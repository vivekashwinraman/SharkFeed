package com.sharkfeed.interfaces;

import com.sharkfeed.modelobjects.ServerResponseObject;

/**
 * Created by vraman on 2/13/16.
 */
public interface ServerResponseListener {
    public void onResponseReceived(ServerResponseObject responseObject);
}
