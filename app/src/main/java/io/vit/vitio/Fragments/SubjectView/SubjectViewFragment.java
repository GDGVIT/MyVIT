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

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.concurrent.ThreadPoolExecutor;

import io.vit.vitio.AttendanceActivity;
import io.vit.vitio.HomeActivity;
import io.vit.vitio.Instances.Attendance;
import io.vit.vitio.MarksActivity;
import io.vit.vitio.Instances.Course;
import io.vit.vitio.Managers.DataHandler;
import io.vit.vitio.R;

/**
 * Created by shalini on 18-06-2015.
 */
public class SubjectViewFragment extends Fragment implements View.OnClickListener {
    private TextView subName, subType, subCode, subPer, subSlot, subVenue, attended, subSchool, subFaculty,attendClassText,missClassText;
    private FloatingActionButton fab;
    private ImageView subColorCircle, back_button, schoolImage;
    private LinearLayout attBar, missMinus, missAdd, attendMinus, attendAdd;
    private FloatingActionButton attFab, marksFab, reminderFab;
    private String MY_CLASS_NUMBER;
    private Course myCourse;
    private int MISS_COUNTS = 0;
    private int ATTEND_COUNTS = 0;
    private DataHandler dataHandler;
    Typeface typeface;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.subject_view_fragment, container, false);
        init(rootView);
        setFonts();
        myCourse = dataHandler.getCourse(MY_CLASS_NUMBER);
        back_button.setOnClickListener(this);
        marksFab.setOnClickListener(this);
        attFab.setOnClickListener(this);
        reminderFab.setOnClickListener(this);
        missMinus.setOnClickListener(this);
        missAdd.setOnClickListener(this);
        attendMinus.setOnClickListener(this);
        attendAdd.setOnClickListener(this);
        if (myCourse != null) {
            setData();
        }
        return rootView;
    }


    private void init(ViewGroup rootView) {

        //Get Arguments
        try {
            MY_CLASS_NUMBER = getArguments().getString("class_number");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
        //Initialize TextViews
        subName = (TextView) rootView.findViewById(R.id.subject_name);
        subType = (TextView) rootView.findViewById(R.id.subject_type);
        subCode = (TextView) rootView.findViewById(R.id.subject_code);
        subSlot = (TextView) rootView.findViewById(R.id.subject_slot);
        subVenue = (TextView) rootView.findViewById(R.id.subject_venue);
        subPer = (TextView) rootView.findViewById(R.id.subject_perc);
        subFaculty = (TextView) rootView.findViewById(R.id.subject_faculty);
        //subSchool = (TextView) rootView.findViewById(R.id.subject_school);
        attended = (TextView) rootView.findViewById(R.id.subject_att_out_total);
        attendClassText= (TextView) rootView.findViewById(R.id.attend_class_text);
        missClassText = (TextView) rootView.findViewById(R.id.miss_class_text);

        //Initialize ImageViews
        subColorCircle = (ImageView) rootView.findViewById(R.id.subject_color_circle);
        missMinus = (LinearLayout) rootView.findViewById(R.id.miss_minus);
        missAdd = (LinearLayout) rootView.findViewById(R.id.miss_plus);
        attendMinus = (LinearLayout) rootView.findViewById(R.id.attend_minus);
        attendAdd = (LinearLayout) rootView.findViewById(R.id.attend_plus);
        back_button = (ImageView) rootView.findViewById(R.id.back_button);
        schoolImage = (ImageView) rootView.findViewById(R.id.school_image);

        //Initialize LinearLayouts
        attBar = (LinearLayout) rootView.findViewById(R.id.subject_per_bar);

        //Initialize Others
        attFab = (FloatingActionButton) rootView.findViewById(R.id.att_fab);
        marksFab = (FloatingActionButton) rootView.findViewById(R.id.marks_fab);
        reminderFab = (FloatingActionButton) rootView.findViewById(R.id.reminder_fab);
        dataHandler = DataHandler.getInstance(getActivity());

    }

    private void setFonts() {
        typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/Montserrat-Regular.ttf");
        subCode.setTypeface(typeface);
        subName.setTypeface(typeface);
        subPer.setTypeface(typeface);
        subVenue.setTypeface(typeface);
        subType.setTypeface(typeface);
        subSlot.setTypeface(typeface);
        subFaculty.setTypeface(typeface);
        //subSchool.setTypeface(typeface);
        attended.setTypeface(typeface);
        attendClassText.setTypeface(typeface);
        missClassText.setTypeface(typeface);

    }

    private void setData() {
        subName.setText(myCourse.getCOURSE_TITLE());
        subSlot.setText(myCourse.getCOURSE_SLOT());
        subType.setText(myCourse.getCOURSE_TYPE());
        subCode.setText(myCourse.getCOURSE_CODE());
        subFaculty.setText(myCourse.getCOURSE_FACULTY().getNAME());
        //subSchool.setText(myCourse.getCOURSE_FACULTY().getSCHOOL());

        schoolImage.setImageResource(myCourse.getBuildingImageId());
        subPer.setText(myCourse.getCOURSE_ATTENDANCE().getPERCENTAGE() + "%");
        subVenue.setText(myCourse.getCOURSE_VENUE());
        attended.setText("attended " + myCourse.getCOURSE_ATTENDANCE().getATTENDED_CLASSES() + " out " + myCourse.getCOURSE_ATTENDANCE().getTOTAL_CLASSES());
        attBar.setLayoutParams(new LinearLayout.LayoutParams(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17, getResources().getDisplayMetrics()), myCourse.getCOURSE_ATTENDANCE().getPERCENTAGE()));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back_button:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.marks_fab:
                Intent intent = new Intent(getActivity(), MarksActivity.class);
                intent.putExtra("class_number", MY_CLASS_NUMBER);
                startActivity(intent);
                break;
            case R.id.att_fab:
                Intent intent1 = new Intent(getActivity(), AttendanceActivity.class);
                intent1.putExtra("class_number", MY_CLASS_NUMBER);
                startActivity(intent1);
                break;
            case R.id.reminder_fab:
                Log.d("rem", "fab");
                Toast.makeText(getActivity(), "Reminders coming soon!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.miss_minus:
                if (MISS_COUNTS > 0) {
                    MISS_COUNTS--;
                    reflectAttendance();
                }
                break;
            case R.id.miss_plus:
                MISS_COUNTS++;
                reflectAttendance();
                break;
            case R.id.attend_minus:
                if (ATTEND_COUNTS > 0) {
                    ATTEND_COUNTS--;
                    reflectAttendance();
                }
                break;
            case R.id.attend_plus:
                ATTEND_COUNTS++;
                reflectAttendance();
                break;
            default:

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //((HomeActivity)getActivity()).hideToolbar();
        HomeActivity h = (HomeActivity) getActivity();
        h.setToolbarFormat(0, "SUBJECT ANALYSIS");
        h.changeStatusBarColor(0);
    }

    private void reflectAttendance() {
        attendClassText.setText(ATTEND_COUNTS>1?"If you attend "+ATTEND_COUNTS+" classes":"If you attend "+ATTEND_COUNTS+" class");
        missClassText.setText(MISS_COUNTS>1?"If you miss "+MISS_COUNTS+" classes":"If you miss "+MISS_COUNTS+" class");
        int attClass = myCourse.getCOURSE_ATTENDANCE().getATTENDED_CLASSES();
        int totClass = myCourse.getCOURSE_ATTENDANCE().getTOTAL_CLASSES();
        int mulFactor = 1;
        if (myCourse.getCOURSE_TYPE_SHORT().equals("L")) {
            mulFactor = myCourse.getCOURSE_LTPC().getPRACTICAL();
        }
        if (dataHandler.getSemester().equals("SS")) {
            int modAtt = myCourse.getCOURSE_ATTENDANCE().getModifiedPercentage(attClass + ATTEND_COUNTS * 2 * mulFactor, totClass + (ATTEND_COUNTS + MISS_COUNTS) * 2 * mulFactor);
            attended.setText("attended " + (attClass + ATTEND_COUNTS * 2 * mulFactor) + " out " + (totClass + (ATTEND_COUNTS + MISS_COUNTS) * 2 * mulFactor));
            subPer.setText(modAtt + " %");
            attBar.setLayoutParams(new LinearLayout.LayoutParams(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17, getResources().getDisplayMetrics()), modAtt));

        } else {
            int modAtt = myCourse.getCOURSE_ATTENDANCE().getModifiedPercentage(attClass + ATTEND_COUNTS*mulFactor, totClass + (ATTEND_COUNTS + MISS_COUNTS)*mulFactor);
            attended.setText("attended " + (attClass + ATTEND_COUNTS*mulFactor) + " out " + (totClass + (ATTEND_COUNTS + MISS_COUNTS)*mulFactor));
            subPer.setText(modAtt + " %");
            attBar.setLayoutParams(new LinearLayout.LayoutParams(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17, getResources().getDisplayMetrics()), modAtt));

        }
    }
}
