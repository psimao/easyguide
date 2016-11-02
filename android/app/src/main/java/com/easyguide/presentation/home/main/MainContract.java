package com.easyguide.presentation.home.main;

import com.easyguide.data.entity.Beacon;
import com.easyguide.presentation.BasePresenter;
import com.easyguide.presentation.BaseView;

import java.util.List;

public class MainContract {

    public interface View extends BaseView<Presenter> {
        void showLoadingView();
        void hideLoadingView();
        void setBeacon(List<Beacon> beaconsList);
        void destroyAndStartLoginActivity();
    }

    public interface Presenter extends BasePresenter {
        void logout();
    }
}
