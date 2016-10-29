package com.easyguide.presentation.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.easyguide.BaseActivity;
import com.easyguide.Injection;
import com.easyguide.R;
import com.easyguide.presentation.home.HomeActivity;
import com.easyguide.util.GoogleAuthenticationProvider;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via Google Account.
 */
public class LoginActivity extends BaseActivity implements LoginContract.View, GoogleApiClient.OnConnectionFailedListener {

    private static final int RESULT_GOOGLE_ACCOUNT_LOGIN = 123;

    private LoginContract.Presenter presenter;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        new LoginPresenter(
                this,
                Injection.provideUserRepository(),
                Injection.provideSchedulerProvider()
        );
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_GOOGLE_ACCOUNT_LOGIN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            startHomeActivity();
                        }
                    }
                });
    }

    @Override
    public void showDefaultProgress() {
        progressDialog = ProgressDialog.show(
                this,
                getString(R.string.login_progress_default_title),
                getString(R.string.login_progress_default_message),
                true,
                true,
                null);
    }

    @Override
    public void showLoginErrorMessage(String message) {
        showMessage(R.string.default_error_alert_title, message);
    }

    @Override
    public void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void requestLoginWithGoogleAccount() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(
                GoogleAuthenticationProvider.provideGoogleApiClient(this, this, getString(R.string.default_web_client_id))
        );
        startActivityForResult(signInIntent, RESULT_GOOGLE_ACCOUNT_LOGIN);
    }

    @Override
    public void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @OnClick(R.id.button_login_google)
    public void loginWithGoogleButtonOnClick() {
        this.presenter.loginWithGoogleAccount();
    }
}