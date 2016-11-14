package com.easyguide.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easyguide.R;
import com.easyguide.data.entity.Log;
import com.easyguide.util.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<Log> sourceList;

    public HistoryAdapter(Context context, List<Log> sourceList) {
        this.context = context;
        this.sourceList = sourceList;
    }

    public void setSourceList(List<Log> sourceList) {
        this.sourceList = sourceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.adapter_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log log = sourceList.get(position);
        holder.textViewTitle.setText(log.getTitle());
        holder.textViewDateTime.setText(DateUtils.timestampToDate(log.getTimestamp(), DateUtils.DATE_FORMAT_PT_BR));
    }

    @Override
    public int getItemCount() {
        return sourceList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview_history_title)
        TextView textViewTitle;
        @BindView(R.id.textview_history_datetime)
        TextView textViewDateTime;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
