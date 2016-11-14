package com.easyguide.presentation.home.profile;

import com.easyguide.presentation.BasePresenter;
import com.easyguide.presentation.BaseView;

public class ProfileContract {

    public interface View extends BaseView<Presenter> {
        void setUserPicture(String pictureUrl);
        void setUserName(String name);
        void setUserProfile(String profile);
        void setDiscoveryModeStatus(boolean status);
    }

    public interface Presenter extends BasePresenter {
        void loadUser();
        void loadSettings();
        void updateDiscoveryMode(boolean status);
    }
}
