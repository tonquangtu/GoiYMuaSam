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
    private int size;
    public SpaceItemRecyclerView(int space, int type, int size){
        this.space = space;
        this.type = type;
        this.size = size;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if(type == VERTICAL){
            if(position == 0){
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

            if(position%2 == 1){
                outRect.left = space;
                outRect.right = 2*space;
                outRect.bottom = space;
                outRect.top = space;
            }else if(position%2==0){
                outRect.left = 2*space;
                outRect.right = space;
                outRect.bottom = space;
                outRect.top = space;
            }

            if(position == 0){
                outRect.top = 2*space;
                outRect.bottom = space;
                outRect.left = 2*space;
                outRect.right = space;
            }else if(position == 1){
                outRect.top = 2*space;
                outRect.bottom = space;
                outRect.left = space;
                outRect.right = 2*space;
            }else if(position == size - 1){
                outRect.top = space;
                outRect.bottom = 2*space;
                outRect.left = space;
                outRect.right = 2*space;
            }else if(position == size - 2){
                outRect.top = space;
                outRect.bottom = 2*space;
                outRect.left = 2*space;
                outRect.right = space;
            }
        }
    }
}
