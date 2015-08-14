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

package io.vit.vitio.Navigation;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Collections;
import java.util.List;

import io.vit.vitio.R;

/**
 * Created by shalini on 23-02-2015.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.NavigationViewHolder> {
    private LayoutInflater inflater;
    private List<NavigationDrawerInfo> recyclerList = Collections.emptyList();
    private Context c;
    private ClickListener clickListener;
    public NavigationDrawerAdapter(Context context,List<NavigationDrawerInfo> list)
    {
        c=context;
        inflater= LayoutInflater.from(context);
        recyclerList=list;
    }
    @Override
    public NavigationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.navigation_recycler_row,parent,false);
        NavigationViewHolder navigationViewHolder=new NavigationViewHolder(view);
        return navigationViewHolder;
    }

    @Override
    public void onBindViewHolder(NavigationViewHolder holder, int position) {
        NavigationDrawerInfo info=recyclerList.get(position);
        holder.title.setText(info.name);
        holder.imageView.setImageResource(info.iconId);
    }

    @Override
    public int getItemCount() {

        return recyclerList.size();
    }

    public void setClickListener(ClickListener cl)
    {
        this.clickListener=cl;
    }
    class NavigationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        ImageView imageView;
        RelativeLayout rowframe;
        public NavigationViewHolder(View itemView)
        {
            super(itemView);
            Typeface tf=Typeface.createFromAsset(c.getAssets(),"fonts/Montserrat-Regular.ttf");
            title=(TextView)itemView.findViewById(R.id.navigation_row_text);
            imageView=(ImageView)itemView.findViewById(R.id.navigation_row_image);
            rowframe=(RelativeLayout)itemView.findViewById(R.id.row_holder);
            title.setTypeface(tf);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(clickListener!=null){
                clickListener.onRecyclerItemClick(v,getAdapterPosition());
            }
        }
    }
    public interface ClickListener{
        void onRecyclerItemClick(View v, int position);
    }
}
