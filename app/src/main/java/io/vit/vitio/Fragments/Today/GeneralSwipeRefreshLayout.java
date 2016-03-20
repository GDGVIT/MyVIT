/*
 * Copyright (c) 2015 GDG VIT Vellore.
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.vit.vitio.Fragments.Today;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by shalini on 23-08-2015.
 */
public class GeneralSwipeRefreshLayout extends SwipeRefreshLayout {
    private RecyclerView recyclerView;
    private boolean canScroll=false;

    public GeneralSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setRecyclerView(RecyclerView view){
        recyclerView=view;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("rv2", String.valueOf(view.getChildAt(0).getTop()));
                if(view.getChildCount()>0) {
                    if (view.getChildAt(0).getTop()==0){
                        canScroll=false;
                    }
                    else
                        canScroll=true;
                }
                else
                    canScroll=true;
            }
        });
    }
    @Override
    public boolean canChildScrollUp() {
        if(recyclerView!=null){
            //Log.d("rv", String.valueOf(recyclerView.getScrollY()));
           return canScroll;
        }
        return false;
    }
}
