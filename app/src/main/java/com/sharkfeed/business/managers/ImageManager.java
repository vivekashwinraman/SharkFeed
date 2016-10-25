package com.sharkfeed.business.managers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by vraman on 10/25/16.
 */

public class ImageManager {

    private LruCache<String, Bitmap> imageCache;

    /************************************************************************************************/
    public ImageManager() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        imageCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    /************************************************************************************************/
    public static Bitmap getResizedBitmap(Bitmap image, int newHeight, int newWidth) {
        int width = image.getWidth();
        int height = image.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
                matrix, false);
        return resizedBitmap;
    }

    /************************************************************************************************/
    public void loadUrlImage(ImageView imageView, String url, int width, int height) {
        new BitmapWorkerTask(imageView, url, width, height).execute();
    }

    /************************************************************************************************/
    public Bitmap getBitmapFromCache(String key) {
        return imageCache.get(key);
    }

    /************************************************************************************************/
    private void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromCache(key) == null) {
            imageCache.put(key, bitmap);
        }
    }

    /************************************************************************************************/
    private class BitmapWorkerTask extends AsyncTask<Void, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewWeakReference;
        private String imageUrl;
        private int width;
        private int height;

        public BitmapWorkerTask(ImageView imageView, String url, int width, int height) {
            imageViewWeakReference = new WeakReference<ImageView>(imageView);
            this.imageUrl = url;
            this.width = width;
            this.height = height;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL url = new URL(imageUrl);
                return BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                Log.i("bitmaptask", "error occurred");
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewWeakReference != null && bitmap != null) {
                final ImageView imageView = imageViewWeakReference.get();
                if (imageView != null) {
                    Bitmap resizedBitmap = getResizedBitmap(bitmap, width, height);
                    imageView.setImageBitmap(resizedBitmap);
                    addBitmapToCache(imageUrl, resizedBitmap);
                }
            }
        }
    }
    /************************************************************************************************/
}
