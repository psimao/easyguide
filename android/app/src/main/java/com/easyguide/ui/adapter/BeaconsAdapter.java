package com.easyguide.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BeaconsAdapter extends RecyclerView.Adapter<BeaconsAdapter.ViewHolder> {

    private Context context;
    private List<Beacon> sourceList;
    private OnItemClickListener onItemClickListener;

    public BeaconsAdapter(Context context, List<Beacon> sourceList) {
        this.context = context;
        this.sourceList = sourceList;
    }

    public void setSourceList(List<Beacon> sourceList) {
        this.sourceList = sourceList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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
        HashMap<String, BeaconContent> contents = beacon.getContent();
        BeaconContent beaconContent = null;
        if (contents != null && contents.size() > 0) {
            ArrayList<BeaconContent> beaconContents = new ArrayList<>(contents.values());
            for (BeaconContent item : beaconContents) {
                if (item.getProfile() == 1L) {
                    beaconContent = item;
                }
            }
            if (beaconContent == null) {
                beaconContent = beaconContents.get(0);
            }
        }
        String cardContentDescription = "";
        String distanceText = "~" + beacon.getDistance() + "m";
        holder.textViewDistance.setText(distanceText);
        if (beaconContent != null) {
            holder.textViewDescription.setText(beaconContent.getTextDescription());
            cardContentDescription += beaconContent.getSpeechDescription();
            Picasso.with(context)
                    .load(beaconContent.getImageUrl())
                    .placeholder(R.drawable.bg_beacon_256)
                    .error(R.drawable.bg_beacon_256)
                    .into(holder.imageViewBeacon);
        } else {
            holder.textViewDescription.setText(context.getText(R.string.main_undefined_beacon));
            cardContentDescription += context.getText(R.string.main_undefined_beacon);
            Picasso.with(context)
                    .load(R.drawable.bg_beacon_256)
                    .into(holder.imageViewBeacon);
        }
        cardContentDescription += ". " + String.format(context.getString(R.string.adapter_beacon_distance), String.valueOf(beacon.getDistance()));

        holder.cardViewBeacon.setContentDescription(cardContentDescription);
    }

    @Override
    public int getItemCount() {
        return sourceList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.cardview_beacon)
        CardView cardViewBeacon;
        @BindView(R.id.imageview_beacon)
        ImageView imageViewBeacon;
        @BindView(R.id.textview_distance)
        TextView textViewDistance;
        @BindView(R.id.textview_description)
        TextView textViewDescription;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.cardview_beacon)
        void cardViewOnClick() {
            if (onItemClickListener != null) {
                onItemClickListener.OnIemClick(getAdapterPosition(), sourceList.get(getAdapterPosition()));
            }
        }

    }

    public interface OnItemClickListener {
        void OnIemClick(int position, Beacon beacon);
    }
}
