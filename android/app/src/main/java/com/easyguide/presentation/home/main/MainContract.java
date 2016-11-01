package com.easyguide.presentation.home.main;

import com.easyguide.presentation.BasePresenter;
import com.easyguide.presentation.BaseView;

public class MainContract {

    public interface View extends BaseView<Presenter> {
         void destroyAndStartLoginActivity();
    }

    public interface Presenter extends BasePresenter {
        void logout();
    }
}
