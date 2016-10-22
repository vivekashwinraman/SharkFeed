package com.sharkfeed.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharkfeed.R;
import com.sharkfeed.apicommunicators.FlickrApiCommunicator;
import com.sharkfeed.apicommunicators.ServerInteractor;
import com.sharkfeed.business.contracts.FlickrPhotoInfoContract;
import com.sharkfeed.business.managers.FlickrManager;
import com.sharkfeed.modelobjects.ServerResponseObject;
import com.sharkfeed.modelobjects.SharkItem;
import com.sharkfeed.receivers.DownloadReceiver;
import com.sharkfeed.services.DownloadImageService;
import com.sharkfeed.uicontrols.SharkFeedProgressDialog;
import com.squareup.picasso.Picasso;

import rx.Subscriber;


public class ImageDetailActivity extends BaseActivity implements DownloadReceiver.Receiver {
    private static final String TAG = "ImageDetailActivity";
    private ImageView imageDetailView;
    private ImageButton downloadButton;
    private ImageButton openInApp;
    private TextView imageDescription;
    private SharkItem sharkItem;
    private String flickrUrl;
    private DownloadReceiver downloadReceiver;
    private ProgressDialog sharkFeedProgressDialog;

    /************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        downloadReceiver = new DownloadReceiver(new Handler());
        downloadReceiver.setReceiver(this);
        sharkItem = (SharkItem) getIntent().getParcelableExtra("image");
        imageDetailView = (ImageView) findViewById(R.id.imageDetail);
        imageDescription = (TextView) findViewById(R.id.imageDescription);
        imageDescription.setText(sharkItem.getTitle());
        Picasso.with(this).load(sharkItem.getImageUrlLarge()).into(imageDetailView);
        downloadButton = (ImageButton) findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharkFeedProgressDialog = SharkFeedProgressDialog.create(ImageDetailActivity.this, getString(R.string.downloading));
                sharkFeedProgressDialog.show();
                Intent intent = new Intent(getApplicationContext(), DownloadImageService.class);
                intent.putExtra("downloadImageUrl", sharkItem.getImageUrlLarge());
                intent.putExtra("receiver", downloadReceiver);
                startService(intent);
            }
        });
        openInApp = (ImageButton) findViewById(R.id.openInApp);
        openInApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flickrUrl == null || flickrUrl.isEmpty()) {
                    sharkFeedProgressDialog = SharkFeedProgressDialog.create(ImageDetailActivity.this, getString(R.string.opening_in_app));
                    sharkFeedProgressDialog.show();
                    getFlickrPhotoInfo(sharkItem.getId());
                } else {
                    openUrlInFlickrApp();
                }
            }
        });
    }

    /************************************************************************************************/
    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        sharkFeedProgressDialog.dismiss();
        switch (resultCode) {
            case DownloadImageService.DOWNLOAD_SUCCESS:
                Toast.makeText(getApplicationContext(),
                        getString(R.string.image_download_success),
                        Toast.LENGTH_SHORT).show();
                break;
            case DownloadImageService.DOWNLOAD_ERROR:
                Toast.makeText(getApplicationContext(), getString(R.string.image_download_failed),
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /************************************************************************************************/
    private void getFlickrPhotoInfo(String photoId) {
        String endpoint = "?method=flickr.photos.getInfo&api_key=" + FlickrApiCommunicator.API_KEY + "&photo_id=" + photoId + "&format=json&nojsoncallback=1";
        Subscriber<ServerResponseObject> subscriber = new Subscriber<ServerResponseObject>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ServerResponseObject serverResponseObject) {
                onPhotoInfoResponse(serverResponseObject);
            }
        };
        compositeSubscription.add(new ServerInteractor(new FlickrManager(FlickrPhotoInfoContract.class, endpoint), FlickrManager.class).makeServerRequest(subscriber));
    }

    /************************************************************************************************/
    private void onPhotoInfoResponse(ServerResponseObject serverResponseObject) {
        sharkFeedProgressDialog.dismiss();
        if (serverResponseObject.isSuccess()) {
            FlickrPhotoInfoContract flickrPhotoInfoContract = (FlickrPhotoInfoContract) (serverResponseObject.getResponseObject());
            this.flickrUrl = flickrPhotoInfoContract.photoInfoContract.urls.url.get(0).content;
            openUrlInFlickrApp();
        } else {
            showErrorDialog(getResources().getString(R.string.failed_to_get_image_info));
        }
    }

    /************************************************************************************************/
    private void openUrlInFlickrApp() {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(flickrUrl));
            startActivity(browserIntent);
        } catch (Exception e) {
            showErrorDialog(getResources().getString(R.string.unknown_error));
            Log.e(TAG, "exception occurred while opening url");
        }
    }
    /************************************************************************************************/
}
