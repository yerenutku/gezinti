package com.hackathon.gezinti.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.hackathon.gezinti.R;

/**
 * Created by yutku on 26/03/17.
 */

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    public void showWaitingDialog() {
        if (mProgressDialog == null)
            mProgressDialog = ProgressDialog.show(this, "", getString(R.string.loading_text), true, true);
        mProgressDialog.show();
    }

    public void dismissWaitingDialog() {
        if (mProgressDialog == null) return;
        mProgressDialog.dismiss();
    }

    public void showErrorMessage(String errorMessage) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.error_msg_general_title))
                .setMessage(errorMessage)
                .setNeutralButton(getString(R.string.error_msg_btn_txt), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
