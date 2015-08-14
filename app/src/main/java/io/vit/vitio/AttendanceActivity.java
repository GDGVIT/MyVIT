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

package io.vit.vitio;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.vit.vitio.Extras.TypeFaceSpan;
import io.vit.vitio.Fragments.SubjectViewFragment;
import io.vit.vitio.Instances.Attendance;
import io.vit.vitio.Instances.Course;
import io.vit.vitio.Instances.Day;
import io.vit.vitio.Managers.DataHandler;

/**
 * Created by shalini on 28-06-2015.
 */
public class AttendanceActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private TextView dayHead, dateHead, statusHead;
    private Typeface typeface;
    private AttendanceListAdapter adapter;
    private DataHandler dataHandler;
    private Course myCourse;
    private List<Attendance.VClass> attendanceList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_view_attendance);
        init();
        setInit();
        setToolbar();
        setData();
    }


    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.attendance_recycler_view);
        typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/Montserrat-Regular.ttf");
        dataHandler = DataHandler.getInstance(this);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        dayHead = (TextView) findViewById(R.id.day_header);
        dateHead = (TextView) findViewById(R.id.date_header);
        statusHead = (TextView) findViewById(R.id.status_header);
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        SpannableString s = new SpannableString("Attendance");
        s.setSpan(new TypeFaceSpan(this, "Montserrat-Regular.ttf"), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.darkgray)));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        changeStatusBarColor(getResources().getColor(R.color.darkergray));
    }

    private void setInit() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/Montserrat-Regular.ttf");
        dayHead.setTypeface(typeface);
        dateHead.setTypeface(typeface);
        statusHead.setTypeface(typeface);
    }

    private void setData() {
        if (getIntent().hasExtra("class_number")) {
            myCourse = dataHandler.getCourse(getIntent().getStringExtra("class_number"));
        }
        if (myCourse != null) {
            adapter = new AttendanceListAdapter(this, myCourse.getCOURSE_ATTENDANCE().getClasses());
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No attendance yet", Toast.LENGTH_SHORT).show();
        }
    }

    private class AttendanceListAdapter extends RecyclerView.Adapter<AttendanceListAdapter.AttendanceViewHolder> {
        private final List<Attendance.VClass> data;
        private LayoutInflater inflater;
        private Context c;

        public AttendanceListAdapter(Context context, List<Attendance.VClass> list) {
            data = list;
            c = context;
            inflater = LayoutInflater.from(c);
        }

        @Override
        public AttendanceListAdapter.AttendanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.attendance_recycler_row, parent, false);
            AttendanceViewHolder attendanceViewHolder = new AttendanceViewHolder(view);
            return attendanceViewHolder;
        }


        @Override
        public void onBindViewHolder(AttendanceViewHolder holder, int position) {
            Attendance.VClass mClass = data.get(position);
            holder.date.setText(mClass.getFormattedDate());
            Day day = new Day(mClass.getDay());
            holder.day.setText(day.getDayName());
            if (mClass.getSTATUS().equalsIgnoreCase("absent"))
                holder.status.setTextColor(getResources().getColor(R.color.fadered));
            else
                holder.status.setTextColor(getResources().getColor(R.color.fadegreen));
            holder.status.setText(mClass.getSTATUS());
        }

        @Override
        public int getItemCount() {

            return data.size();
        }

        class AttendanceViewHolder extends RecyclerView.ViewHolder {

            TextView date, day, status;
            LinearLayout layout;

            public AttendanceViewHolder(View itemView) {
                super(itemView);
                date = (TextView) itemView.findViewById(R.id.date_col);
                day = (TextView) itemView.findViewById(R.id.day_col);
                status = (TextView) itemView.findViewById(R.id.status_col);
                date.setTypeface(typeface);
                day.setTypeface(typeface);
                status.setTypeface(typeface);
                layout = (LinearLayout) itemView.findViewById(R.id.row_holder);
            }

        }
    }
    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(color);
        }
    }
}
