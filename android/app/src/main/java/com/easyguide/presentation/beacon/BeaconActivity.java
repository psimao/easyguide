package com.easyguide.presentation.beacon;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.easyguide.BaseActivity;
import com.easyguide.R;
import com.easyguide.data.entity.Beacon;
import com.easyguide.data.entity.BeaconContent;
import com.easyguide.data.entity.User;
import com.easyguide.util.Logger;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BeaconActivity extends BaseActivity {

    public static final String EXTRA_BEACON = "com.easyguide.presentation.beacon.BaseActivity.extraBeacon";
    public static final String EXTRA_USER = "com.easyguide.presentation.beacon.BaseActivity.extraUser";

    private Beacon beacon;
    private User user;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.imageview_beacon)
    ImageView imageViewBeacon;

    @BindView(R.id.textview_beacon_description)
    TextView textViewBeaconDescription;
    @BindView(R.id.textview_beacon_content)
    TextView textViewBeaconContent;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);

        ButterKnife.bind(this);

        beacon = (Beacon) getIntent().getSerializableExtra(EXTRA_BEACON);
        user = (User) getIntent().getSerializableExtra(EXTRA_USER);

        if (Build.VERSION.SDK_INT > 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        HashMap<String, BeaconContent> contents = beacon.getContent();
        BeaconContent beaconContent = null;
        if (contents != null) {
            String key = contents.entrySet().iterator().next().getKey();
            beaconContent = contents.get(key);
        }

        String imageUrl = beaconContent != null ? beaconContent.getImageUrl() : null;
        String description = beaconContent != null ? beaconContent.getTextDescription() : getString(R.string.main_undefined_beacon);
        String descriptionAccessibility = beaconContent != null ? beaconContent.getSpeechDescription() : getString(R.string.main_undefined_beacon);
        String content = beaconContent != null ? beaconContent.getTextContent() : getString(R.string.main_undefined_beacon);
        String contentAccessibility = beaconContent != null ? beaconContent.getSpeechContent() : getString(R.string.main_undefined_beacon);

        textViewBeaconDescription.setText(description);
        textViewBeaconDescription.setContentDescription(descriptionAccessibility);
        textViewBeaconContent.setText(Html.fromHtml(content));
        textViewBeaconContent.setContentDescription(contentAccessibility);

        if (imageUrl != null) {
            Picasso.with(this)
                    .load(imageUrl)
                    .into(imageViewBeacon);
        } else {
            Picasso.with(this)
                    .load(R.drawable.bg_beacon_256)
                    .into(imageViewBeacon);
        }

        collapsingToolbarLayout.setTitle(getString(R.string.beacon_title));
        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(this, android.R.color.transparent));
        collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(this, android.R.color.transparent));
        collapsingToolbarLayout.setStatusBarScrimColor(ContextCompat.getColor(this, android.R.color.transparent));
        supportStartPostponedEnterTransition();

        Logger.createAndRegisterLog(
                getString(R.string.log_beacon_detail_title).replace("{beacon}", beaconContent.getTextDescription()),
                beaconContent.getTextContent(),
                user.getName()
        );
    }
}
