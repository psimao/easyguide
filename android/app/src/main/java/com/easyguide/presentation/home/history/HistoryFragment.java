package com.easyguide.presentation.home.history;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.easyguide.BaseFragment;
import com.easyguide.R;
import com.easyguide.data.entity.Log;
import com.easyguide.injection.RepositoryInjection;
import com.easyguide.injection.SchedulerProviderInjection;
import com.easyguide.ui.adapter.HistoryAdapter;
import com.google.common.collect.Lists;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryFragment extends BaseFragment implements HistoryContract.View {

    @BindView(R.id.linearlayout_loading)
    LinearLayout linearLayoutLoading;

    @BindView(R.id.recyclerview_history)
    RecyclerView recyclerViewHistory;

    private HistoryContract.Presenter presenter;

    private HistoryAdapter historyAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        new HistoryPresenter(this, RepositoryInjection.provideLogRepository(), SchedulerProviderInjection.provideSchedulerProvider());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.subscribe();
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    protected int fragmentTitleResourceId() {
        return R.string.history_title;
    }

    @Override
    public void showLoadingView() {
        recyclerViewHistory.setVisibility(View.GONE);
        linearLayoutLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingView() {
        recyclerViewHistory.setVisibility(View.VISIBLE);
        linearLayoutLoading.setVisibility(View.GONE);
    }

    @Override
    public void setHistory(List<Log> logList) {
        logList = Lists.reverse(logList);
        if (historyAdapter == null) {
            historyAdapter = new HistoryAdapter(getContext(), logList);
            recyclerViewHistory.setAdapter(historyAdapter);
        } else if (recyclerViewHistory.getAdapter() == null) {
            recyclerViewHistory.setAdapter(historyAdapter);
        } else {
            historyAdapter.setSourceList(logList);
            historyAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void setPresenter(HistoryContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        recyclerViewHistory.setLayoutManager(layoutManager);
        recyclerViewHistory.addItemDecoration(dividerItemDecoration);
    }
}
