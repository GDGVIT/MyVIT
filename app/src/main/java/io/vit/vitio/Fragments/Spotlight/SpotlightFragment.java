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

package io.vit.vitio.Fragments.Spotlight;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import io.vit.vitio.Extras.ErrorDefinitions;
import io.vit.vitio.Extras.ReturnParcel;
import io.vit.vitio.Extras.SlidingTabLayout;
import io.vit.vitio.Gcm.ApplicationConstants;
import io.vit.vitio.HomeActivity;
import io.vit.vitio.Managers.AppController;
import io.vit.vitio.Managers.ConnectAPI;
import io.vit.vitio.Managers.DataHandler;
import io.vit.vitio.Managers.Parsers.ParseSpotlight;
import io.vit.vitio.R;

/**
 * Created by shalini on 16-06-2015.
 */
public class SpotlightFragment extends Fragment implements ConnectAPI.RequestListener {

    private DataHandler dataHandler;
    private static final int NUM_PAGES = 3;
    private ViewPager pager;
    protected static ParseSpotlight parseSpotlight;
    private ConnectAPI connectAPI;
    private SliderAdapter adapter;
    private SlidingTabLayout tabs;
    private Typeface typeface;
    private ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.spotlight_fragment, container, false);

        init(rootView);
        setInit();
        setData();
        return rootView;
    }

    private void init(ViewGroup rootView) {
        adapter=new SliderAdapter(getChildFragmentManager());
        pager= (ViewPager) rootView.findViewById(R.id.pager);
        tabs = (SlidingTabLayout) rootView.findViewById(R.id.tabs);
        dataHandler=DataHandler.getInstance(getActivity());
        connectAPI=new ConnectAPI(getActivity());
        dialog=new ProgressDialog(getActivity());
    }

    private void setInit() {
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });


        dialog.setCancelable(true);
        dialog.setTitle("Please Wait");
        dialog.setMessage("Fetching Data...");
        connectAPI.setOnRequestListener(this);

    }


    public void setData() {
        connectAPI.fetchSpotlight();
    }


    @Override
    public void onResume() {
        super.onResume();
        ((HomeActivity) getActivity()).setToolbarFormat(6);
        ((HomeActivity) getActivity()).changeStatusBarColor(6);
    }

    @Override
    public void onRequestInitiated(int code) {
        Log.d("spotlight","reqinti");
        dialog.show();
    }

    @Override
    public void onRequestCompleted(ReturnParcel parcel, int code) {

        Log.d("spotlight","reqcom");
        if(dialog.isShowing()){
            dialog.hide();
        }
        if(parcel.getRETURN_CODE()==ErrorDefinitions.CODE_SUCCESS){
            parseSpotlight=(ParseSpotlight)parcel.getRETURN_PARCEL_OBJECT();
            pager.setAdapter(adapter);
            // Setting the ViewPager For the SlidingTabsLayout
            tabs.setViewPager(pager);
            pager.setCurrentItem(0);
        }
        else{
            Toast.makeText(getActivity(),parcel.getRETURN_MESSAGE(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onErrorRequest(ReturnParcel parcel, int code) {
        if(dialog.isShowing()){
            dialog.hide();
        }
        Toast.makeText(getActivity(),parcel.getRETURN_MESSAGE(),Toast.LENGTH_SHORT).show();
    }


    private class SliderAdapter extends FragmentStatePagerAdapter {
        String[] tabs;

        public SliderAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
            tabs = getResources().getStringArray(R.array.tabs_name_spotlight);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("pos", String.valueOf(position));

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
