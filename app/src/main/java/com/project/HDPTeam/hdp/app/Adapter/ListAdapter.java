package com.project.HDPTeam.hdp.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.HDPTeam.hdp.app.R;

import java.util.ArrayList;


/**
 * Created by kali on 1/9/17.
 */

public class ListAdapter extends BaseAdapter {
    ArrayList<String> menu;
    int imageID;
    Context mContext;
    LayoutInflater mInflater;

    public ListAdapter (Context context, ArrayList<String> menu, int imageID){
        this.menu = menu;
        this.imageID = imageID;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return menu.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.list_item, null);
        TextView itemMenu = (TextView)convertView.findViewById(R.id.textList);
        ImageView imageMenu = (ImageView)convertView.findViewById(R.id.imageList);
        itemMenu.setText(menu.get(position));
        imageMenu.setImageResource(imageID);

        return convertView;
    }
}
