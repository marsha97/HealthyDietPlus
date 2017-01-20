package com.project.HDPTeam.hdp.app.fragments;


import android.app.ProgressDialog;

import com.project.HDPTeam.hdp.app.OtherClass.DividerItemDecoration;
import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.project.HDPTeam.hdp.app.OtherClass.EndlessScroll;
import com.project.HDPTeam.hdp.app.OtherClass.titleBar;
import com.project.HDPTeam.hdp.app.OtherClass.url;
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
public class FragmentSearchFood extends Fragment{
    private static final String ARG_PARAM1 = "FOOD_NAME_FragmentSearchFood";
    private static final String ARG_LIST = "FOOD_LIST_FragmentSearchFood";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<String> listName = new ArrayList<>();
    private ArrayList<Long> listId = new ArrayList<>();
    private static final String BUNDLE_RECYCLER_LAYOUT = "FragmentSearchFood.recycler.layout";

    private String foodName;
    private int foodPage;

    private final String mUrl = url.webUrl + "foodSearch.php";

    private ArrayList<foodData> listFoodData = new ArrayList<>();
    private long mId;
    private String foodQuery;
    private ProgressDialog progressDialog;
    private  int totalLoaded;
    private int layoutID;
    private RecyclerView mRecyclerView;
    private foodAdapter mFoodAdapter;
    private EndlessScroll scrollListener;
    private LinearLayoutManager mLinearLayoutManager;

    public FragmentSearchFood() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSearchFood.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSearchFood newInstance(int layoutID, String param2) {
        FragmentSearchFood fragment = new FragmentSearchFood();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, layoutID);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foodPage = 0;
        if (getArguments() != null){
            layoutID = getArguments().getInt(ARG_PARAM1);
        }
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        scrollListener = new EndlessScroll(mLinearLayoutManager){
            @Override
            public void onLoadMore(int page, int totalItem, RecyclerView view) {
               // Toast.makeText(getContext(), "scrollListener called", Toast.LENGTH_SHORT).show();
               // Toast.makeText(HealthyDietPlus.getContext(), "onLoadMore called", Toast.LENGTH_SHORT).show();
               // Toast.makeText(HealthyDietPlus.getContext(), "page: " + String.valueOf(page) + "; totalItem: "+totalItem, Toast.LENGTH_SHORT).show();
               // Toast.makeText(HealthyDietPlus.getContext(), "page : " + page, Toast.LENGTH_SHORT).show();
               // Toast.makeText(HealthyDietPlus.getContext(), "query : " + foodQuery, Toast.LENGTH_SHORT).show();
                foodPage = page;
                totalLoaded = totalItem;
                makeRequest(foodQuery, page);
            }
        };

        /*if (getArguments() != null) {
            query = getArguments().getString(ARG_PARAM1);
        }*/
        setHasOptionsMenu(true);
    }

    public void makeRequest(String query, int offset){
        progressDialog = ProgressDialog.show(getContext(), "Loading Content", "Please Wait...", true, false);
        //Toast.makeText(HealthyDietPlus.getContext(), "makeRequest called", Toast.LENGTH_SHORT).show();

        RequestQueue mRequestQueue = Singleton.getIsntance().getRequestQueue();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getUrl(query,offset), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseJSON(response);
                foodDatas newObj = foodDatas.get(HealthyDietPlus.getContext(),listName,listId);
                listFoodData = newObj.getFoodDatas();
                EndlessScroll.setTotalItemCount(listFoodData.size());
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
            if (mfoods.getString("total_results").equals("0")){
                AlertFragment.createDialog("Food not Found!", "Oops...", getContext());
            }

            JSONArray mfood = mfoods.getJSONArray("food");

            for (int i = 0; i < mfood.length(); i++){
                JSONObject currFood = mfood.getJSONObject(i);
                String mFoodName = currFood.getString("food_name");
                if (currFood.getString("food_type").equals("Brand")){
                    String mBrand = currFood.getString("brand_name");
                    mFoodName += "\n" + "Brand: " + mBrand;
                }
                long mId = currFood.getLong("food_id");
                listName.add(mFoodName);
                listId.add(mId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String getUrl (String query, int offset){
        return mUrl + "?food_name=" + query + "&offset=" + offset;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // Toast.makeText(HealthyDietPlus.getContext(), "onCreateView called", Toast.LENGTH_SHORT).show();
        //Toast.makeText(HealthyDietPlus.getContext(), "mLayoutManager == null : " + String.valueOf(mLinearLayoutManager == null), Toast.LENGTH_SHORT).show();

        View view = inflater.inflate(R.layout.fragment_search_food, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.search_recycleView);
        if (mLinearLayoutManager == null){
            //Toast.makeText(HealthyDietPlus.getContext(), "mLinearLayout is not set", Toast.LENGTH_SHORT).show();
            //kembali dari foodDetail fragment
           // Toast.makeText(HealthyDietPlus.getContext(), "List data makanan : " + String.valueOf(listFoodData.size()), Toast.LENGTH_SHORT).show();

            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            LinearLayoutManager mOtherLinearLayoutManager = new LinearLayoutManager(getActivity());
            EndlessScroll.setTotalItemCount(listFoodData.size());
            if (listFoodData.size() % 50 == 0){
                EndlessScroll.setStartingPageIndex((listFoodData.size()/50) - 1);
            }
            else {
                EndlessScroll.setStartingPageIndex((listFoodData.size()/50));
            }
            updateUI();
            scrollListener = new EndlessScroll(mOtherLinearLayoutManager){
                @Override
                public void onLoadMore(int page, int totalItem, RecyclerView view) {
                   // Toast.makeText(getContext(), "scrollListener called", Toast.LENGTH_SHORT).show();
                   // Toast.makeText(HealthyDietPlus.getContext(), "onLoadMore called", Toast.LENGTH_SHORT).show();
                   // Toast.makeText(HealthyDietPlus.getContext(), "page: " + String.valueOf(page) + "; totalItem: "+totalItem, Toast.LENGTH_SHORT).show();
                   // Toast.makeText(HealthyDietPlus.getContext(), "page : " + page, Toast.LENGTH_SHORT).show();
                   // Toast.makeText(HealthyDietPlus.getContext(), "query : " + foodQuery, Toast.LENGTH_SHORT).show();
                    foodPage = page;
                    totalLoaded = totalItem;
                    makeRequest(foodQuery, page);
                }
            };
        }
        else {
           // Toast.makeText(HealthyDietPlus.getContext(), "mLinearLayout is set", Toast.LENGTH_SHORT).show();
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mLinearLayoutManager = null;
        }
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        mRecyclerView.addOnScrollListener(scrollListener);
        ((titleBar.titleBarOperation) getActivity()).setTitleBar("Food List");
        return view;
    }
    //nampilkan searchbar
    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                //Toast.makeText(getContext(), "onQueryTextSubmit called", Toast.LENGTH_SHORT).show();
                Log.d("search", "QueryTextSubmit: " + query);
                if (listName != null || listName.size() > 0){
                    listName.clear();
                    listId.clear();
                }
                query = query.replaceAll(" ", "%20");
                Log.d("search", query);
                foodQuery = query;
                if (scrollListener != null) {scrollListener.resetState();}
                makeRequest(query,0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("search", "QueryTextChange: " + newText);
                return false;
            }
        });
    }


    private void updateUI(){
        //Toast.makeText(HealthyDietPlus.getContext(), "updateUI called", Toast.LENGTH_SHORT).show();
        mRecyclerView.scrollToPosition(totalLoaded);
        mFoodAdapter = new foodAdapter(listFoodData);
        mRecyclerView.setAdapter(mFoodAdapter);
    }

    private class foodSearchHolder extends RecyclerView.ViewHolder{
        public TextView foodItem;
        public foodSearchHolder(View itemView){
            super (itemView);
            foodItem = (TextView) itemView;
        }
        public void onClickItem (final String foodName, final long id){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ((fragmentOperation) getActivity()).replaceFragment(String.valueOf(id), foodName, layoutID);
                    }catch (ClassCastException cce){
                        Toast.makeText(getContext(), "Class is not implemented", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
    private class foodAdapter extends RecyclerView.Adapter<foodSearchHolder>{
        ArrayList<foodData> listOfFood;
        public foodAdapter (ArrayList<foodData> listData){
            //Toast.makeText(HealthyDietPlus.getContext(), "foodAdapter called", Toast.LENGTH_SHORT).show();
            listOfFood = listData;
        }
        @Override
        public foodSearchHolder onCreateViewHolder (ViewGroup parent, int viewType){
           // Toast.makeText(HealthyDietPlus.getContext(), "onCreateViewHolder called", Toast.LENGTH_SHORT).show();
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            //Toast.makeText(HealthyDietPlus.getContext(), "Scrolled to: " + String.valueOf(totalLoaded), Toast.LENGTH_SHORT).show();

            return new foodSearchHolder(view);
        }

        @Override
        public void onBindViewHolder(foodSearchHolder holder, int position) {
            //Toast.makeText(HealthyDietPlus.getContext(), "bind position: " + position, Toast.LENGTH_SHORT).show();
            EndlessScroll.setLastVisible(position);
            foodData food = listOfFood.get(position);
            mId = food.getId();
            foodName = food.getFoodName();
            holder.foodItem.setText(food.getFoodName());
            holder.onClickItem(foodName,mId);
        }

        @Override
        public int getItemCount() {
            return listOfFood.size();
        }
    }

    public interface fragmentOperation{
        public void replaceFragment(String mId, String title, int layoutID);
    }

}
