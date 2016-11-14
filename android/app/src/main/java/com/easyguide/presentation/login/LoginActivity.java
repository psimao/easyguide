package com.easyguide.presentation.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.easyguide.BaseActivity;
import com.easyguide.R;
import com.easyguide.data.entity.User;
import com.easyguide.data.entity.mapper.UserMapper;
import com.easyguide.injection.RepositoryInjection;
import com.easyguide.injection.SchedulerProviderInjection;
import com.easyguide.presentation.home.HomeActivity;
import com.easyguide.util.GoogleAuthenticationProvider;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via Google Account.
 */
public class LoginActivity extends BaseActivity implements LoginContract.View {

    private static final int RESULT_GOOGLE_ACCOUNT_LOGIN = 123;

    private LoginContract.Presenter presenter;

    private ProgressDialog progressDialog;

    @BindView(R.id.imageview_login)
    ImageView imageViewLogin;

    @BindView(R.id.textview_login)
    TextView textViewLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        new LoginPresenter(
                this,
                RepositoryInjection.provideUserRepository(getApplicationContext()),
                SchedulerProviderInjection.provideSchedulerProvider()
        );
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
                User user = UserMapper.transform(account);
                presenter.login(user);
            }
        }
    }

    @Override
    public void showDefaultProgress() {
        progressDialog = ProgressDialog.show(
                this,
                null,
                getString(R.string.default_progress_message),
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
                GoogleAuthenticationProvider.provideGoogleApiClient(
                        getApplicationContext(),
                        getString(R.string.default_web_client_id)
                )
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

    @OnClick(R.id.button_login_google)
    public void loginWithGoogleButtonOnClick() {
        this.requestLoginWithGoogleAccount();
    }
}