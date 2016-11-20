package com.easyguide.presentation.home.main;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.easyguide.BaseFragment;
import com.easyguide.R;
import com.easyguide.data.entity.Beacon;
import com.easyguide.data.entity.BeaconContent;
import com.easyguide.data.entity.mapper.UserMapper;
import com.easyguide.injection.BeaconInjection;
import com.easyguide.injection.RepositoryInjection;
import com.easyguide.injection.SchedulerProviderInjection;
import com.easyguide.presentation.beacon.BeaconActivity;
import com.easyguide.presentation.login.LoginActivity;
import com.easyguide.ui.adapter.BeaconsAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends BaseFragment implements MainContract.View, BeaconsAdapter.OnItemClickListener {

    private static final int RECYCLERVIEW_VERTICAL_SPAN_SIZE = 2;
    private static final int RECYCLERVIEW_HORIZONTAL_SPAN_SIZE = 3;

    private static final int RESULT_REQUEST_PERMISSION = 539;
    private static final int RESULT_REQUEST_BLUETOOTH_ACTIVATION = 997;

    @BindView(R.id.framelayout_main)
    FrameLayout frameLayoutMain;

    @BindView(R.id.recyclerview_beacons)
    RecyclerView recyclerViewBeacons;

    @BindView(R.id.linearlayout_loading)
    LinearLayout linearLayoutLoading;

    private MainContract.Presenter presenter;

    private BeaconsAdapter beaconsAdapter;
    private Beacon previousNearestBeacon;

    @Override
    protected int fragmentTitleResourceId() {
        return R.string.main_title;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        setHasOptionsMenu(true);
        Context applicationContext = getActivity().getApplicationContext();
        new MainPresenter(
                this,
                RepositoryInjection.provideUserRepository(applicationContext),
                RepositoryInjection.provideBeaconRepository(),
                BeaconInjection.provideProximityBeaconManager(BeaconInjection.provideBeaconManager(applicationContext)),
                SchedulerProviderInjection.provideSchedulerProvider()
        );
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(checkAllPermissions()) {
            presenter.subscribe();
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.unsubscribe();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                presenter.logout();
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                Snackbar.make(frameLayoutMain, R.string.main_permission_error, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.main_permission_error_action, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                requestPermissions();
                            }
                        })
                        .show();
                return;
            }
            if(checkAllPermissions()) {
                presenter.subscribe();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_REQUEST_BLUETOOTH_ACTIVATION:
                if (resultCode == Activity.RESULT_OK) {
                    if(checkAllPermissions()) {
                        presenter.subscribe();
                    }
                } else {
                    Snackbar.make(frameLayoutMain, R.string.main_bluetooth_permission_error, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.main_bluetooth_permission_error_action, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    requestBluetoothActivation();
                                }
                            })
                            .show();
                }
                break;
        }
    }

    @Override
    public void showLoadingView() {
        recyclerViewBeacons.setVisibility(View.GONE);
        linearLayoutLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingView() {
        recyclerViewBeacons.setVisibility(View.VISIBLE);
        linearLayoutLoading.setVisibility(View.GONE);
    }

    @Override
    public void setBeacon(List<Beacon> beaconsList) {
        final Beacon currentNearestBeacon = beaconsList.isEmpty() ? null : beaconsList.get(0);
        if (beaconsAdapter == null) {
            beaconsAdapter = new BeaconsAdapter(getContext(), beaconsList);
            beaconsAdapter.setOnItemClickListener(this);
            recyclerViewBeacons.setAdapter(beaconsAdapter);
            previousNearestBeacon = currentNearestBeacon;
        } else {
            beaconsAdapter.setSourceList(beaconsList);
            beaconsAdapter.notifyDataSetChanged();
        }
        if (currentNearestBeacon != null && !currentNearestBeacon.equals(previousNearestBeacon)) {
            previousNearestBeacon = currentNearestBeacon;
            recyclerViewBeacons.post(new Runnable() {
                @Override
                public void run() {
                    HashMap<String, BeaconContent> contents = currentNearestBeacon.getContent();
                    if (contents != null) {
                        String key = contents.entrySet().iterator().next().getKey();
                        BeaconContent beaconContent = contents.get(key);
                        recyclerViewBeacons.getChildAt(0).announceForAccessibility(beaconContent.getSpeechDescription());
                    }
                }
            });
        }
    }

    @Override
    public void destroyAndStartLoginActivity() {
        getActivity().finish();
        startActivity(new Intent(getContext(), LoginActivity.class));
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void setupRecyclerView() {
        int screenOrientation = getContext().getResources().getConfiguration().orientation;
        final int spanSize = screenOrientation == Configuration.ORIENTATION_PORTRAIT ? RECYCLERVIEW_VERTICAL_SPAN_SIZE : RECYCLERVIEW_HORIZONTAL_SPAN_SIZE;
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanSize);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return spanSize;
                }
                return 1;
            }
        });
        recyclerViewBeacons.setLayoutManager(layoutManager);
    }

    private boolean checkAllPermissions() {
        if (!checkPermissions()) {
            requestPermissions();
            return false;
        }
        if (!isBluetoothEnabled()) {
            requestBluetoothActivation();
            return false;
        }
        if (!isLocationEnabled()) {
            requestLocationActivation();
            return false;
        }
        return true;
    }

    /**
     * Checks screen required permissions.
     */
    private boolean checkPermissions() {
        int currentPermission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        return currentPermission == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Requests screen required permissions.
     */
    private void requestPermissions() {
        requestPermissions(
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                RESULT_REQUEST_PERMISSION
        );
    }

    /**
     * Checks bluetooth status.
     */
    private boolean isBluetoothEnabled() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    /**
     * Requests bluetooth activation.
     */
    private void requestBluetoothActivation() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, RESULT_REQUEST_BLUETOOTH_ACTIVATION);
    }

    /**
     * Checks Location (by GPS or Network) status.
     */
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     * Shows location request message and open settings activity on action button click.
     */
    private void requestLocationActivation() {
        Snackbar.make(frameLayoutMain, R.string.main_location_permission_request, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.main_location_permission_request_open_settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .show();
    }

    @Override
    public void OnIemClick(int position, Beacon beacon) {
        Intent intent = new Intent(getContext(), BeaconActivity.class);
        intent.putExtra(BeaconActivity.EXTRA_BEACON, beacon);
        intent.putExtra(BeaconActivity.EXTRA_USER, UserMapper.transform(FirebaseAuth.getInstance().getCurrentUser())); // TODO: Find better way!
        startActivity(intent);
    }
}
