package com.sharkfeed.business.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vraman on 2/15/16.
 */
public class FlickrPhotoInfoContract {

    @SerializedName("photo")
    public PhotoInfoContract photoInfoContract;

    @SerializedName("stat")
    public String stat;
}
