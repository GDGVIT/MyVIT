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

package io.vit.vitio.StartScreens;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import io.vit.vitio.HomeActivity;
import io.vit.vitio.R;

/**
 * Created by shalini on 15-06-2015.
 */
public class FragmentHolder extends AppCompatActivity {
    private ViewPager pager;
    private ImageView im1, im2, im3, im4, im5, im6, im7;
    private int NUM_PAGES = 7;
    private SliderAdapter adapter;
    private boolean knownUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_fragment_holder);
        init();
        pager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position != 6)
                    //adapter.getCurrentDetailFragment(position).animate();
                    if (Build.VERSION.SDK_INT > 21) {
                        adapter.getCurrentDetailFragment(position).circularRevealSchoolImage();
                    }
                switch (position) {
                    case 0:
                        toggleCircle(im1, new ImageView[]{im2, im3, im4, im5, im6, im7});
                        break;
                    case 1:
                        toggleCircle(im2, new ImageView[]{im1, im3, im4, im5, im6, im7});
                        break;
                    case 2:
                        toggleCircle(im3, new ImageView[]{im2, im1, im4, im5, im6, im7});
                        break;
                    case 3:
                        toggleCircle(im4, new ImageView[]{im2, im3, im1, im5, im6, im7});
                        break;
                    case 4:
                        toggleCircle(im5, new ImageView[]{im2, im3, im4, im1, im6, im7});
                        break;
                    case 5:
                        toggleCircle(im6, new ImageView[]{im2, im3, im4, im1, im5, im7});
                        break;
                    case 6:
                        toggleCircle(im7, new ImageView[]{im2, im3, im4, im1, im6, im5});

                }
            }
        });
        pager.setAdapter(adapter);
        pager.setPadding(40, 0, 40, 0);
        pager.setClipToPadding(false);
        pager.setPageMargin(10);
        if (knownUser) {
            pager.setCurrentItem(adapter.getCount() - 1);
        }
    }

    private void init() {

        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new SliderAdapter(getSupportFragmentManager());
        im1 = (ImageView) findViewById(R.id.c1);
        im2 = (ImageView) findViewById(R.id.c2);
        im3 = (ImageView) findViewById(R.id.c3);
        im4 = (ImageView) findViewById(R.id.c4);
        im5 = (ImageView) findViewById(R.id.c5);
        im6 = (ImageView) findViewById(R.id.c6);
        im7 = (ImageView) findViewById(R.id.c7);
        toggleCircle(im1, new ImageView[]{im2, im3, im4, im5, im6, im7});

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.darkestgray));
        }

        if (getIntent().hasExtra("return")) {
            if (getIntent().getStringExtra("return").equals("logout")) {
                knownUser = true;
            }
        }
    }

    private void toggleCircle(ImageView imon, ImageView imoff[]) {
        imon.setActivated(true);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(imon, "scaleX", 0.5f, 1.0f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(imon, "scaleY", 0.5f, 1.0f);
        animatorX.setDuration(300);
        animatorY.setDuration(300);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorX, animatorY);
        animatorSet.start();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        if (imon != im7)
            params.setMargins(0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13, getResources().getDisplayMetrics()), 0);
        imon.setLayoutParams(params);
        for (int i = 0; i < imoff.length; i++) {
            imoff[i].setActivated(false);
            params = new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics()));
            if (imoff[i] != im7)
                params.setMargins(0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13, getResources().getDisplayMetrics()), 0);
            imoff[i].setLayoutParams(params);
        }
    }

    public void changePage(int fragmentid) {
        if (fragmentid == NUM_PAGES - 1) {
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            pager.setCurrentItem(fragmentid + 1);
        }
    }

    private class SliderAdapter extends FragmentStatePagerAdapter {
        DetailFragment dfragment;
        LoginFragment lfragment;
        String[] heads;
        String[] texts;
        DetailFragment[] detailFragments = {null, null, null, null, null, null};
        TypedArray icons;

        public SliderAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
            heads = getResources().getStringArray(R.array.startheads);
            texts = getResources().getStringArray(R.array.starttexts);
            icons = getResources().obtainTypedArray(R.array.onboarding_icons);
        }

        @Override
        public Fragment getItem(int position) {
            if (position != 6) {
                dfragment = new DetailFragment();
                dfragment.setId(position);
                dfragment.setDesHead(heads[position]);
                dfragment.setDesText(texts[position]);
                dfragment.setDisplayImage(icons.getResourceId(position, -1));
                detailFragments[position] = dfragment;
                return dfragment;
            } else {
                lfragment = new LoginFragment();
                lfragment.setId(4);
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

        public DetailFragment getCurrentDetailFragment(int pos) {
            return detailFragments[pos];
        }

        @Override

        public int getCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
