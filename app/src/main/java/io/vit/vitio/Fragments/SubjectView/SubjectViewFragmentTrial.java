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

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.List;

import io.vit.vitio.AttendanceActivity;
import io.vit.vitio.Extras.Themes.MyTheme;
import io.vit.vitio.HomeActivity;
import io.vit.vitio.Instances.Course;
import io.vit.vitio.Instances.Timing;
import io.vit.vitio.Managers.DataHandler;
import io.vit.vitio.Managers.Parsers.ParseTimeTable;
import io.vit.vitio.MarksActivity;
import io.vit.vitio.R;
import io.vit.vitio.SampleActivity;

/**
 * Created by shalini on 18-06-2015.
 */
public class SubjectViewFragmentTrial extends Fragment implements View.OnClickListener {
    private TextView subName, subType, subCode, subPer, subSlot, subVenue, attended, subSchool, subFaculty, attendClassText, missClassText;
    private FloatingActionButton fab;
    private ImageView subColorCircle, back_button, schoolImage;
    private LinearLayout attBar, missMinus, missAdd, attendMinus, attendAdd,fullViewTint;
    private RecyclerView recyclerView;
    //private FloatingActionButton attFab, marksFab, reminderFab;
    private FloatingActionButton attFab, marksFab;
    private FloatingActionsMenu floatingMenu;
    private String MY_CLASS_NUMBER;
    private Course myCourse;
    private int MISS_COUNTS = 0;
    private int ATTEND_COUNTS = 0;
    private DataHandler dataHandler;
    Typeface typeface;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private NestedScrollView nestedScrollView;
    private MyTheme theme;
    private String SUB_NAME_ID;
    private String IMAGE_NAME_ID;

    private ParseTimeTable parseTimeTable;
    private TimingAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.subject_view_fragment_trial_two, container, false);
        init(rootView);
        setInit();
        setFonts();
        setTransitions();

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
        attendClassText = (TextView) rootView.findViewById(R.id.attend_class_text);
        missClassText = (TextView) rootView.findViewById(R.id.miss_class_text);

        //Initialize ImageViews
        subColorCircle = (ImageView) rootView.findViewById(R.id.subject_color_circle);
        missMinus = (LinearLayout) rootView.findViewById(R.id.miss_minus);
        missAdd = (LinearLayout) rootView.findViewById(R.id.miss_plus);
        attendMinus = (LinearLayout) rootView.findViewById(R.id.attend_minus);
        attendAdd = (LinearLayout) rootView.findViewById(R.id.attend_plus);
        fullViewTint= (LinearLayout) rootView.findViewById(R.id.full_view_tint);
        back_button = (ImageView) rootView.findViewById(R.id.back_button);
        schoolImage = (ImageView) rootView.findViewById(R.id.school_image);

        //Initialize LinearLayouts
        attBar = (LinearLayout) rootView.findViewById(R.id.subject_per_bar);

        //Initialize Others
        recyclerView=(RecyclerView)rootView.findViewById(R.id.recycler_view);
        attFab = (FloatingActionButton) rootView.findViewById(R.id.att_fab);
        marksFab = (FloatingActionButton) rootView.findViewById(R.id.marks_fab);
        //reminderFab = (FloatingActionButton) rootView.findViewById(R.id.reminder_fab);
        floatingMenu = (FloatingActionsMenu) rootView.findViewById(R.id.left_labels);
        dataHandler = DataHandler.getInstance(getActivity());
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        nestedScrollView = (NestedScrollView) rootView.findViewById(R.id.scrollview);
        theme = new MyTheme(getActivity());
        parseTimeTable=new ParseTimeTable(dataHandler.getCoursesList(),dataHandler);
    }

    private void setInit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            subName.setTransitionName(SUB_NAME_ID);
            schoolImage.setTransitionName(IMAGE_NAME_ID);
        }

        //Drawable navIcon= ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back_white_24dp, null);
        //navIcon=new ScaleDrawable(navIcon,0,(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,46.0f,getResources().getDisplayMetrics()),(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,46.0f,getResources().getDisplayMetrics())).getDrawable();
        //navIcon.setBounds(0,0,(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,46.0f,getResources().getDisplayMetrics()),(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,46.0f,getResources().getDisplayMetrics()));
        toolbar.setNavigationIcon(R.drawable.ic_nav_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        //collapsingToolbarLayout.setForeground(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_nav_back,null));
        //collapsingToolbarLayout.setPadding(10,0,0,10);
        myCourse = dataHandler.getCourse(MY_CLASS_NUMBER);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        back_button.setOnClickListener(this);
        marksFab.setOnClickListener(this);
        attFab.setOnClickListener(this);
        //reminderFab.setOnClickListener(this);
        missMinus.setOnClickListener(this);
        missAdd.setOnClickListener(this);
        attendMinus.setOnClickListener(this);
        attendAdd.setOnClickListener(this);
        fullViewTint.setOnClickListener(this);
        floatingMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                fullViewTint.setVisibility(View.VISIBLE);
            }

            @Override
            public void onMenuCollapsed() {
                fullViewTint.setVisibility(View.GONE);
            }
        });
    }

    private void setTransitions() {
        if(Build.VERSION.SDK_INT>=21) {
            setExitTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.explode));
            setReenterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.fade));
            /*((Transition)this.getEnterTransition()).addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    if(Build.VERSION.SDK_INT>=21){
                        Log.d("cir","cir");
                        schoolImage.setVisibility(View.INVISIBLE);
                        circularRevealSchoolImage();
                    }
                    else{
                        schoolImage.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });*/
        }
    }

    private void setFonts() {
        typeface = theme.getMyThemeTypeface();
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
        List<Timing> courseTimings = parseTimeTable.getCourseTimingByParsing(myCourse);
        adapter=new TimingAdapter(getActivity(),courseTimings);
        recyclerView.setAdapter(adapter);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void circularRevealSchoolImage() {
        // previously invisible view


        // get the center for the clipping circle
        int cx = schoolImage.getMeasuredWidth() / 2;
        int cy = schoolImage.getMeasuredHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(schoolImage.getWidth(), schoolImage.getHeight()) / 2;

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(schoolImage, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        schoolImage.setVisibility(View.VISIBLE);
        anim.start();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back_button:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            case R.id.marks_fab:
                floatingMenu.collapseImmediately();
                Intent intent = new Intent(getActivity(), MarksActivity.class);
                intent.putExtra("class_number", MY_CLASS_NUMBER);
                startActivity(intent);
                break;
            case R.id.att_fab:
                floatingMenu.collapseImmediately();
                Intent intent1 = new Intent(getActivity(), AttendanceActivity.class);
                intent1.putExtra("class_number", MY_CLASS_NUMBER);
                startActivity(intent1);
                /*Intent intent1 = new Intent(getActivity(), SampleActivity.class);
                startActivity(intent1);*/
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
            case R.id.full_view_tint:
                if(floatingMenu.isExpanded()){
                    floatingMenu.collapse();
                }
                break;
            default:

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*
        if(Build.VERSION.SDK_INT>=21){
            Log.d("cir","cir");
            schoolImage.setVisibility(View.INVISIBLE);
            circularRevealSchoolImage();
        }
        else{
            schoolImage.setVisibility(View.VISIBLE);
        }
        */
    }

    @Override
    public void onResume() {
        super.onResume();
        //((HomeActivity)getActivity()).hideToolbar();
        HomeActivity h = (HomeActivity) getActivity();
        h.setToolbarFormat(0, "SUBJECT ANALYSIS");
        h.changeStatusBarColor(0);
        theme.refreshTheme();
        collapsingToolbarLayout.setContentScrimColor(theme.getToolbarColorTypedArray().getColor(0, -1));
        setFonts();
    }

    private void reflectAttendance() {
        attendClassText.setText(ATTEND_COUNTS > 1 ? "If you attend " + ATTEND_COUNTS + " classes" : "If you attend " + ATTEND_COUNTS + " class");
        missClassText.setText(MISS_COUNTS > 1 ? "If you miss " + MISS_COUNTS + " classes" : "If you miss " + MISS_COUNTS + " class");
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
            int modAtt = myCourse.getCOURSE_ATTENDANCE().getModifiedPercentage(attClass + ATTEND_COUNTS * mulFactor, totClass + (ATTEND_COUNTS + MISS_COUNTS) * mulFactor);
            attended.setText("attended " + (attClass + ATTEND_COUNTS * mulFactor) + " out " + (totClass + (ATTEND_COUNTS + MISS_COUNTS) * mulFactor));
            subPer.setText(modAtt + " %");
            attBar.setLayoutParams(new LinearLayout.LayoutParams(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17, getResources().getDisplayMetrics()), modAtt));

        }
    }

    public void setSubNameId(String s) {
        SUB_NAME_ID = s;
    }

    public void setImageNameId(String s) {
        IMAGE_NAME_ID = s;
    }


}
