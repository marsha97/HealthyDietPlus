package com.project.HDPTeam.hdp.app.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.project.HDPTeam.hdp.app.R;

import java.util.zip.Inflater;

public class lastUpdateFragment extends Fragment {
    private View layout;
    private String mWeight, mHeight, mDate, mIntensity;
    private TextView weight, height, date, intensity;
    private String mUname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstance){
        layout = inflater.inflate(R.layout.fragment_last_update, container, false);
        return layout;
    }
    @Override
    public void onActivityCreated (Bundle bundle){
        super.onActivityCreated(bundle);

        weight = (TextView) getActivity().findViewById(R.id.lastWeightResult_textView);
        mWeight = getArguments().getString("weight");
        weight.setText(mWeight);

        height = (TextView) getActivity().findViewById(R.id.lastHeightResult_textView);
        mHeight = getArguments().getString("height");
        height.setText(mHeight);

        date = (TextView) getActivity().findViewById(R.id.lastUpdateResult_textView);
        mDate = getArguments().getString("lastUpdate");
        date.setText(mDate);

        intensity = (TextView) getActivity().findViewById(R.id.lastIntensityResult_textView);
        mIntensity = getArguments().getString("intensity");
        intensity.setText(mIntensity);

    }
}
