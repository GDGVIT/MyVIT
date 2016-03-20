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
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import io.vit.vitio.R;

/**
 * Created by shalini on 15-06-2015.
 */
public class ComingSoonFragment extends Fragment {
    private int FRAGMENTID=0;
    private int DISPLAYIMAGERESOURCE=R.mipmap.ic_appicon;
    private ImageView displayImage;

    //private Button nextButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.coming_soon_fragment, container, false);
        displayImage=(ImageView)rootView.findViewById(R.id.detail_image);
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

    private void setData() {

        displayImage.setImageResource(DISPLAYIMAGERESOURCE);
        //nextButton.setText(NEXTBUTTONTXT);
    }

    public void setId(int id){
        FRAGMENTID=id;
    }
    public void setDisplayImage(int rId){
        DISPLAYIMAGERESOURCE=rId;
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
