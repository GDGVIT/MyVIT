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

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionInflater;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.vit.vitio.Extras.ErrorDefinitions;
import io.vit.vitio.Extras.ReturnParcel;
import io.vit.vitio.Extras.Themes.MyTheme;
import io.vit.vitio.Fragments.SubjectView.SubjectViewFragmentTrial;
import io.vit.vitio.Fragments.TimeTable.TimeTableListInfo;
import io.vit.vitio.HomeActivity;
import io.vit.vitio.Instances.Course;
import io.vit.vitio.Managers.ConnectAPI;
import io.vit.vitio.Managers.DataHandler;
import io.vit.vitio.Managers.Parsers.ParseTimeTable;
import io.vit.vitio.R;

/**
 * Created by shalini on 16-06-2015.
 */
public class TodayFragment extends Fragment implements View.OnClickListener, ConnectAPI.RequestListener {

    private ConnectAPI connectAPI;
    private DataHandler dataHandler;

    //Declare Views
    private TextView subjectCode, subjectName, subjectTime, subjectPer, subjectVenue, ifmissedPer, ocassionQoute;
    private RecyclerView recyclerView;
    private LinearLayout attendanceBar, bottomHalf, ocassionContainer, ocassionContainerInner,topHalf,headerLayout;
    private ImageView ocassionImage;

    private TodayListAdapter adapter;
    private List<TimeTableListInfo> todaytimeTable;
    private ParseTimeTable parseTimeTable;
    private ProgressDialog dialog;
    private List<Course> courseList;
    private Typeface typeface;
    private MyTheme theme;
    private GeneralSwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.today_fragment, container, false);
        init(rootView);
        setFonts();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setListeners();
        setTransitions();
        dialog.setCancelable(false);
        setData();
        return rootView;
    }

    private void setTransitions() {
        if(Build.VERSION.SDK_INT>=21) {
            setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));
            setReenterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));
        }
    }


    private void init(ViewGroup rootView) {
        subjectCode = (TextView) rootView.findViewById(R.id.subject_code);
        subjectName = (TextView) rootView.findViewById(R.id.subject_name);
        subjectTime = (TextView) rootView.findViewById(R.id.subject_time);
        subjectPer = (TextView) rootView.findViewById(R.id.subject_per);
        subjectVenue = (TextView) rootView.findViewById(R.id.subject_venue);
        ifmissedPer = (TextView) rootView.findViewById(R.id.ifmissed_per);
        ocassionQoute = (TextView) rootView.findViewById(R.id.ocassion_qoute);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.today_recycler_view);
        attendanceBar = (LinearLayout) rootView.findViewById(R.id.attendance_bar);
        bottomHalf = (LinearLayout) rootView.findViewById(R.id.bottom_half_content);
        topHalf = (LinearLayout) rootView.findViewById(R.id.top_half_content);
        ocassionContainer = (LinearLayout) rootView.findViewById(R.id.ocassion_container);
        ocassionContainerInner = (LinearLayout) rootView.findViewById(R.id.ocassion_container_inner);
        headerLayout= (LinearLayout) rootView.findViewById(R.id.header_layout);

        ocassionImage = (ImageView) rootView.findViewById(R.id.ocassion_image);

       /* weekendMusicImage=(ImageView)rootView.findViewById(R.id.weekend_music_image);
        weekendGlassesImage=(ImageView)rootView.findViewById(R.id.weekend_glasses_image);
        weekendShirtImage=(ImageView)rootView.findViewById(R.id.weekend_shirt_image);
        weekendCameraImage=(ImageView)rootView.findViewById(R.id.weekend_camera_image);
        */

        connectAPI = new ConnectAPI(getActivity());
        dataHandler = new DataHandler(getActivity());

        dialog = new ProgressDialog(getActivity());

        swipeRefreshLayout = (GeneralSwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setRecyclerView(recyclerView);

        theme=new MyTheme(getActivity());
    }

    private void setFonts() {
        typeface = theme.getMyThemeTypeface();
        subjectCode.setTypeface(typeface);
        subjectName.setTypeface(typeface);
        subjectTime.setTypeface(typeface);
        subjectPer.setTypeface(typeface);
        subjectVenue.setTypeface(typeface);
        ifmissedPer.setTypeface(typeface);
        ocassionQoute.setTypeface(typeface);
    }

    private void setListeners() {
        connectAPI.setOnRequestListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);//this should be false for roatation
                    }
                }, 5000);

                if (!dataHandler.isWeekend()) {

                    swipeRefreshLayout.setEnabled(true);
                    setTimeTableFromAPI();

                } else {
                    Toast.makeText(getActivity(), "Refresh your Timetable", Toast.LENGTH_SHORT).show();
                }
            }


        });


    }

    private void setData() {
        if (!dataHandler.isWeekend()) {
            ocassionContainer.setVisibility(LinearLayout.GONE);
            if (dataHandler.isDatabaseBuild()) {
                setTimetableFromDatabase();
            } else {
                setTimeTableFromAPI();
            }
        } else {
            setWeekendFormat();
        }
    }

    private void setTimetableFromDatabase() {
        courseList = dataHandler.getCoursesList();
        parseTimeTable = new ParseTimeTable(courseList, dataHandler);
        parseTimeTable.parse();
        todaytimeTable = parseTimeTable.getTodayTimeTable();
        setTimetable();
    }

    private void setTimetableFromDatabase(List<Course> list) {
        courseList = list;
        parseTimeTable = new ParseTimeTable(courseList, dataHandler);
        parseTimeTable.parse();
        todaytimeTable = parseTimeTable.getTodayTimeTable();
        setTimetable();
    }

    private void setTimeTableFromAPI() {
        connectAPI.serverTest();
    }

    private void setTimetable() {
        if (todaytimeTable != null) {

            adapter = new TodayListAdapter(getActivity(), todaytimeTable);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            setNowHeader(todaytimeTable);
        }

    }

    private void setWeekendFormat() {
        bottomHalf.setVisibility(LinearLayout.GONE);
        topHalf.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,100));
        subjectCode.setVisibility(LinearLayout.GONE);
        //subjectName.setVisibility(TextView.GONE);
        subjectName.setText("it's a");
        subjectTime.setText("weekend!");
        subjectTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 46);
        subjectName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
        ocassionContainer.setVisibility(LinearLayout.VISIBLE);
        ocassionImage.setImageResource(R.drawable.weekend_vector);
        ocassionQoute.setText(getString(R.string.weekend_qoute));
        animateView();
        recyclerView.setAdapter(new TodayListAdapter(getActivity(), new ArrayList<TimeTableListInfo>()));
        /*ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(weekendContainerInner, "rotation", 0, 360);
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(weekendMusicImage, "rotation", 0, 360);
        ObjectAnimator objectAnimator2= ObjectAnimator.ofFloat(weekendGlassesImage, "rotation", 0, 360);
        ObjectAnimator objectAnimator3 = ObjectAnimator.ofFloat(weekendShirtImage, "rotation", 0, 360);
        ObjectAnimator objectAnimator4 = ObjectAnimator.ofFloat(weekendCameraImage, "rotation", 0, 360);
        objectAnimator.setDuration(10000);
        objectAnimator1.setDuration(11000);
        objectAnimator2.setDuration(8000);
        objectAnimator3.setDuration(13000);
        objectAnimator4.setDuration(10000);
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator1.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator2.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator3.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator4.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator1.setInterpolator(new LinearInterpolator());
        objectAnimator2.setInterpolator(new LinearInterpolator());
        objectAnimator3.setInterpolator(new LinearInterpolator());
        objectAnimator4.setInterpolator(new LinearInterpolator());
        //objectAnimator.start();
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(objectAnimator1,objectAnimator2,objectAnimator3,objectAnimator4);
        animatorSet.start();
        */

    }

    private void animateView() {
        PowerManager powerManager=(PowerManager)getActivity().getSystemService(Context.POWER_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP&&powerManager.isPowerSaveMode()){
            return;
        }
        //float dimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(ocassionImage, "scaleX", 1f, 0.8f);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(ocassionImage, "scaleY", 1f, 0.8f);
        objectAnimatorX.setDuration(6000);
        objectAnimatorY.setDuration(6000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(objectAnimatorX, objectAnimatorY);
        animatorSet.setInterpolator(new LinearInterpolator());
        objectAnimatorX.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimatorX.setRepeatMode(ObjectAnimator.REVERSE);
        objectAnimatorY.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimatorY.setRepeatMode(ObjectAnimator.REVERSE);
        animatorSet.start();
        objectAnimatorX.start();
    }

    private void setNowHeader(List<TimeTableListInfo> todaytimeTable) {

        subjectTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
        subjectName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
        topHalf.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,50));
        Course currentClass = parseTimeTable.getCurrentClass(todaytimeTable);

        if (currentClass != null) {
            subjectCode.setVisibility(TextView.VISIBLE);
            subjectName.setVisibility(TextView.VISIBLE);
            bottomHalf.setVisibility(LinearLayout.VISIBLE);
            subjectCode.setText(currentClass.getCOURSE_CODE());
            subjectName.setText(currentClass.getCOURSE_TITLE());
            subjectVenue.setText(currentClass.getCOURSE_VENUE());
            attendanceBar.setLayoutParams(new LinearLayout.LayoutParams(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()), currentClass.getCOURSE_ATTENDANCE().getPERCENTAGE()));
            int per = currentClass.getCOURSE_ATTENDANCE().getPERCENTAGE();
            if (per < 75) {
                subjectPer.setTextColor(getActivity().getResources().getColor(R.color.fadered));
                ((GradientDrawable) attendanceBar.getBackground()).setColor(getActivity().getResources().getColor(R.color.fadered));
            } else {
                subjectPer.setTextColor(getActivity().getResources().getColor(R.color.fadegreen));
                ((GradientDrawable) attendanceBar.getBackground()).setColor(getActivity().getResources().getColor(R.color.fadegreen));
            }
            subjectPer.setText(per + "%");

            int ifmissed = 0;

            int mulFactor = 1;

            Log.d("L", String.valueOf(currentClass.getCOURSE_TYPE_SHORT()));
            if (currentClass.getCOURSE_TYPE_SHORT().equals("L")) {
                mulFactor = currentClass.getCOURSE_LTPC().getPRACTICAL();
                Log.d("L", String.valueOf(currentClass.getCOURSE_LTPC().getPRACTICAL()));
            }
            if (dataHandler.getSemester().equals("SS")) {
                ifmissed = currentClass.getCOURSE_ATTENDANCE().getModifiedPercentage(currentClass.getCOURSE_ATTENDANCE().getATTENDED_CLASSES(),currentClass.getCOURSE_ATTENDANCE().getTOTAL_CLASSES()+2*mulFactor);

            } else {
                ifmissed = currentClass.getCOURSE_ATTENDANCE().getModifiedPercentage(currentClass.getCOURSE_ATTENDANCE().getATTENDED_CLASSES(),currentClass.getCOURSE_ATTENDANCE().getTOTAL_CLASSES()+mulFactor);
            }

            if (ifmissed < 75)
                ifmissedPer.setTextColor(getActivity().getResources().getColor(R.color.fadered));
            else
                ifmissedPer.setTextColor(getActivity().getResources().getColor(R.color.fadegreen));
            ifmissedPer.setText(ifmissed + "%");
            subjectTime.setText("Right Now");

        } else {
            TodayHeader header = parseTimeTable.getNextClass(todaytimeTable);
            Course nextClass = header.getCourse();
            if (nextClass != null) {
                subjectCode.setVisibility(TextView.VISIBLE);
                subjectName.setVisibility(TextView.VISIBLE);
                bottomHalf.setVisibility(LinearLayout.VISIBLE);
                subjectCode.setText(nextClass.getCOURSE_CODE());
                subjectName.setText(nextClass.getCOURSE_TITLE());
                subjectVenue.setText(nextClass.getCOURSE_VENUE());
                attendanceBar.setLayoutParams(new LinearLayout.LayoutParams(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()), nextClass.getCOURSE_ATTENDANCE().getPERCENTAGE()));
                int per = nextClass.getCOURSE_ATTENDANCE().getPERCENTAGE();
                if (per < 75) {
                    subjectPer.setTextColor(getActivity().getResources().getColor(R.color.fadered));
                    ((GradientDrawable) attendanceBar.getBackground()).setColor(getActivity().getResources().getColor(R.color.fadered));
                } else {
                    subjectPer.setTextColor(getActivity().getResources().getColor(R.color.fadegreen));
                    ((GradientDrawable) attendanceBar.getBackground()).setColor(getActivity().getResources().getColor(R.color.fadegreen));
                }
                subjectPer.setText(per + "%");

                int ifmissed = 0;

                int mulFactor = 1;

                Log.d("L", String.valueOf(nextClass.getCOURSE_TYPE_SHORT()));
                if (nextClass.getCOURSE_TYPE_SHORT().equals("L")) {
                    mulFactor = nextClass.getCOURSE_LTPC().getPRACTICAL();
                    Log.d("L", String.valueOf(nextClass.getCOURSE_LTPC().getPRACTICAL()));
                }
                if (dataHandler.getSemester().equals("SS")) {
                    ifmissed = nextClass.getCOURSE_ATTENDANCE().getModifiedPercentage(nextClass.getCOURSE_ATTENDANCE().getATTENDED_CLASSES(),nextClass.getCOURSE_ATTENDANCE().getTOTAL_CLASSES()+2*mulFactor);

                } else {
                    ifmissed = nextClass.getCOURSE_ATTENDANCE().getModifiedPercentage(nextClass.getCOURSE_ATTENDANCE().getATTENDED_CLASSES(),nextClass.getCOURSE_ATTENDANCE().getTOTAL_CLASSES()+mulFactor);
                }

                if (ifmissed < 75)
                    ifmissedPer.setTextColor(getActivity().getResources().getColor(R.color.fadered));
                else
                    ifmissedPer.setTextColor(getActivity().getResources().getColor(R.color.fadegreen));
                ifmissedPer.setText(ifmissed + "%");
                subjectTime.setText(header.getStatus());
            } else {
                topHalf.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,100));
                bottomHalf.setVisibility(LinearLayout.GONE);
                subjectCode.setVisibility(LinearLayout.GONE);
                recyclerView.setAdapter(new TodayListAdapter(getActivity(), new ArrayList<TimeTableListInfo>()));
                //subjectName.setVisibility(TextView.GONE);
                ocassionContainer.setVisibility(LinearLayout.VISIBLE);
                subjectName.setText("done for the");
                subjectTime.setText("day!");
                subjectTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 46);
                subjectName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
                ocassionImage.setImageResource(R.drawable.end_of_day_vector);
                ocassionQoute.setText(getString(R.string.end_of_day_qoute));
                animateView();
                /*bottomHalf.setVisibility(LinearLayout.GONE);
                subjectCode.setVisibility(LinearLayout.GONE);
                subjectName.setVisibility(TextView.GONE);
                subjectTime.setText("Your classes are over!");
                TypedArray imageIds=getResources().obtainTypedArray(R.array.endofday_images);
                ArrayList<Integer> list=new ArrayList<>();
                for(int i=0;i<imageIds.length();i++){
                    list.add(new Integer(imageIds.getResourceId(i,-1)));
                }
                recyclerView.setAdapter(new TodayListAdapter(getActivity(), list));
                adapter.notifyDataSetChanged();*/
            }
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
                        dialog.hide();
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

    private void showToast(String return_message) {
        Toast.makeText(getActivity(), return_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setToolbarFormat(0);
        ((HomeActivity) getActivity()).changeStatusBarColor(0);
        setData();
        theme.refreshTheme();
        headerLayout.setBackgroundColor(theme.getToolbarColorTypedArray().getColor(0,-1));
        setFonts();
    }



    @Override
    public void onClick(View v) {

    }

    private class TodayListAdapter extends RecyclerView.Adapter<TodayListAdapter.TodayViewHolder> {
        private final List<TimeTableListInfo> dataT;
        private final ArrayList<Integer> dataI;
        private LayoutInflater inflater;
        private Context c;

        public TodayListAdapter(Context context, List<TimeTableListInfo> list) {
            dataT = list;
            dataI = null;
            c = context;
            inflater = LayoutInflater.from(c);
        }

        public TodayListAdapter(Context context, ArrayList<Integer> list) {
            dataI = list;
            dataT = null;
            c = context;
            inflater = LayoutInflater.from(c);
        }

        @Override
        public TodayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.today_list_recycler_row, parent, false);
            TodayViewHolder todayViewHolder = new TodayViewHolder(view);
            return todayViewHolder;
        }


        @Override
        public void onBindViewHolder(TodayViewHolder holder, int position) {
            if (dataT != null) {
                float d = getResources().getDisplayMetrics().density;
                holder.layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (75 * d)));
               // holder.middleContentImage.setVisibility(ImageView.GONE);
                holder.middleContentInfo.setVisibility(LinearLayout.VISIBLE);
                holder.rightContentInfo.setVisibility(LinearLayout.VISIBLE);
                TimeTableListInfo info = dataT.get(position);
                holder.subName.setText(info.name);
                holder.subTime.setText(info.time12);
                holder.subVenue.setText(info.venue);
                holder.subTypeShort.setText(info.typeShort);
                int p = Integer.parseInt((info.per.split(" ")[0]));
                if (p < 75)
                    holder.subPer.setTextColor(c.getResources().getColor(R.color.fadered));
                else
                    holder.subPer.setTextColor(c.getResources().getColor(R.color.fadegreen));
                holder.subPer.setText(info.per);
                if (position == dataT.size() - 1) {
                    holder.contLine.setVisibility(LinearLayout.INVISIBLE);
                }
            } else if (dataI != null) {
               // holder.middleContentImage.setVisibility(ImageView.VISIBLE);
                holder.middleContentInfo.setVisibility(LinearLayout.GONE);
                holder.rightContentInfo.setVisibility(LinearLayout.GONE);
                holder.layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                Integer id = dataI.get(position);
                //holder.middleContentImage.setImageResource(id.intValue());
                if (position == dataI.size() - 1) {
                    holder.contLine.setVisibility(LinearLayout.INVISIBLE);
                }
            }
        }

        @Override
        public int getItemCount() {
            if (dataI != null)
                return dataI.size();
            else if (dataT != null)
                return dataT.size();
            else return 0;
        }



        class TodayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView subName, subTime, subPer, subVenue, subTypeShort;
            LinearLayout layout, contLine, middleContentInfo, rightContentInfo;
            ImageView middleContentImage;


            public TodayViewHolder(View itemView) {
                super(itemView);
                subName = (TextView) itemView.findViewById(R.id.subject_name);
                subTime = (TextView) itemView.findViewById(R.id.subject_time);
                subPer = (TextView) itemView.findViewById(R.id.subject_per);
                subVenue = (TextView) itemView.findViewById(R.id.subject_venue);
                subTypeShort = (TextView) itemView.findViewById(R.id.subject_type_short);
                contLine = (LinearLayout) itemView.findViewById(R.id.cont_line);
                middleContentInfo = (LinearLayout) itemView.findViewById(R.id.middle_content_info);
                rightContentInfo = (LinearLayout) itemView.findViewById(R.id.right_content_info);

                //middleContentImage = (ImageView) itemView.findViewById(R.id.middle_content_image);
                subName.setTypeface(typeface);
                subTime.setTypeface(typeface);
                subPer.setTypeface(typeface);
                subVenue.setTypeface(typeface);
                layout = (LinearLayout) itemView.findViewById(R.id.row_holder);
                if (dataT != null)
                    itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                SubjectViewFragmentTrial subject = new SubjectViewFragmentTrial();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                Bundle arguments = new Bundle();
                arguments.putString("class_number", String.valueOf(dataT.get(getAdapterPosition()).clsnbr));
                subject.setArguments(arguments);
                ft.replace(R.id.main_fragment_holder, subject);
                //ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                if(Build.VERSION.SDK_INT>=21) {
                    setSharedElementReturnTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.trans_move));
                    setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));
                    subject.setSharedElementEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(R.transition.trans_move));
                    subject.setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));
                    subject.setSubNameId(subName.getTransitionName());
                    subject.setImageNameId(headerLayout.getTransitionName());
                    ft.addSharedElement(subName,subName.getTransitionName());
                    ft.addSharedElement(headerLayout,headerLayout.getTransitionName());
                }

                ft.addToBackStack(null);
                ft.commit();
            }

        }
    }

}
