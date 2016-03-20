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

package io.vit.vitio.Settings;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;

import io.vit.vitio.HomeActivity;
import io.vit.vitio.R;
import io.vit.vitio.StartScreens.DetailFragment;
import io.vit.vitio.StartScreens.LoginFragment;

/**
 * Created by shalini on 25-08-2015.
 */
public class ComingSoonActivity extends AppCompatActivity {

        private ViewPager pager;
        private ImageView im1,im2;
        private int NUM_PAGES=2;
        private SliderAdapter adapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.coming_soon_layout);
            init();
            pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
            {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    switch(position){
                        case 0:
                            toggleCircle(im1,new ImageView[]{im2});
                            break;
                        case 1:
                            toggleCircle(im2,new ImageView[]{im1});
                            break;

                    }
                }
            });
            pager.setAdapter(adapter);
            pager.setPadding(40, 0, 40, 0);
            pager.setClipToPadding(false);
            pager.setPageMargin(10);
        }

        private void init() {

            pager= (ViewPager) findViewById(R.id.pager);
            adapter=new SliderAdapter(getSupportFragmentManager());
            im1=(ImageView)findViewById(R.id.c1);
            im2=(ImageView)findViewById(R.id.c2);
            toggleCircle(im1, new ImageView[]{im2});

            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.darkestgray));
            }

        }

        private void toggleCircle(ImageView imon,ImageView imoff[]) {
            imon.setActivated(true);
            ObjectAnimator animatorX=ObjectAnimator.ofFloat(imon,"scaleX",0.5f,1.0f);
            ObjectAnimator animatorY=ObjectAnimator.ofFloat(imon,"scaleY",0.5f,1.0f);
            animatorX.setDuration(300);
            animatorY.setDuration(300);
            AnimatorSet animatorSet= new AnimatorSet();
            animatorSet.playTogether(animatorX, animatorY);
            animatorSet.start();
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
            params.setMargins(0,0,(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13, getResources().getDisplayMetrics()),0);
            imon.setLayoutParams(params);
            for(int i=0; i<imoff.length; i++){
                imoff[i].setActivated(false);
                params=new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics()));
                params.setMargins(0,0,(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13, getResources().getDisplayMetrics()),0);
                imoff[i].setLayoutParams(params);
            }
        }

        private class SliderAdapter extends FragmentStatePagerAdapter {
            ComingSoonFragment lfragment;
            public SliderAdapter(FragmentManager supportFragmentManager) {
                super(supportFragmentManager);
            }
            @Override
            public Fragment getItem(int position) {
                    if(position==0) {
                        lfragment = new ComingSoonFragment();
                        lfragment.setId(0);
                        lfragment.setDisplayImage(R.drawable.coming_soon_friends);
                        return lfragment;
                    }
                else{
                        lfragment = new ComingSoonFragment();
                        lfragment.setId(1);
                        lfragment.setDisplayImage(R.drawable.coming_soon_reminders);
                        return lfragment;
                    }

            /*switch(position){
                case 0:
                    dfragment=new DetailFragment();
                    dfragment.setId(0);
                    dfragment.setDesHead(heads[0]);
                    dfragment.setDesText(texts[0]);
                    dfragment.setDisplayImage(icons.getResourceId(position,-1));
                    return dfragment;

                case 1:
                    dfragment=new DetailFragment();
                    dfragment.setId(1);
                    dfragment.setDesHead(heads[1]);
                    dfragment.setDesText(texts[1]);
                    dfragment.setDisplayImage(icons.getResourceId(position,-1));
                    return dfragment;
                case 2:
                    dfragment=new DetailFragment();
                    dfragment.setId(2);
                    dfragment.setDesHead(heads[2]);
                    dfragment.setDesText(texts[2]);
                    dfragment.setDisplayImage(icons.getResourceId(position,-1));
                    return dfragment;
                case 3:
                    dfragment=new DetailFragment();
                    dfragment.setId(3);
                    dfragment.setDesHead(heads[3]);
                    dfragment.setDesText(texts[3]);
                    dfragment.setDisplayImage(icons.getResourceId(position,-1));
                    return dfragment;
                case 4:
                    dfragment=new DetailFragment();
                    dfragment.setId(4);
                    dfragment.setDesHead(heads[4]);
                    dfragment.setDesText(texts[4]);
                    dfragment.setDisplayImage(icons.getResourceId(position,-1));
                    return dfragment;
                case 5:
                    dfragment=new DetailFragment();
                    dfragment.setId(5);
                    dfragment.setDesHead(heads[5]);
                    dfragment.setDesText(texts[5]);
                    dfragment.setDisplayImage(icons.getResourceId(position,-1));
                    return dfragment;

                case 6:
                    lfragment=new LoginFragment();
                    lfragment.setId(4);
                    return lfragment;
            }*/

            }


            @Override

            public int getCount() {
                return NUM_PAGES;
            }
        }

    }


