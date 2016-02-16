package com.sharkfeed.business.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vraman on 2/13/16.
 */
public class FlickrPhotoContract {

    @SerializedName("photos")
    public PhotoListContract photoListContract;

    @SerializedName("stat")
    public String stat;
}
