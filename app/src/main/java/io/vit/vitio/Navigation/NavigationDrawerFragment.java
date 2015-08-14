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

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.vit.vitio.Fragments.CampusMapFragment;
import io.vit.vitio.Fragments.Courses.CoursesFragment;
import io.vit.vitio.Fragments.FriendsFragment;
import io.vit.vitio.Fragments.ReminderFragment;
import io.vit.vitio.Fragments.Spotlight.SpotlightFragment;
import io.vit.vitio.Fragments.TimeTable.TimeTableFragment;
import io.vit.vitio.Fragments.Today.TodayFragment;
import io.vit.vitio.HomeActivity;
import io.vit.vitio.Instances.Course;
import io.vit.vitio.Managers.DataHandler;
import io.vit.vitio.R;
import io.vit.vitio.Settings.SettingsActivity;

/**
 * Created by shalini on 23-02-2015.
 */
public class NavigationDrawerFragment extends Fragment implements NavigationDrawerAdapter.ClickListener, View.OnClickListener {
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private NavigationDrawerAdapter adapter;
    private Toolbar toolbar;
    private TextView headerRegNo, headerSchool, subheaderPer, subheaderleft, navFooterLeftText;
    private LinearLayout subheadAttLeft, subheadAttRight, navFooterSettings;
    private FrameLayout navFooterLeft;
    private HomeActivity home;
    private ActionBarDrawerToggle drawerToggle;
    private ArrayList<String> m;
    private float LAST_TRANSLATE = 0.0f;
    Typeface tf;
    private LinearLayout fl;
    private int lastPosition = 0;

    private DataHandler dataHandler;
    private int CURRENT_FRAGMENT=0;

    public NavigationDrawerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.drawer_fragment, container, false);
        init(rootView);
        headerRegNo.setTypeface(tf);
        headerSchool.setTypeface(tf);
        subheaderleft.setTypeface(tf);
        subheaderPer.setTypeface(tf);
        navFooterLeftText.setTypeface(tf);
        adapter.setClickListener(this);
        navFooterSettings.setOnClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setUserData();
        return rootView;
    }

    private void setUserData() {
        try {
            headerRegNo.setText(dataHandler.getRegNo());
            headerSchool.setText(Course.getSchool(dataHandler.getRegNo()));
            List<Course> courseList = dataHandler.getCoursesList();

            if (courseList != null) {
                int per = 0;
                for (int i = 0; i < courseList.size(); i++) {
                    per = per + courseList.get(i).getCOURSE_ATTENDANCE().getPERCENTAGE();
                }
                per = per / courseList.size();
                subheaderPer.setText(per + "%");
                subheadAttLeft.setLayoutParams(new LinearLayout.LayoutParams(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17, getResources().getDisplayMetrics()), per));
                subheadAttRight.setLayoutParams(new LinearLayout.LayoutParams(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17, getResources().getDisplayMetrics()), 100 - per));
            } else {
                subheaderPer.setText("0%");
                subheadAttLeft.setLayoutParams(new LinearLayout.LayoutParams(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17, getResources().getDisplayMetrics()), 0));
                subheadAttRight.setLayoutParams(new LinearLayout.LayoutParams(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 17, getResources().getDisplayMetrics()), 100));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(ViewGroup rootView) {
        tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Montserrat-Regular.ttf");
        recyclerView = (RecyclerView) rootView.findViewById(R.id.drawer_recycler_view);
        headerRegNo = (TextView) rootView.findViewById(R.id.header_reg_no);
        headerSchool = (TextView) rootView.findViewById(R.id.header_school);
        subheaderPer = (TextView) rootView.findViewById(R.id.subheader_per);
        subheaderleft = (TextView) rootView.findViewById(R.id.subheader_head_left);
        navFooterLeft = (FrameLayout) rootView.findViewById(R.id.nav_footer1);
        navFooterLeftText = (TextView) rootView.findViewById(R.id.nav_footer_left_text);
        subheadAttLeft = (LinearLayout) rootView.findViewById(R.id.subheader_per_left);
        subheadAttRight = (LinearLayout) rootView.findViewById(R.id.subheader_per_right);
        navFooterSettings = (LinearLayout) rootView.findViewById(R.id.nav_footer2);
        fl = (LinearLayout) rootView.findViewById(R.id.drawer_frame);
        adapter = new NavigationDrawerAdapter(getActivity(), getData());
        dataHandler = DataHandler.getInstance(getActivity());
    }


    //TODO ic_calendar icon xxxhdpi , get png from psd
    public List<NavigationDrawerInfo> getData() {
        List<NavigationDrawerInfo> list = new ArrayList<>();
        String[] titles = getActivity().getResources().getStringArray(R.array.drawer_list_titles);
        TypedArray icon = getActivity().getResources().obtainTypedArray(R.array.drawer_icons);
        int[] ico = new int[icon.length()];
        for (int j = 0; j < ico.length; j++) {
            ico[j] = icon.getResourceId(j, -1);
        }
        for (int i = 0; i < titles.length && i < ico.length; i++) {
            NavigationDrawerInfo info = new NavigationDrawerInfo();
            info.name = titles[i];
            info.iconId = ico[i];
            list.add(info);
        }
        return list;

    }

    public void setUp(DrawerLayout dl, Toolbar t, final HomeActivity hm) {
        toolbar = t;
        home = hm;
        drawerLayout = dl;
        drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, t, R.string.draweropen, R.string.drawerclose) {

            @Override
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {

                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                float moveFactor = (fl.getWidth() * slideOffset);
                hm.slideLayout(moveFactor);
            }
        };
        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });
        drawerLayout.setDrawerListener(drawerToggle);
        //drawerLayout.setScrimColor(Color.TRANSPARENT);
    }

    @Override
    public void onRecyclerItemClick(View v, int position) {
       // if (lastPosition != position) {
            final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            //toggleViewActive(0);
            switch (position) {

                case 0:
                    drawerLayout.closeDrawers();
                    Fragment today = new TodayFragment();
                    ft.replace(R.id.main_fragment_holder, today);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);
                    ft.commit();
                    home.setToolbarFormat(0);
                    home.changeStatusBarColor(0);


                    break;
                case 1:
                    drawerLayout.closeDrawers();
                    Fragment courses = new CoursesFragment();
                    ft.replace(R.id.main_fragment_holder, courses);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);
                    ft.commit();
                    home.setToolbarFormat(1);
                    home.changeStatusBarColor(1);

                    break;
                case 2:
                    drawerLayout.closeDrawers();
                    Fragment tt = new TimeTableFragment();
                    ft.replace(R.id.main_fragment_holder, tt);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);
                    ft.commit();
                    home.setToolbarFormat(2);
                    home.changeStatusBarColor(2);
                    break;
                case 3:
                    drawerLayout.closeDrawers();
                    Fragment friends = new FriendsFragment();
                    ft.replace(R.id.main_fragment_holder, friends);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);
                    ft.commit();
                    home.setToolbarFormat(3);
                    home.changeStatusBarColor(3);
                    break;
                case 4:
                    drawerLayout.closeDrawers();
                    Fragment reminders = new ReminderFragment();
                    ft.replace(R.id.main_fragment_holder, reminders);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);
                    ft.commit();
                    home.setToolbarFormat(4);
                    home.changeStatusBarColor(4);
                    break;
                case 5:
                    drawerLayout.closeDrawers();
                    Fragment campus = new CampusMapFragment();
                    ft.replace(R.id.main_fragment_holder, campus);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ft.commit();
                            home.setToolbarFormat(5);
                            home.changeStatusBarColor(5);
                        }
                    }, 250);
                    break;
                case 6:
                    drawerLayout.closeDrawers();
                    Fragment spotlight = new SpotlightFragment();
                    ft.replace(R.id.main_fragment_holder, spotlight);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.addToBackStack(null);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ft.commit();
                            home.setToolbarFormat(6);
                            home.changeStatusBarColor(6);
                        }
                    }, 250);
                    break;
                default:
                    drawerLayout.closeDrawers();
            }
            lastPosition = position;
        /// else {
        //    drawerLayout.closeDrawers();
       // }
    }

    private void toggleViewActive(int pos) {
        View on = recyclerView.getChildAt(pos);
        on.findViewById(R.id.row_holder).setActivated(true);
        for (int i = 0; i < 7; i++) {
            if (i != pos)
                recyclerView.getChildAt(i).findViewById(R.id.row_holder).setActivated(false);
        }
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), SettingsActivity.class));
        drawerLayout.closeDrawers();
    }

    /*public class SwapFragment extends AsyncTask<Fragment,Void,Void>{


        FragmentTransaction ft;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             ft= getActivity().getSupportFragmentManager().beginTransaction();
        }

        @Override
        protected Void doInBackground(Fragment... params) {
            ft.replace(R.id.main_fragment_holder, params[0]);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            home.setToolbarFormat(CURRENT_FRAGMENT);
            home.changeStatusBarColor(CURRENT_FRAGMENT);
        }
    }*/
}
