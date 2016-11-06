package com.easyguide;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected AlertDialog showDefaultErrorMessage() {
        return showMessage(R.string.default_error_alert_title, R.string.default_error_alert_message);
    }

    protected AlertDialog showMessage(int title, int message) {
        return showMessage(getString(title), getString(message));
    }

    protected AlertDialog showMessage(int title, String message) {
        return showMessage(getString(title), message);
    }

    protected AlertDialog showMessage(String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getBaseContext());
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setNeutralButton(getString(R.string.default_alert_neutral_button),null);
        return alertDialog.show();
    }

}
