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

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.vit.vitio.Extras.ErrorDefinitions;
import io.vit.vitio.Extras.ReturnParcel;
import io.vit.vitio.Fragments.Courses.*;
import io.vit.vitio.Fragments.SubjectViewFragment;
import io.vit.vitio.HomeActivity;
import io.vit.vitio.Instances.Course;
import io.vit.vitio.Managers.ConnectAPI;
import io.vit.vitio.Managers.DataHandler;
import io.vit.vitio.Managers.Parsers.ParseCourses;
import io.vit.vitio.Managers.Parsers.ParseTimeTable;
import io.vit.vitio.R;

/**
 * Created by shalini on 16-06-2015.
 */
public class TimeTableFragment extends Fragment implements View.OnClickListener, ConnectAPI.RequestListener, ViewPager.OnPageChangeListener {

    private ConnectAPI connectAPI;
    private DataHandler dataHandler;

    //Declared Views
    private TextView mDate, tuDate, wDate, thDate, fDate, sDate, mDay, tuDay, wDay, thDay, fDay, sDay;
    private ImageView mImage, tuImage, wImage, thImage, fImage, sImage;
    private LinearLayout mBox, tuBox, wBox, thBox, fBox, sBox, calIcon;
    private ArrayList<ImageView> arrayImages;
    protected ViewPager myTimeTablePager;
    private SliderAdapter adapter;
    public static List<List<TimeTableListInfo>> TIME_TABLE_LIST;
    private ParseTimeTable parseTimeTable;
    private ProgressDialog dialog;
    private List<Course> courseList;
    Typeface typeface;
    private int NUM_PAGES=6;

    public TimeTableFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.timetable_fragment, container, false);
        init(rootView);
        setFonts();
        setListeners();
        dialog.setCancelable(false);
        changeImageBackground(0);
        if (dataHandler.isDatabaseBuild()) {
            setTimetableFromDatabase();
        } else {
            setTimeTableFromAPI();
        }


        return rootView;
    }


    private void setListeners() {
        mBox.setOnClickListener(this);
        tuBox.setOnClickListener(this);
        wBox.setOnClickListener(this);
        thBox.setOnClickListener(this);
        fBox.setOnClickListener(this);
        sBox.setOnClickListener(this);
        calIcon.setOnClickListener(this);
        connectAPI.setOnRequestListener(this);
        myTimeTablePager.addOnPageChangeListener(this);

    }

    private void init(ViewGroup rootView) {
        mDate = (TextView) rootView.findViewById(R.id.monday_date);
        tuDate = (TextView) rootView.findViewById(R.id.tuesday_date);
        wDate = (TextView) rootView.findViewById(R.id.wednesday_date);
        thDate = (TextView) rootView.findViewById(R.id.thursday_date);
        fDate = (TextView) rootView.findViewById(R.id.friday_date);
        sDate = (TextView) rootView.findViewById(R.id.saturday_date);

        mDay = (TextView) rootView.findViewById(R.id.monday_letter);
        tuDay = (TextView) rootView.findViewById(R.id.tuesday_letter);
        wDay = (TextView) rootView.findViewById(R.id.wednesday_letter);
        thDay = (TextView) rootView.findViewById(R.id.thursday_letter);
        fDay = (TextView) rootView.findViewById(R.id.friday_letter);
        sDay = (TextView) rootView.findViewById(R.id.saturday_letter);

        mImage = (ImageView) rootView.findViewById(R.id.mImage);
        tuImage = (ImageView) rootView.findViewById(R.id.tuImage);
        wImage = (ImageView) rootView.findViewById(R.id.wImage);
        thImage = (ImageView) rootView.findViewById(R.id.thImage);
        fImage = (ImageView) rootView.findViewById(R.id.fImage);
        sImage = (ImageView) rootView.findViewById(R.id.sImage);

        mBox = (LinearLayout) rootView.findViewById(R.id.monday_box);
        tuBox = (LinearLayout) rootView.findViewById(R.id.tuesday_box);
        wBox = (LinearLayout) rootView.findViewById(R.id.wednesday_box);
        thBox = (LinearLayout) rootView.findViewById(R.id.thursday_box);
        fBox = (LinearLayout) rootView.findViewById(R.id.friday_box);
        sBox = (LinearLayout) rootView.findViewById(R.id.saturday_box);
        calIcon = (LinearLayout) rootView.findViewById(R.id.calender_icon);

        myTimeTablePager= (ViewPager) rootView.findViewById(R.id.pager);
        arrayImages = new ArrayList<>();
        arrayImages.add(mImage);
        arrayImages.add(tuImage);
        arrayImages.add(wImage);
        arrayImages.add(thImage);
        arrayImages.add(fImage);
        arrayImages.add(sImage);

        connectAPI = new ConnectAPI(getActivity());
        dataHandler = new DataHandler(getActivity());

        dialog = new ProgressDialog(getActivity());

    }

    private void setFonts() {
        typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/Montserrat-Regular.ttf");
        mDate.setTypeface(typeface);
        tuDate.setTypeface(typeface);
        wDate.setTypeface(typeface);
        thDate.setTypeface(typeface);
        fDate.setTypeface(typeface);
        sDate.setTypeface(typeface);

        mDay.setTypeface(typeface);
        tuDay.setTypeface(typeface);
        wDay.setTypeface(typeface);
        thDay.setTypeface(typeface);
        fDay.setTypeface(typeface);
        sDay.setTypeface(typeface);
    }


    public void setTimetableFromDatabase() {
        courseList= dataHandler.getCoursesList();
        parseTimeTable=new ParseTimeTable(courseList,dataHandler);
        parseTimeTable.parse();
        TIME_TABLE_LIST=parseTimeTable.getFullTimeTable();
        setTimetable(0);
    }

    public void setTimetableFromDatabase(List<Course> list) {
        courseList= list;
        parseTimeTable=new ParseTimeTable(courseList,dataHandler);
        parseTimeTable.parse();
        TIME_TABLE_LIST=parseTimeTable.getFullTimeTable();
        setTimetable(0);
    }

    public void setTimeTableFromAPI() {
        connectAPI.serverTest();
    }

    public void setTimetable(int day) {
        if(TIME_TABLE_LIST!=null){
            adapter=new SliderAdapter(getChildFragmentManager());
            myTimeTablePager.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            changeImageBackground(0);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.monday_box:
                changeImageBackground(0);
                if(adapter!=null){
                    myTimeTablePager.setCurrentItem(0,true);
                }
                break;
            case R.id.tuesday_box:
                changeImageBackground(1);
                if(adapter!=null){
                    myTimeTablePager.setCurrentItem(1,true);
                }
                break;
            case R.id.wednesday_box:
                changeImageBackground(2);
                if(adapter!=null){
                    myTimeTablePager.setCurrentItem(2,true);
                }
                break;
            case R.id.thursday_box:
                changeImageBackground(3);
                if(adapter!=null){
                    myTimeTablePager.setCurrentItem(3,true);
                }
                break;
            case R.id.friday_box:
                changeImageBackground(4);
                if(adapter!=null){
                    myTimeTablePager.setCurrentItem(4,true);
                }
                break;
            case R.id.saturday_box:
                changeImageBackground(5);
                if(adapter!=null){
                    myTimeTablePager.setCurrentItem(5,true);
                }
                break;
            case R.id.calender_icon:
                DialogFragment newFragment = new DatePickerFragment(this);
                newFragment.show(getChildFragmentManager(), "datePicker");
                break;
            default:
                changeImageBackground(0);
                if(adapter!=null){
                    myTimeTablePager.setCurrentItem(0,true);
                }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setToolbarFormat(2);
        ((HomeActivity) getActivity()).changeStatusBarColor(2);
    }

    public void changeImageBackground(int i) {
        for (int j = 0; j < arrayImages.size(); j++) {
            if (j == i) {
                arrayImages.get(i).setActivated(true);
            } else {
                arrayImages.get(j).setActivated(false);
            }
        }
    }
    public void changePage(int i) {
        if(adapter!=null){
            myTimeTablePager.setCurrentItem(i,true);
        }
    }

    @Override
    public void onRequestInitiated(int code) {
        dialog.setTitle("Initiating");
        switch (code) {
            case ConnectAPI.SERVERTEST_CODE:
                dialog.setMessage("Server Testing");
                break;
            case ConnectAPI.LOGIN_CODE:
                dialog.setMessage("Logging In");
                break;
            case ConnectAPI.REFRESH_CODE:
                dialog.setMessage("Refreshing");
                break;
            default:

        }
        dialog.show();
    }

    @Override
    public void onRequestCompleted(ReturnParcel parcel, int code) {

        switch (code) {
            case ConnectAPI.SERVERTEST_CODE:
                if (parcel.getRETURN_CODE() == ErrorDefinitions.CODE_SUCCESS) {
                    connectAPI.login();
                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    showToast(parcel.getRETURN_MESSAGE());
                }
                break;
            case ConnectAPI.LOGIN_CODE:
                if (parcel.getRETURN_CODE() == ErrorDefinitions.CODE_SUCCESS) {
                    connectAPI.refresh();
                } else {
                    if (dialog.isShowing()) {
                        dialog.hide();
                    }
                    showToast(parcel.getRETURN_MESSAGE());
                }
                break;
            case ConnectAPI.REFRESH_CODE:
                if (dialog.isShowing()) {
                    dialog.hide();
                }
                if (parcel.getRETURN_CODE() == ErrorDefinitions.CODE_SUCCESS) {
                    setTimetableFromDatabase();
                } else {
                    showToast(parcel.getRETURN_MESSAGE());
                }
                break;
            default:

        }
    }


    @Override
    public void onErrorRequest(ReturnParcel parcel, int code) {
        if (dialog.isShowing()) {
            dialog.hide();
        }
        showToast(parcel.getRETURN_MESSAGE());
        switch (code) {
            case ConnectAPI.SERVERTEST_CODE:
                break;
            case ConnectAPI.LOGIN_CODE:
                break;
            case ConnectAPI.REFRESH_CODE:
                break;
            default:

        }
    }

    public void showToast(String return_message) {
        Toast.makeText(getActivity(), return_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeImageBackground(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener,DatePickerDialog.OnCancelListener {
        DatePickerDialog dialog;
        TimeTableFragment outerInstance;

        public DatePickerFragment() {
        }

        @SuppressLint("ValidFragment")
        public DatePickerFragment(TimeTableFragment timeTableFragment) {
            outerInstance=timeTableFragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();

            // Create a new instance of DatePickerDialog and return it
            dialog= new DatePickerDialog(getActivity(), this, c.get(Calendar.YEAR),c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            DatePicker picker=dialog.getDatePicker();
            return dialog;
        }


        public void onDateSet(DatePicker view, int year, int month, int day) {
            if(view.isShown()){
                //Toast.makeText(getActivity(),"Y:"+year+"m:"+month+"d:"+day,Toast.LENGTH_SHORT).show();
                Calendar calendar=Calendar.getInstance();
                calendar.set(year,month,day);
                if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
                    Toast.makeText(getActivity(),"No classes on Sunday",Toast.LENGTH_SHORT).show();
                }
                else{
                    outerInstance.changeImageBackground(calendar.get(Calendar.DAY_OF_WEEK) - 2);
                    outerInstance.changePage(calendar.get(Calendar.DAY_OF_WEEK) - 2);
                }
                Log.d("day", String.valueOf(day));}
            // Do something with the date chosen by the user
        }

        @Override
        public void onCancel(DialogInterface dialog) {
            super.onCancel(dialog);
            Log.d("date","cancel");
        }

        /*
        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Log.d("day1", String.valueOf(dayOfMonth));
            Calendar calendar=Calendar.getInstance();
            calendar.set(year,monthOfYear,dayOfMonth);
            if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
            else{
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            }
        }
        */
    }

    private class SliderAdapter extends FragmentStatePagerAdapter {

        public SliderAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("getItem", String.valueOf(position));
            PagerFragment fragment= new PagerFragment();
            Bundle bundle=new Bundle();
            bundle.putInt("mode", position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override

        public int getCount() {
            return NUM_PAGES;
        }
    }

}
