package com.project.HDPTeam.hdp.app.Divider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by kali on 1/5/17.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private static int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private Drawable mDivider;
    private int mOrientation;

    public DividerItemDecoration (Context context, int orientation){
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation (orientation);
    }

    public void setOrientation (int orientation){
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST){
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDrawOver (Canvas c, RecyclerView parent, RecyclerView.State state){
        if (mOrientation == VERTICAL_LIST){
            drawVertical (c, parent);
        }
        else{
            drawHorizontal (c, parent);
        }
    }

    public void drawVertical (Canvas canvas, RecyclerView parent){
        int dividerLeft = parent.getPaddingLeft();
        int dividerRight = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0 ; i < childCount - 1; i++){
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int dividerTop = child.getBottom() + params.bottomMargin;
            int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

            mDivider.setBounds(dividerLeft,dividerTop, dividerRight, dividerBottom);
            mDivider.draw(canvas);
        }
    }

    public void drawHorizontal (Canvas canvas, RecyclerView parent){
        int dividerTop = parent.getPaddingTop();
        int dividerBottom = parent.getHeight() - parent.getPaddingBottom();

        int childCount = parent.getChildCount();
        for (int i = 0 ; i < childCount - 1; i++){
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int dividerLeft = child.getRight() + params.rightMargin;
            int dividerRight = dividerLeft + mDivider.getIntrinsicHeight();

            mDivider.setBounds(dividerLeft,dividerTop, dividerRight, dividerBottom);
            mDivider.draw(canvas);
        }
    }

    @Override
    public void getItemOffsets (Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
        if (mOrientation == VERTICAL_LIST){
            outRect.set(0,0,0, mDivider.getIntrinsicHeight());
        }
        else{
            outRect.set(0,0, mDivider.getIntrinsicWidth(),0);
        }
    }
}
