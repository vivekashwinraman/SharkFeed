package com.sharkfeed.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sharkfeed.R;
import com.sharkfeed.activities.ImageDetailActivity;
import com.sharkfeed.business.managers.ImageManager;
import com.sharkfeed.modelobjects.ImageItem;

import java.util.List;

/**
 * Created by vraman on 2/9/16.
 */
public class ImageItemAdapter extends RecyclerView.Adapter<ImageItemAdapter.ViewHolder> {
    private List<ImageItem> imageItemList;
    private ImageManager imageManager;
    private Context context;

    /************************************************************************************************/
    public ImageItemAdapter(List<ImageItem> imageItemList, Context context) {
        this.imageItemList = imageItemList;
        this.context = context;
        imageManager = new ImageManager();
    }

    /************************************************************************************************/
    @Override
    public ImageItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shark_item, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    /************************************************************************************************/
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ImageItem imageItem = imageItemList.get(position);
        if (imageItem != null) {
            final Bitmap bitmap = imageManager.getBitmapFromCache(imageItem.getImageUrlThumbnail());
            if (bitmap == null) {
                viewHolder.imgViewIcon.setImageResource(R.mipmap.ic_launcher);
                imageManager.loadUrlImage(viewHolder.imgViewIcon, imageItem.getImageUrlThumbnail(), 100, 100);
            } else {
                viewHolder.imgViewIcon.setImageBitmap(bitmap);
            }
        }
    }

    /************************************************************************************************/
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imgViewIcon;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ImageDetailActivity.class);
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("image", imageItemList.get(getAdapterPosition()));
            intent.putExtras(mBundle);
            context.startActivity(intent);
        }
    }
    /************************************************************************************************/
    @Override
    public int getItemCount() {
        return imageItemList.size();
    }
    /************************************************************************************************/
}
