package com.sharkfeed.uicontrols;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.sharkfeed.R;

/**
 * Created by vraman on 2/15/16.
 */
public class SharkFeedProgressDialog extends ProgressDialog {

    private String progressText;

    /************************************************************************************************/
    public static ProgressDialog create(Context context, String progressText) {
        SharkFeedProgressDialog loginProgressDialog = new SharkFeedProgressDialog(context);
        loginProgressDialog.setIndeterminate(true);
        loginProgressDialog.setCancelable(false);
        loginProgressDialog.progressText = progressText;
        return loginProgressDialog;
    }

    /************************************************************************************************/
    public SharkFeedProgressDialog(Context context) {
        super(context);
    }

    /************************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_sharkfeed_progess);
        TextView progressText = (TextView) findViewById(R.id.progress_text);
        progressText.setText(this.progressText);
    }
/************************************************************************************************/
}
