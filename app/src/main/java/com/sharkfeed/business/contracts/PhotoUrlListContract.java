package com.sharkfeed.business.contracts;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vraman on 2/15/16.
 */
public class PhotoUrlListContract {

    @SerializedName("url")
    public List<PhotoUrlContract> url;
}
