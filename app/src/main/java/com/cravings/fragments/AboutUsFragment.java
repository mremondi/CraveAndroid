package com.cravings.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cravings.R;

/**
 * Created by mremondi on 11/5/16.
 */

public class AboutUsFragment extends Fragment {

    public static final String TAG = "AboutUsFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.about_us_fragment, null, false);

        TextView tvSplashTitle = (TextView) rootView.findViewById(R.id.tvAboutAppName);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "Milkshake.ttf");
        tvSplashTitle.setTypeface(font);
        return rootView;
    }
}
