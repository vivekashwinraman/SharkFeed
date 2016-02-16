package com.sharkfeed.modelobjects;

import android.os.Parcel;
import android.os.Parcelable;

import com.sharkfeed.business.contracts.PhotoContract;

/**
 * Created by vraman on 2/9/16.
 */
public class SharkItem implements Parcelable {
    private String id;
    private String imageUrlThumbnail;
    private String imageUrlLarge;
    private String title;

    /************************************************************************************************/
    public SharkItem() {

    }

    /************************************************************************************************/
    public SharkItem(PhotoContract photoContract) {
        this.id = photoContract.id;
        this.imageUrlThumbnail = photoContract.url_t;
        this.imageUrlLarge = photoContract.url_l;
        this.title = photoContract.title;
    }

    /************************************************************************************************/
    public String getImageUrlThumbnail() {
        return imageUrlThumbnail;
    }

    /************************************************************************************************/
    public String getImageUrlLarge() {
        return imageUrlLarge;
    }

    /************************************************************************************************/
    public String getTitle() {
        return title;
    }

    /************************************************************************************************/
    public String getId() {
        return id;
    }

    /************************************************************************************************/
    public int describeContents() {
        return 0;
    }

    /************************************************************************************************/
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(id);
        parcel.writeString(imageUrlThumbnail);
        parcel.writeString(imageUrlLarge);
        parcel.writeString(title);
    }

    /************************************************************************************************/
    public static final Parcelable.Creator<SharkItem> CREATOR = new Creator<SharkItem>() {
        public SharkItem createFromParcel(Parcel source) {
            SharkItem sharkItem = new SharkItem();
            sharkItem.id = source.readString();
            sharkItem.imageUrlThumbnail = source.readString();
            sharkItem.imageUrlLarge = source.readString();
            sharkItem.title = source.readString();
            return sharkItem;
        }

        public SharkItem[] newArray(int size) {
            return new SharkItem[size];
        }
    };
    /************************************************************************************************/
}