package com.easyguide.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easyguide.R;
import com.easyguide.data.entity.Beacon;
import com.easyguide.data.entity.BeaconContent;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BeaconsAdapter extends RecyclerView.Adapter<BeaconsAdapter.ViewHolder> {

    private Context context;
    private List<Beacon> sourceList;

    public BeaconsAdapter(Context context, List<Beacon> sourceList) {
        this.context = context;
        this.sourceList = sourceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.adapter_beacons, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Beacon beacon = sourceList.get(position);
        BeaconContent beaconContent = beacon.getContent() != null ? beacon.getContent().get(0) : null;
        if (beaconContent != null) {
            holder.textViewDescription.setText(beaconContent.getTextDescription());
            Picasso.with(context)
                    .load(beaconContent.getImageUrl())
                    .placeholder(R.drawable.bg_beacon_256)
                    .error(R.drawable.bg_beacon_256)
                    .into(holder.imageViewBeacon);
        } else {
            holder.textViewDescription.setText(context.getText(R.string.main_undefined_beacon));
            Picasso.with(context)
                    .load(R.drawable.bg_beacon_256)
                    .into(holder.imageViewBeacon);
        }
    }

    @Override
    public int getItemCount() {
        return sourceList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageview_beacon)
        ImageView imageViewBeacon;
        @BindView(R.id.textview_description)
        TextView textViewDescription;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
