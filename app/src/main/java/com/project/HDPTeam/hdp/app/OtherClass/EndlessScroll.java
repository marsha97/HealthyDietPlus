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
    private static int currentPage = 0;
    //jumlah data terakhir yang ke load
    private int previousDataLoad = 0;
    //masih loading (belum selesai sampai datang terakhir)
    private boolean isLoading = true;
    //halaman awal index
    private static int startingPageIndex = 0;
    private static int totalItemCount = 0;
    private static int lastVisibleItemPosition = 0;
    RecyclerView.LayoutManager mLayoutManager;

    public EndlessScroll(LinearLayoutManager layoutManager){
        //Toast.makeText(HealthyDietPlus.getContext(), "constructor called",Toast.LENGTH_SHORT).show();

        this.mLayoutManager = layoutManager;
    }
    /*public EndlessScroll(GridLayoutManager gridLayoutManager){
        this.mLayoutManager = gridLayoutManager;
        visibleBelow = visibleBelow * gridLayoutManager.getSpanCount();
    }

    private EndlessScroll(StaggeredGridLayoutManager staggeredGridLayoutManager){
        this.mLayoutManager = staggeredGridLayoutManager;
        visibleBelow = visibleBelow * staggeredGridLayoutManager.getSpanCount();

    }    */

    /*private int getLastVisibleItem (int[] lastVisiblePosition){
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
    }*/

    @Override
    public void onScrolled (RecyclerView view, int dx, int dy){
        //jaga" kalau nanti butuh
        //int lastVisibleItemPosition = 0;
        /*if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
            // get maximum element within the list
            lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions);
        } else if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }*/

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
       // Toast.makeText(HealthyDietPlus.getContext(), "last visible" + String.valueOf(lastVisibleItemPosition), Toast.LENGTH_SHORT).show();
       //
       // Toast.makeText(HealthyDietPlus.getContext(), "curr page : " + String.valueOf(currentPage), Toast.LENGTH_SHORT).show();
       // Toast.makeText(HealthyDietPlus.getContext(), "total item" + String.valueOf(totalItemCount), Toast.LENGTH_SHORT).show();
       // Toast.makeText(HealthyDietPlus.getContext(), "sum = " + String.valueOf(lastVisibleItemPosition + visibleBelow), Toast.LENGTH_SHORT).show();
       // Toast.makeText(HealthyDietPlus.getContext(), "isLoading = " + String.valueOf(isLoading), Toast.LENGTH_SHORT).show();
        if (!isLoading && (lastVisibleItemPosition + visibleBelow) > totalItemCount){

            currentPage++;
            onLoadMore(currentPage, totalItemCount, view);
            isLoading = true;
        }

    }

    public void resetState(){
        //Toast.makeText(HealthyDietPlus.getContext(), "resetState called", Toast.LENGTH_SHORT).show();
        this.currentPage = startingPageIndex;
        this.previousDataLoad = 0;
        this.isLoading = true;
    }

    /*public static void setIsLoading(boolean seter){
        isLoading = seter;
    }*/

    public static void setTotalItemCount (int count){
        totalItemCount = count;
    }

    public static void setStartingPageIndex (int page){
        startingPageIndex = page;
    }

    public static void setLastVisible (int last){
        lastVisibleItemPosition = last;
    }

    public abstract void onLoadMore (int page, int totalItem, RecyclerView view);
}
