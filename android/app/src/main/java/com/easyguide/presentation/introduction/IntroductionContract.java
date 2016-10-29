package com.easyguide.presentation.introduction;

import com.easyguide.presentation.BasePresenter;
import com.easyguide.presentation.BaseView;

public class IntroductionContract {

    interface View extends BaseView<Presenter> {
        void showDefaultProgress();
        void dismissProgress();
        void startLoginActivity();
    }

    interface Presenter extends BasePresenter {
        void setupFirstAccess();
    }

}
