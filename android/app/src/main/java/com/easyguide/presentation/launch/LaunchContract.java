package com.easyguide.presentation.launch;

import com.easyguide.presentation.BasePresenter;
import com.easyguide.presentation.BaseView;

public class LaunchContract {

    public interface View extends BaseView<Presenter> {
        void startIntroductionActivity();

        void startLoginActivity();

        void startHomeActivity();
    }

    public interface Presenter extends BasePresenter {}

}
