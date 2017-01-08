package com.project.HDPTeam.hdp.app.OtherClass;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.Toast;

import com.project.HDPTeam.hdp.app.Activities.HealthyDietPlus;

/**
 * Created by kali on 1/8/17.
 */

public abstract class EndlessScroll extends RecyclerView.OnScrollListener {
    //jumlah minimum dibawah item yang ada
    private int visibleBelow = 5;
    //data offset yang SUDAH keload
    private int currentPage = 0;
    //jumlah data terakhir yang ke load
    private int previousDataLoad = 0;
    //masih loading (belum selesai sampai datang terakhir)
    private boolean isLoading = true;
    //halaman awal index
    private int startingPageIndex = 0;
    RecyclerView.LayoutManager mLayoutManager;

    public EndlessScroll(GridLayoutManager gridLayoutManager){
        this.mLayoutManager = gridLayoutManager;
        visibleBelow = visibleBelow * gridLayoutManager.getSpanCount();
    }
    public EndlessScroll(LinearLayoutManager layoutManager){
        Toast.makeText(HealthyDietPlus.getContext(), "constructor called",Toast.LENGTH_SHORT).show();

        this.mLayoutManager = layoutManager;
    }

    private EndlessScroll(StaggeredGridLayoutManager staggeredGridLayoutManager){
        this.mLayoutManager = staggeredGridLayoutManager;
        visibleBelow = visibleBelow * staggeredGridLayoutManager.getSpanCount();

    }

    private int getLastVisibleItem (int[] lastVisiblePosition){
        int maxSize = 0;
        for (int i = 0; i < lastVisiblePosition.length; i++){
            if (i == 0){
                maxSize = lastVisiblePosition[i];
            }
            else if (lastVisiblePosition[i] > maxSize){
                maxSize = lastVisiblePosition[i];
            }
        }
        return maxSize;
    }

    @Override
    public void onScrolled (RecyclerView view, int dx, int dy){
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();
        if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }

        if (totalItemCount < previousDataLoad){
            this.currentPage = this.startingPageIndex;
            this.previousDataLoad = totalItemCount;
            if (totalItemCount == 0){
                isLoading = true;
            }
        }

        //Toast.makeText(HealthyDietPlus.getContext(), "isLoading: " + String.valueOf(isLoading), Toast.LENGTH_SHORT).show();


        if (isLoading && (totalItemCount > previousDataLoad)){
            isLoading = false; //stop loading
            previousDataLoad = totalItemCount;
        }

        if (!isLoading && (lastVisibleItemPosition + visibleBelow) > totalItemCount){
            currentPage++;
            onLoadMore(currentPage, totalItemCount, view);
            isLoading = true;
        }

    }

    public void resetState(){
        Toast.makeText(HealthyDietPlus.getContext(), "resetState called", Toast.LENGTH_SHORT).show();
        this.currentPage = startingPageIndex;
        this.previousDataLoad = 0;
        this.isLoading = true;
    }

    public boolean getIsLoading(){
        return isLoading;
    }

    public abstract void onLoadMore (int page, int totalItem, RecyclerView view);
}
