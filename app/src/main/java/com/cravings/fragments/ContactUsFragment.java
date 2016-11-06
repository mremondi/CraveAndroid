package com.cravings.fragments;

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

public class ContactUsFragment extends Fragment{
    public static final String TAG = "ContactUsFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.contact_us_fragment, null, false);
        TextView tvContactUsEmail = (TextView) rootView.findViewById(R.id.tvContactUsEmail);
        tvContactUsEmail.setVisibility(View.VISIBLE);
        return rootView;
    }
}
