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

package io.vit.vitio.Fragments.SubjectView;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import io.vit.vitio.Extras.Themes.MyTheme;
import io.vit.vitio.Instances.Course;
import io.vit.vitio.Instances.Timing;
import io.vit.vitio.R;

/**
 * Created by shalini on 25-08-2015.
 */
public class TimingAdapter extends RecyclerView.Adapter<TimingAdapter.TimingHolder> {
    private final List<Timing> data;
    private LayoutInflater inflater;
    private Context c;
    private Typeface typeface;
    private MyTheme myTheme;

    public TimingAdapter(Context context, List<Timing> list) {
        data = list;
        c = context;
        inflater = LayoutInflater.from(c);
        myTheme=new MyTheme(c);
        typeface=myTheme.getMyThemeTypeface();
    }

    @Override
    public TimingAdapter.TimingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.subject_view_fragment_timing_recycler_row, parent, false);
        TimingHolder timingViewHolder = new TimingHolder(view);
        return timingViewHolder;
    }


    @Override
    public void onBindViewHolder(TimingHolder holder, int position) {
        Timing info = data.get(position);
        holder.day.setText(info.getDAY().getDayName());
        holder.time.setText(info.toString(Timing.FORMAT12));
        if(position==data.size()-1){
            holder.horLine.setVisibility(LinearLayout.GONE);
        }
    }

    @Override
    public int getItemCount() {
        Log.d("rv", String.valueOf(data.size()));
        return data.size();
    }

    class TimingHolder extends RecyclerView.ViewHolder {

        TextView day, time;
        LinearLayout layout,horLine;

        public TimingHolder(View itemView) {
            super(itemView);
            day = (TextView) itemView.findViewById(R.id.timings_day);
            time = (TextView) itemView.findViewById(R.id.timings_time);
            myTheme.refreshTheme();
            typeface=myTheme.getMyThemeTypeface();
            day.setTypeface(typeface);
            time.setTypeface(typeface);
            layout = (LinearLayout) itemView.findViewById(R.id.row_holder);
            horLine = (LinearLayout) itemView.findViewById(R.id.hor_line);
        }


    }
}