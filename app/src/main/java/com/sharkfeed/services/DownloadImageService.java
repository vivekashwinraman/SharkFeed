package com.sharkfeed.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;

import com.google.common.io.Files;
import com.ning.http.client.Response;
import com.sharkfeed.apicommunicators.HttpCommunicator;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DownloadImageService extends IntentService {
    public static final int DOWNLOAD_ERROR = 0;
    public static final int DOWNLOAD_SUCCESS = 1;
    private static final String DIR_NAME = "SharkFeed";

    public DownloadImageService() {
        super("DownloadImageService");
    }

    /************************************************************************************************/
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String url = intent.getStringExtra("downloadImageUrl");
            final ResultReceiver receiver = intent.getParcelableExtra("receiver");
            Bundle bundle = new Bundle();
            try {
                File sharkFeedDir = new File(Environment.getExternalStorageDirectory() + "/" + DIR_NAME);
                if (!sharkFeedDir.exists()) {
                    sharkFeedDir.mkdir();
                }
                File downloadFile = new File(sharkFeedDir.getPath() + "/" +
                        new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".png");
                downloadFile.createNewFile();
                HttpCommunicator httpCommunicator = new HttpCommunicator();
                Response response = httpCommunicator.makeHttpGet(url);
                if (response.getStatusCode() != 200) {
                    throw new Exception("Error in connection");
                }
                InputStream is = response.getResponseBodyAsStream();
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                Files.write(buffer, downloadFile);
                bundle.putString("filePath", downloadFile.getPath());
                receiver.send(DOWNLOAD_SUCCESS, bundle);
            } catch (Exception e) {
                receiver.send(DOWNLOAD_ERROR, Bundle.EMPTY);
                e.printStackTrace();
            }
        }
    }
    /************************************************************************************************/
}
