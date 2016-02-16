package com.sharkfeed.business.contracts;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vraman on 2/11/16.
 */
public class PhotoListContract {
    @SerializedName("page")
    public int page;

    @SerializedName("pages")
    public int pages;

    @SerializedName("perpage")
    public int perpage;

    @SerializedName("total")
    public String total;

    @SerializedName("photo")
    public List<PhotoContract> photoContractList;

}
