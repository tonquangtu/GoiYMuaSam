package com.mycompany.mainui;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Mr.T on 4/14/2016.
 */
public class SpaceItemRecyclerView extends RecyclerView.ItemDecoration {
    public static final int VERTICAL = 1;
    public static final int HORIZONTAL = 2;
    public static final int GRID = 3;
    private int space;
    private int type;
    public SpaceItemRecyclerView(int space, int type){
        this.space = space;
        this.type = type;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(type == VERTICAL){
            if(parent.getChildAdapterPosition(view) == 0){
                outRect.top = 2*space;
                outRect.right = 2*space;
                outRect.left = 2*space;
                outRect.bottom = space;
            }
            else{
                outRect.top = space;
                outRect.right = 2*space;
                outRect.left = 2*space;
                outRect.bottom = space;
            }

        }
        else if(type == HORIZONTAL){
            outRect.right = space;
        }else{
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            outRect.top = space;
        }
    }
}
