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

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.vit.vitio.Extras.SlidingTabLayout;
import io.vit.vitio.HomeActivity;
import io.vit.vitio.Instances.Course;
import io.vit.vitio.Managers.DataHandler;
import io.vit.vitio.R;

/**
 * Created by shalini on 16-06-2015.
 */
public class CoursesFragment extends Fragment {
    public static List<Course> allCoursesList;
    private DataHandler dataHandler;
    private static final int NUM_PAGES = 8;
    private ViewPager pager;
    private SliderAdapter adapter;
    private SlidingTabLayout tabs;
    private Typeface typeface;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.courses_fragment, container, false);
        Log.d("oncreate", "oncrate");
        init(rootView);
        setInit();
        return rootView;
    }


    private void init(ViewGroup rootView) {
        Log.d("initcf", "initcf");
        adapter=new SliderAdapter(getChildFragmentManager());
        pager= (ViewPager) rootView.findViewById(R.id.pager);
        tabs = (SlidingTabLayout) rootView.findViewById(R.id.tabs);
        dataHandler=DataHandler.getInstance(getActivity());
        allCoursesList=dataHandler.getCoursesList();
    }

    private void setInit() {
        Log.d("setinitcf", "setinitcf");
        pager.setAdapter(adapter);
        //tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("onresume", "onresume");
        ((HomeActivity) getActivity()).setToolbarFormat(1);
        ((HomeActivity) getActivity()).changeStatusBarColor(1);
    }

    private class SliderAdapter extends FragmentStatePagerAdapter {
        String[] tabs;

        public SliderAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
            tabs = getResources().getStringArray(R.array.tabs_name_courses);
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
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override

        public int getCount() {
            return NUM_PAGES;
        }
    }

}
