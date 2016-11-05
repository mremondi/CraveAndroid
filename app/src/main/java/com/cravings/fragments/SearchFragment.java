package com.cravings.fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cravings.R;
import com.cravings.adapters.CraveLocationManager;
import com.cravings.adapters.SearchPagerAdapter;
import com.google.android.gms.maps.model.LatLng;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


public class SearchFragment extends Fragment {
    public static final String TAG = "SearchFragment";

    private boolean searchItems = true;

    private CraveLocationManager craveLocationManager;
    private LatLng location;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_pager_view, null, false);

        craveLocationManager = new CraveLocationManager(this.getContext());
        location = craveLocationManager.getLastKnownLocation();

        PagerTabStrip pagerTabStrip = (PagerTabStrip) rootView.findViewById(R.id.pagerTabStrip);
        pagerTabStrip.setTabIndicatorColor(getResources().getColor(R.color.colorPrimary));

        ViewPager mViewPager = (ViewPager) rootView.findViewById(R.id.searchViewPager);
        mViewPager.setAdapter(new SearchPagerAdapter(getChildFragmentManager()));

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onLocationReceived(Location location) {
        this.location = new LatLng(location.getLatitude(), location.getLongitude());
    }

    public LatLng getLocation(){
        return location;
    }
}
