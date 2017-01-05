package com.project.HDPTeam.hdp.app.fragments;


import android.app.ProgressDialog;
import com.project.HDPTeam.hdp.app.Divider.DividerItemDecoration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.project.HDPTeam.hdp.app.Activities.HealthyDietPlus;
import com.project.HDPTeam.hdp.app.R;
import com.project.HDPTeam.hdp.app.model.foodDatas;
import com.project.HDPTeam.hdp.app.networks.CheckConnection;
import com.project.HDPTeam.hdp.app.networks.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.project.HDPTeam.hdp.app.model.foodData;


/**
 * A simple {@link //Fragment} subclass.
 * Use the {@link FragmentSearchFood#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSearchFood extends Fragment {
    private static final String ARG_PARAM1 = "FOOD_NAME_FragmentSearchFood";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<String> listName = new ArrayList<>();
    private ArrayList<Long> listId = new ArrayList<>();

    private String foodName;
    private String mParam2;

    private final String url = "http://192.168.0.111:80/hdplusdb/foodSearch.php";

    private ArrayList<foodData> listFoodData = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private foodAdapter mFoodAdapter;

    public FragmentSearchFood() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSearchFood.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSearchFood newInstance(String param1, String param2) {
        FragmentSearchFood fragment = new FragmentSearchFood();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            foodName = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        makeRequest();
    }

    public void makeRequest(){
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Loading Content", "Please Wait...", true, false);
        RequestQueue mRequestQueue = Singleton.getIsntance().getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUrl(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseJSON(response);
                foodDatas newObj = foodDatas.get(HealthyDietPlus.getContext(),listName,listId);
                listFoodData = newObj.getFoodDatas();
                updateUI();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                new CheckConnection().createInternetAccessDialog(getContext());
            }
        });
        mRequestQueue.add(request);
        mRequestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<JSONObject>() {

            @Override
            public void onRequestFinished(Request<JSONObject> request){
                progressDialog.dismiss();
            }
        });
    }

    public void parseJSON (JSONObject response){
        if (response == null || response.length() == 0){
            return;
        }
        try {
            JSONObject mfoods = response.getJSONObject("foods");
            JSONArray mfood = mfoods.getJSONArray("food");

            for (int i = 0; i < mfood.length(); i++){
                JSONObject currFood = mfood.getJSONObject(i);
               String mFoodName = currFood.getString("food_name");
               long mId = currFood.getLong("food_id");
                listName.add(mFoodName);
                listId.add(mId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getUrl (){
        return url + "?food_name=" + foodName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_food, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.search_recycleView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        return view;
    }

    private void updateUI(){
        mFoodAdapter = new foodAdapter(listFoodData);
       mRecyclerView.setAdapter(mFoodAdapter);
    }

    private class foodSearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView foodItem;
        public foodSearchHolder(View itemView){
            super (itemView);
            itemView.setOnClickListener(this);
            foodItem = (TextView) itemView;
        }

        @Override
        public void onClick(View view){
            Toast.makeText(getContext(), foodItem.getText().toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private class foodAdapter extends RecyclerView.Adapter<foodSearchHolder>{
        ArrayList<foodData> listOfFood;
        public foodAdapter (ArrayList<foodData> listData){
            listOfFood = listData;
        }

        @Override
        public foodSearchHolder onCreateViewHolder (ViewGroup parent, int viewType){
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new foodSearchHolder(view);
        }

        @Override
        public void onBindViewHolder(foodSearchHolder holder, int position) {
            foodData food = listOfFood.get(position);
            holder.foodItem.setText(food.getFoodName());
        }

        @Override
        public int getItemCount() {
            return listOfFood.size();
        }
    }

}
