package com.cravings.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cravings.R;

/**
 * Created by mremondi on 11/5/16.
 */

public class MoreFragment extends Fragment {

    public static final String TAG = "MoreFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.more_fragment, null, false);

        TextView tvProfile = (TextView) rootView.findViewById(R.id.tvProfile);
        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileFragment nextFrag= new ProfileFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frame_content, nextFrag, ProfileFragment.TAG)
                        .addToBackStack(null)
                        .commit();
            }
        });

        TextView tvContactUs = (TextView) rootView.findViewById(R.id.tvContactUs);
        tvContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactUsFragment nextFrag= new ContactUsFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frame_content, nextFrag, ContactUsFragment.TAG)
                        .addToBackStack(null)
                        .commit();
            }
        });

        TextView tvAboutUs = (TextView) rootView.findViewById(R.id.tvAboutUs);
        tvAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutUsFragment nextFrag= new AboutUsFragment();
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_frame_content, nextFrag, AboutUsFragment.TAG)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }
}
