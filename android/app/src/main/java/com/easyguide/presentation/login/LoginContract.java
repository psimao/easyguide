package com.easyguide.presentation.login;

import com.easyguide.presentation.BasePresenter;
import com.easyguide.presentation.BaseView;

/**
 * Specification of contract between Login View and Presenter
 */
public class LoginContract {

    interface View extends BaseView<Presenter> {
        void showDefaultProgress();
        void showLoginErrorMessage(String message);
        void dismissProgress();
        void requestLoginWithGoogleAccount();
        void startHomeActivity();
    }

    interface Presenter extends BasePresenter {
        void loginWithGoogleAccount();
    }
}
