package com.cravings.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cravings.R;

/**
 * Created by mremondi on 11/5/16.
 */

public class AboutUsFragment extends Fragment {

    public static final String TAG = "AboutUsFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.about_us_fragment, null, false);
    }
}
