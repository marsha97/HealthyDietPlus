package com.project.HDPTeam.hdp.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.HDPTeam.hdp.app.R;

public class lastUpdateFragment extends Fragment {
    private View layout;
    private String mWeight, mIdeal, mDate, mIntensity;
    private TextView weight, idealWeight, date, intensity;
    private String mUname;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstance){
        View view = inflater.inflate(R.layout.fragment_last_update,container, false);
        layout = inflater.inflate(R.layout.fragment_last_update, container, false);
        weight = (TextView) view.findViewById(R.id.lastWeightResult_textView);
        weight.setText(mWeight);

        idealWeight = (TextView) view.findViewById(R.id.lastIdealResult_textView);
        idealWeight.setText(mIdeal);

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
        mIdeal = getArguments().getString("idealWeight");
        mDate = getArguments().getString("lastUpdate");
        mIntensity = getArguments().getString("intensity");
    }
}
