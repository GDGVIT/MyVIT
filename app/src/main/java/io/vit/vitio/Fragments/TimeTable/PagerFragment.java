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

package io.vit.vitio.Fragments.TimeTable;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import io.vit.vitio.Extras.Themes.MyTheme;
import io.vit.vitio.Fragments.SubjectView.SubjectViewFragmentTrial;
import io.vit.vitio.R;

/**
 * Created by shalini on 28-06-2015.
 */
public class PagerFragment extends Fragment {

    private RecyclerView recyclerView;
    private Typeface typeface;
    private MyTheme myTheme;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TimeTableListAdapter adapter;
    private int MODE=0;
    private List<TimeTableListInfo> myCourses;
    private TextView noClassView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.timetable_pager_fragment, container, false);
        init(rootView);
        setInit();
        setData();
        return rootView;
    }


    private void init(ViewGroup rootView) {
        recyclerView=(RecyclerView)rootView.findViewById(R.id.timetable_recycler_view);
        myTheme=new MyTheme(getActivity());
        typeface = myTheme.getMyThemeTypeface();
        noClassView= (TextView) rootView.findViewById(R.id.no_classes_view);
        swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh_layout);
    }


    private void setInit() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeRefreshLayout.setRefreshing(false);//this should be false for roatation
                    }
                }, 5000);


                swipeRefreshLayout.setEnabled(true);
                ((TimeTableFragment)getParentFragment()).setTimeTableFromAPI();
            }


        });
    }

    private void setData() {
        if(getArguments().containsKey("mode")) {
            MODE = getArguments().getInt("mode");
        }
        myCourses=TimeTableFragment.TIME_TABLE_LIST.get(MODE);
        if(myCourses.size()!=0) {
            noClassView.setVisibility(TextView.GONE);
            adapter = new TimeTableListAdapter(getActivity(), myCourses);
            recyclerView.setAdapter(adapter);
        }
        else{
            noClassView.setVisibility(TextView.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        myTheme.refreshTheme();
        typeface=myTheme.getMyThemeTypeface();
        setData();
    }



    private class TimeTableListAdapter extends RecyclerView.Adapter<TimeTableListAdapter.TimeTableViewHolder> {
        private final List<TimeTableListInfo> data;
        private LayoutInflater inflater;
        private Context c;

        public TimeTableListAdapter(Context context, List<TimeTableListInfo> list) {
            data = list;
            c = context;
            inflater = LayoutInflater.from(c);
        }

        @Override
        public TimeTableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.timetable_list_recycler_row, parent, false);
            TimeTableViewHolder timetableViewHolder = new TimeTableViewHolder(view);
            return timetableViewHolder;
        }


        @Override
        public void onBindViewHolder(TimeTableViewHolder holder, int position) {
            TimeTableListInfo info = data.get(position);
            holder.subName.setText(info.name);
            holder.subTime.setText(info.time12);
            holder.subVenue.setText(info.venue);
            int p = Integer.parseInt((info.per.split(" ")[0]));
            if (p < 75)
                holder.subPer.setTextColor(c.getResources().getColor(R.color.fadered));
            else if (p == 75)
                holder.subPer.setTextColor(c.getResources().getColor(R.color.fadeyellow));
            holder.subPer.setText(info.per);
            holder.subTypeShort.setText(info.typeShort);
            Log.d("typet", info.typeShort);
            animateList(holder);
        }

        private void animateList(TimeTableViewHolder holder) {
            ObjectAnimator animator=ObjectAnimator.ofFloat(holder.layout,"translationY",600,0);
            animator.setDuration(500);
            animator.setInterpolator(new AccelerateInterpolator());
            animator.start();
        }

        @Override
        public int getItemCount() {

            return data.size();
        }

        class TimeTableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView subName, subTime, subPer, subVenue, subTypeShort;
            FrameLayout layout;
            LinearLayout innerLayout;

            public TimeTableViewHolder(View itemView) {
                super(itemView);
                subName = (TextView) itemView.findViewById(R.id.subject_name);
                subTime = (TextView) itemView.findViewById(R.id.subject_time);
                subPer = (TextView) itemView.findViewById(R.id.subject_per);
                subVenue = (TextView) itemView.findViewById(R.id.subject_venue);
                subTypeShort = (TextView) itemView.findViewById(R.id.subject_type_short);
                subName.setTypeface(typeface);
                subTime.setTypeface(typeface);
                subPer.setTypeface(typeface);
                subVenue.setTypeface(typeface);
                subTypeShort.setTypeface(typeface);
                layout = (FrameLayout) itemView.findViewById(R.id.row_holder);
                innerLayout = (LinearLayout) itemView.findViewById(R.id.inner_row_holder);
                innerLayout.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Log.d("click","click");
                FragmentTransaction ft =getActivity().getSupportFragmentManager().beginTransaction();
                Fragment subject = new SubjectViewFragmentTrial();
                Bundle arguments=new Bundle();
                arguments.putString("class_number", String.valueOf(data.get(getAdapterPosition()).clsnbr));
                subject.setArguments(arguments);
                ft.replace(R.id.main_fragment_holder, subject);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();
            }
        }
    }
}
