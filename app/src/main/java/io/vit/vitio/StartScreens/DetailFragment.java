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

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.vit.vitio.R;

/**
 * Created by shalini on 15-06-2015.
 */
public class DetailFragment extends Fragment {
    private int FRAGMENTID=0;
    private int DISPLAYIMAGERESOURCE=R.mipmap.ic_appicon;
    private ImageView displayImage;
    private TextView desHead,desText;
    private String DESHEAD="",DESTEXT="",NEXTBUTTONTXT="NEXT";
    private Typeface typeface;

    //private Button nextButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.detail_fragment, container, false);

        displayImage=(ImageView)rootView.findViewById(R.id.detail_image);
        desHead=(TextView)rootView.findViewById(R.id.des_head);
        desText=(TextView)rootView.findViewById(R.id.des_text);
        setFonts();
        //nextButton=(Button)rootView.findViewById(R.id.next_button);
       /* nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentHolder)getActivity()).changePage(FRAGMENTID);
            }
        });*/
        setData();

        Log.d(String.valueOf(FRAGMENTID), "oncreateview");
        return rootView;
    }

    //TODO change display display image back to visible in  xml and put slide up animation back for non-lolipop devices
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    void circularRevealSchoolImage() {
        // previously invisible view

        displayImage.setVisibility(View.INVISIBLE);
        final View myView = displayImage;

        // get the center for the clipping circle
        int cx = myView.getMeasuredWidth() / 2;
        int cy = myView.getMeasuredHeight() / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight()) / 2;

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

        // make the view visible and start the animation
        myView.setVisibility(View.VISIBLE);
        anim.start();
    }
    private void setFonts() {
        typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/Montserrat-Regular.ttf");
        desHead.setTypeface(typeface);
        desText.setTypeface(typeface);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(String.valueOf(FRAGMENTID), "onviewcreated");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(String.valueOf(FRAGMENTID), "onresume");
    }

    public void animate(){
        ObjectAnimator animatorY=ObjectAnimator.ofFloat(displayImage,"translationY",200,0);
        ObjectAnimator animatorA=ObjectAnimator.ofFloat(displayImage,"alpha",0,1);
        animatorY.setDuration(200);
        animatorA.setDuration(200);
        animatorY.setInterpolator(new AccelerateInterpolator());
        AnimatorSet animatorSet=new AnimatorSet();
        animatorSet.playTogether(animatorY,animatorA);
        animatorSet.start();
    }
    private void setData() {

        displayImage.setImageResource(DISPLAYIMAGERESOURCE);
        desHead.setText(DESHEAD);
        desText.setText(DESTEXT);
        //animate();
        //nextButton.setText(NEXTBUTTONTXT);
    }

    public void setId(int id){
        FRAGMENTID=id;
    }
    public void setDisplayImage(int rId){
        DISPLAYIMAGERESOURCE=rId;
    }
    public void setDesHead(String str){
        DESHEAD=str;
    }
    public void setDesText(String str){
        DESTEXT=str;
    }
    public void setNextButtonText(int strres){
        NEXTBUTTONTXT=getActivity().getString(strres);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.d(String.valueOf(FRAGMENTID), "onsaveinstancestate");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        Log.d(String.valueOf(FRAGMENTID), "onviewstaterestored");
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(String.valueOf(FRAGMENTID), "onpause");
    }
}
