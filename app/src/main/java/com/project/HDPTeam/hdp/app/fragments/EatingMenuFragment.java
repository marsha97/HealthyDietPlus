package com.project.HDPTeam.hdp.app.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import  java.util.Calendar;

import com.project.HDPTeam.hdp.app.Activities.HealthyDietPlus;
import com.project.HDPTeam.hdp.app.Activities.ManageSchedule;
import com.project.HDPTeam.hdp.app.Adapter.ListAdapter;
import com.project.HDPTeam.hdp.app.OtherClass.titleBar;
import com.project.HDPTeam.hdp.app.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EatingMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EatingMenuFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MANAGE_SCHEDULE_CHOSEN_MENU = "ManageSchedule_chosenMenu";
    private static final String MANAGE_SCHEDULE_MENU_SIZE = "ManageSchedule_menuSize";

    private String MenuTime;
    private int mHour, mMinute;
    private int menuSize;

    private Button timeButton;
    private Button foodListButton;
    private Button saveButton;

    private ArrayList<String> foodMenu, caloriesList;

    public EatingMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param choosenTime Parameter 1.
     * @return A new instance of fragment EatingMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EatingMenuFragment newInstance(String choosenTime) {
        EatingMenuFragment fragment = new EatingMenuFragment();
        Bundle args = new Bundle();
        args.putString(MANAGE_SCHEDULE_CHOSEN_MENU, choosenTime);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Toast.makeText(getContext(), "onCreate", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            MenuTime = getArguments().getString(MANAGE_SCHEDULE_CHOSEN_MENU);
        }
    }

    @Override
    public void onSaveInstanceState (Bundle outState){
        Toast.makeText(getContext(), "onSaveInstanceState", Toast.LENGTH_SHORT).show();

        outState.putString(MANAGE_SCHEDULE_CHOSEN_MENU, MenuTime);
        outState.putInt(MANAGE_SCHEDULE_MENU_SIZE, menuSize);

        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Toast.makeText(getContext(), "onCreateView "+ MenuTime, Toast.LENGTH_SHORT).show();

        // Inflate the layout for this fragment
        menuSize = ((ManageSchedule) getActivity()).getSize();
        View view = inflater.inflate(R.layout.fragment_eating_menu, container, false);
        String time = ((ManageSchedule) getActivity()).getTime();

        ((titleBar.titleBarOperation) getActivity()).setTitleBar(MenuTime);
        timeButton = (Button) view.findViewById(R.id.time_button);
        timeButton.setText(time);
        timeButton.setOnClickListener(this);
        foodListButton = (Button) view.findViewById(R.id.chooseFoods_button);
        foodListButton.setOnClickListener(this);
        if (menuSize != 0) {
            foodMenu = new ArrayList<>();
            caloriesList = new ArrayList<>();
            foodMenu = ((ManageSchedule) getActivity()).getFoodNames();
            caloriesList = ((ManageSchedule) getActivity()).getCalories();
            ListView listView = (ListView) view.findViewById(R.id.menuSchedule_listView);
            final ListAdapter mAdapter = new ListAdapter(HealthyDietPlus.getContext(), foodMenu, caloriesList,R.drawable.ic_action_delete);
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String removedCalories = caloriesList.get(position);
                    foodMenu.remove(position);
                    caloriesList.remove(position);
                    ((ManageSchedule) getActivity()).updateCalories(caloriesList, Double.parseDouble(removedCalories),position);
                    ((ManageSchedule) getActivity()).updateMenu(foodMenu,position);
                    ((ManageSchedule) getActivity()).decreaseSize();
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == timeButton){
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);
            //time picker dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String stringHour , stringMin;
                    ((ManageSchedule) getActivity()).addTime(hourOfDay+" : "+minute, hourOfDay, minute);
                    if (hourOfDay < 10)
                        stringHour = "0"+String.valueOf(hourOfDay);
                    else
                        stringHour = String.valueOf(hourOfDay);
                    if (minute < 10)
                        stringMin = "0"+String.valueOf(minute);
                    else
                        stringMin = String.valueOf(minute);
                    timeButton.setText(stringHour + " : " + stringMin);
                    ((ManageSchedule) getActivity()).makeAlarm(hourOfDay, minute);
                }
            }, mHour, mMinute, false);
            timePickerDialog.setTitle("Eating Time");
            timePickerDialog.show();
        }
        else if (v == foodListButton){
            ((FragmentReplaceMent) getActivity()).FromEatingMenu("test");
        }

    }

    public interface FragmentReplaceMent{
        public void FromEatingMenu(String menu);
    }
}
