package com.sharkfeed.modelobjects;

/**
 * Created by vraman on 2/13/16.
 */
public class ServerResponseObject {
    private boolean success;
    private Object responseObject;

    /************************************************************************************************/
    public ServerResponseObject(boolean success) {
        this.success = success;
    }

    /************************************************************************************************/
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(Object responseObject) {
        this.responseObject = responseObject;
    }
}

