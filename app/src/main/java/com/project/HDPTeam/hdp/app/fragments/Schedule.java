package com.project.HDPTeam.hdp.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.project.HDPTeam.hdp.app.Activities.ManageSchedule;;
import com.project.HDPTeam.hdp.app.OtherClass.titleBar;
import com.project.HDPTeam.hdp.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link Schedule#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Schedule extends Fragment implements ListView.OnItemClickListener {
    private String[] menu = {"Breakfast", "Morning Snack", "Lunch", "Afternoon Snack", "Dinner", "Evening Snack"};

    private static final String MAX_CAL_ARG ="schedule_maxCalories";
    private static final String CURRENT_CAL_ARG = "schedule_currentCalories";
    //view variable
    private TextView maxCaloriesText, currentCaloriesText;
    private ListView mListView;
    private int maxCalories;
    private double currentCalories;

    public Schedule() {

        // Required empty public constructor
    }

    public static Schedule newInstance(int maxCalories) {
        Schedule fragment = new Schedule();
        Bundle bundle = new Bundle();
        bundle.putInt(MAX_CAL_ARG, maxCalories);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO dapet current calories dari preference
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            maxCalories = getArguments().getInt(MAX_CAL_ARG,0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater = getLayoutInflater(savedInstanceState);
        View view = inflater.inflate(R.layout.schedule_fragment, container, false);

        currentCalories = ((ManageSchedule) getActivity()).getCurrCal();

        ((titleBar.titleBarOperation) getActivity()).setTitleBar("Schedule Manager");
        maxCaloriesText = (TextView)view.findViewById(R.id.caloriesTotal_textview);
        currentCaloriesText = (TextView)view.findViewById(R.id.caloriesCount_textview);
        currentCaloriesText.setText(String.valueOf(currentCalories));
        maxCaloriesText.setText(String.valueOf(maxCalories));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,menu);
        mListView = (ListView)view.findViewById(R.id.menuSchedule_listView);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case(0) :((FragmentReplacement) getActivity()).fromSchedule("Breakfast"); break;
            case(1) :((FragmentReplacement) getActivity()).fromSchedule("Morning Snack"); break;
            case(2) :((FragmentReplacement) getActivity()).fromSchedule("Lunch"); break;
            case(3) :((FragmentReplacement) getActivity()).fromSchedule("Afternoon Snack"); break;
            case(4) :((FragmentReplacement) getActivity()).fromSchedule("Dinner"); break;
            case(5) :((FragmentReplacement) getActivity()).fromSchedule("Evening Snack");break;
        }
    }

    public interface FragmentReplacement{
        public void fromSchedule(String menu);
    }
}
