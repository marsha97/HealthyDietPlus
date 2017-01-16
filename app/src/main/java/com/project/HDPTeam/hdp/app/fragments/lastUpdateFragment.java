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
        View view = inflater.inflate(R.layout.fragment_last_update,container, false);
        layout = inflater.inflate(R.layout.fragment_last_update, container, false);
        weight = (TextView) view.findViewById(R.id.lastWeightResult_textView);
        weight.setText(mWeight);

        height = (TextView) view.findViewById(R.id.lastHeightResult_textView);
        height.setText(mHeight);

        date = (TextView)view.findViewById(R.id.lastUpdateResult_textView);
        date.setText(mDate);

        intensity = (TextView)view.findViewById(R.id.lastIntensityResult_textView);
        intensity.setText(mIntensity);

        return view;
    }
    @Override
    public void onCreate (Bundle bundle){
        super.onCreate(bundle);
        mWeight = getArguments().getString("weight");
        mHeight = getArguments().getString("height");
        mDate = getArguments().getString("lastUpdate");
        mIntensity = getArguments().getString("intensity");
    }
}
