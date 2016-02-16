package com.sharkfeed.business.contracts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vraman on 2/15/16.
 */
public class PhotoUrlContract {

    @SerializedName("type")
    public String type;

    @SerializedName("_content")
    public String content;
}
