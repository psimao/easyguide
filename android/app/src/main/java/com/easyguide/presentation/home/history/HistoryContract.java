package com.easyguide.presentation.home.history;

import com.easyguide.data.entity.Log;
import com.easyguide.presentation.BasePresenter;
import com.easyguide.presentation.BaseView;

import java.util.List;

public class HistoryContract {

    public interface View extends BaseView<Presenter> {
        void showLoadingView();
        void hideLoadingView();
        void setHistory(List<Log> logList);
    }

    public interface Presenter extends BasePresenter {
        void loadHistory();
    }
}
