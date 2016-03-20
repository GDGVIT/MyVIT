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

import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import io.vit.vitio.Extras.Themes.MyTheme;
import io.vit.vitio.Extras.TypeFaceSpan;
import io.vit.vitio.Instances.Course;
import io.vit.vitio.Instances.Mark;
import io.vit.vitio.Managers.DataHandler;
import io.vit.vitio.R;

/**
 * Created by shalini on 29-06-2015.
 */
public class MarksActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    //Declare Headers
    private TextView quiz1Head, quiz2Head, quiz3Head, cat1Head, cat2Head, assignmentHead, internalsHead, cgpaHead, qouteHead, qouteWriter, errorMessage;

    //Declare Contents
    private TextView quiz1Marks, quiz2Marks, quiz3Marks, cat1Marks, cat2Marks, assignmentMarks, internalsMarks, cgpaContent;

    private LinearLayout cat1Indicator, cat2Indicator, internalsIndicator, layoutContainer;

    private Typeface typeface;

    private MyTheme myTheme;

    private Course myCourse;

    private DataHandler dataHandler;

    private Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_view_marks);
        init();
        setToolbar();
        setTypfaces();
        setData();
        closeButton.setOnClickListener(this);
    }


    private void setTypfaces() {
        typeface = myTheme.getMyThemeTypeface();
        quiz1Head.setTypeface(typeface);
        quiz2Head.setTypeface(typeface);
        quiz3Head.setTypeface(typeface);
        cat1Head.setTypeface(typeface);
        cat2Head.setTypeface(typeface);
        assignmentHead.setTypeface(typeface);
        internalsHead.setTypeface(typeface);
        //cgpaHead.setTypeface(typeface);
        qouteHead.setTypeface(typeface);
        qouteWriter.setTypeface(typeface);

        quiz1Marks.setTypeface(typeface);
        quiz2Marks.setTypeface(typeface);
        quiz3Marks.setTypeface(typeface);
        cat1Marks.setTypeface(typeface);
        cat2Marks.setTypeface(typeface);
        assignmentMarks.setTypeface(typeface);
        internalsMarks.setTypeface(typeface);
        //cgpaContent.setTypeface(typeface);

    }


    private void init() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);

        quiz1Head = (TextView) findViewById(R.id.quiz1_head);
        quiz2Head = (TextView) findViewById(R.id.quiz2_head);
        quiz3Head = (TextView) findViewById(R.id.quiz3_head);
        cat1Head = (TextView) findViewById(R.id.cat1_head);
        cat2Head = (TextView) findViewById(R.id.cat2_head);
        assignmentHead = (TextView) findViewById(R.id.assignment_head);
        internalsHead = (TextView) findViewById(R.id.internals_head);
        //cgpaHead = (TextView) findViewById(R.id.cgpa_head);
        qouteHead = (TextView) findViewById(R.id.qoute_head);
        qouteWriter = (TextView) findViewById(R.id.qoute_writer);

        quiz1Marks = (TextView) findViewById(R.id.quiz1_marks);
        quiz2Marks = (TextView) findViewById(R.id.quiz2_marks);
        quiz3Marks = (TextView) findViewById(R.id.quiz3_marks);
        cat1Marks = (TextView) findViewById(R.id.cat1_marks);
        cat2Marks = (TextView) findViewById(R.id.cat2_marks);
        assignmentMarks = (TextView) findViewById(R.id.assignment_marks);
        internalsMarks = (TextView) findViewById(R.id.internals_marks);
        //cgpaContent = (TextView) findViewById(R.id.cgpa_content);
        errorMessage = (TextView) findViewById(R.id.error_message);

        closeButton = (Button) findViewById(R.id.close_button);

        cat1Indicator = (LinearLayout) findViewById(R.id.cat1_indicator);
        cat2Indicator = (LinearLayout) findViewById(R.id.cat2_indicator);
        internalsIndicator = (LinearLayout) findViewById(R.id.internals_indicator);
        layoutContainer = (LinearLayout) findViewById(R.id.layout_container);

        dataHandler = DataHandler.getInstance(this);

        myTheme=new MyTheme(this);

    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        SpannableString s = new SpannableString("Marks");
        s.setSpan(myTheme.getMyThemeTypeFaceSpan(), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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


    private void setData() {
        if (getIntent().hasExtra("class_number")) {
            myCourse = dataHandler.getCourse(getIntent().getStringExtra("class_number"));
            List<Mark> markList = myCourse.getCOURSE_MARKS();
            float tempInternals = 0;
            boolean internalsFlag = false;
            if (markList != null) {
                errorMessage.setVisibility(TextView.GONE);
                layoutContainer.setVisibility(LinearLayout.VISIBLE);
                if (!markList.get(0).getMARK().isEmpty() && !markList.get(0).getMARK().equals("null") && markList.get(0).getMARK() != null) {
                    cat1Marks.setText(markList.get(0).getMARK() + "/50");
                    internalsFlag = true;
                    tempInternals = tempInternals + (float) (Float.parseFloat(markList.get(0).getMARK()) * 0.30);
                    if (Float.parseFloat(markList.get(0).getMARK()) < 25) {
                        cat1Indicator.setVisibility(LinearLayout.VISIBLE);
                    }
                }
                if (!markList.get(1).getMARK().isEmpty() && !markList.get(1).getMARK().equals("null") && markList.get(1).getMARK() != null) {
                    internalsFlag = true;
                    tempInternals = tempInternals + (float) (Float.parseFloat(markList.get(1).getMARK()) * 0.30);
                    cat2Marks.setText(markList.get(1).getMARK() + "/50");
                    if (Float.parseFloat(markList.get(1).getMARK()) < 25) {
                        cat2Indicator.setVisibility(LinearLayout.VISIBLE);
                    }
                }
                if (!markList.get(2).getMARK().isEmpty() && !markList.get(2).getMARK().equals("null") && markList.get(2).getMARK() != null) {
                    internalsFlag = true;
                    tempInternals = tempInternals + (float) (Float.parseFloat(markList.get(2).getMARK()));
                    quiz1Marks.setText(markList.get(2).getMARK() + "/5");
                }
                if (!markList.get(3).getMARK().isEmpty() && !markList.get(3).getMARK().equals("null") && markList.get(3).getMARK() != null) {
                    internalsFlag = true;
                    tempInternals = tempInternals + (float) (Float.parseFloat(markList.get(3).getMARK()));
                    quiz2Marks.setText(markList.get(3).getMARK() + "/5");
                }
                if (!markList.get(4).getMARK().isEmpty() && !markList.get(4).getMARK().equals("null") && markList.get(4).getMARK() != null) {
                    internalsFlag = true;
                    tempInternals = tempInternals + (float) (Float.parseFloat(markList.get(4).getMARK()));
                    quiz3Marks.setText(markList.get(4).getMARK() + "/5");
                }
                if (!markList.get(5).getMARK().isEmpty() && !markList.get(5).getMARK().equals("null") && markList.get(5).getMARK() != null) {
                    internalsFlag = true;
                    tempInternals = tempInternals + (float) (Float.parseFloat(markList.get(5).getMARK()));
                    assignmentMarks.setText(markList.get(5).getMARK() + "/5");
                }
                if (internalsFlag == true) {
                    internalsMarks.setText(tempInternals + "/50");
                    if (tempInternals < 25) {
                        internalsIndicator.setVisibility(LinearLayout.VISIBLE);
                    }
                }
            } else {
                errorMessage.setVisibility(TextView.VISIBLE);
                layoutContainer.setVisibility(LinearLayout.GONE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_button:
                onBackPressed();
                break;
        }
    }

    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(color);
        }
    }
}
