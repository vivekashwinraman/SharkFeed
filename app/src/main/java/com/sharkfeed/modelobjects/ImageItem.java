package com.sharkfeed.modelobjects;

import android.os.Parcel;
import android.os.Parcelable;

import com.sharkfeed.business.contracts.PhotoContract;

/**
 * Created by vraman on 2/9/16.
 */
public class ImageItem implements Parcelable {
    private String id;
    private String imageUrlThumbnail;
    private String imageUrlLarge;
    private String title;

    /************************************************************************************************/
    public ImageItem() {

    }

    /************************************************************************************************/
    public ImageItem(PhotoContract photoContract) {
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
    public static final Parcelable.Creator<ImageItem> CREATOR = new Creator<ImageItem>() {
        public ImageItem createFromParcel(Parcel source) {
            ImageItem imageItem = new ImageItem();
            imageItem.id = source.readString();
            imageItem.imageUrlThumbnail = source.readString();
            imageItem.imageUrlLarge = source.readString();
            imageItem.title = source.readString();
            return imageItem;
        }

        public ImageItem[] newArray(int size) {
            return new ImageItem[size];
        }
    };
    /************************************************************************************************/
}