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

package io.vit.vitio.Fragments.Courses;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import io.vit.vitio.Fragments.SubjectViewFragment;
import io.vit.vitio.Fragments.TimeTable.TimeTableListInfo;
import io.vit.vitio.HomeActivity;
import io.vit.vitio.Instances.Course;
import io.vit.vitio.Managers.DataHandler;
import io.vit.vitio.Managers.Parsers.ParseTimeTable;
import io.vit.vitio.R;

/**
 * Created by shalini on 28-06-2015.
 */
public class PagerFragment extends Fragment {

    private RecyclerView recyclerView;
    private Typeface typeface;
    private CourseListAdapter adapter;
    private int MODE=0;
    private List<Course> myCourses;
    private ParseTimeTable parseTimeTable;
    private ImageView noClassView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.courses_pager_fragment, container, false);
        init(rootView);
        setInit();
        setData();
        return rootView;
    }


    private void init(ViewGroup rootView) {
        recyclerView=(RecyclerView)rootView.findViewById(R.id.courses_recycler_view);
        typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/Montserrat-Regular.ttf");
        parseTimeTable=new ParseTimeTable(CoursesFragment.allCoursesList,DataHandler.getInstance(getActivity()));
        noClassView= (ImageView) rootView.findViewById(R.id.noclass_image);
    }


    private void setInit() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setData() {
        if(getArguments().containsKey("mode")) {
            MODE = getArguments().getInt("mode");
        }
        myCourses=parseTimeTable.getFilteredCourses(MODE);
        if(myCourses.size()!=0) {
            noClassView.setVisibility(TextView.GONE);
            adapter = new CourseListAdapter(getActivity(), myCourses);
            recyclerView.setAdapter(adapter);
        }
        else{
            noClassView.setVisibility(TextView.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
    }

    private class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.CourseViewHolder> {
        private final List<Course> data;
        private LayoutInflater inflater;
        private Context c;

        public CourseListAdapter(Context context, List<Course> list) {
            data = list;
            c = context;
            inflater = LayoutInflater.from(c);
        }

        @Override
        public CourseListAdapter.CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.courses_pager_recycler_row, parent, false);
            CourseViewHolder courseViewHolder = new CourseViewHolder(view);
            return courseViewHolder;
        }


        @Override
        public void onBindViewHolder(CourseViewHolder holder, int position) {
            Course info = data.get(position);
            holder.subName.setText(info.getCOURSE_TITLE());
            holder.subCode.setText(info.getCOURSE_CODE());
        }

        @Override
        public int getItemCount() {

            return data.size();
        }

        class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView subName, subCode;
            LinearLayout layout;

            public CourseViewHolder(View itemView) {
                super(itemView);
                subName = (TextView) itemView.findViewById(R.id.subject_name);
                subCode = (TextView) itemView.findViewById(R.id.subject_code);
                subName.setTypeface(typeface);
                subCode.setTypeface(typeface);
                layout = (LinearLayout) itemView.findViewById(R.id.row_holder);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment subject = new SubjectViewFragment();
                Bundle arguments = new Bundle();
                arguments.putString("class_number", String.valueOf(data.get(getAdapterPosition()).getCLASS_NUMBER()));
                subject.setArguments(arguments);
                ft.replace(R.id.main_fragment_holder, subject);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.addToBackStack(null);
                ft.commit();

            }

        }
    }
}
