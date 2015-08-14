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

package io.vit.vitio.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.vit.vitio.R;

/**
 * Created by shalini on 16-06-2015.
 */
public class CampusMapFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.campusmap_fragment, container, false);

        WebView view = (WebView) rootView.findViewById(R.id.img_map);
        view.setVisibility(View.GONE);


        view.getSettings().setBuiltInZoomControls(true);

        view.getSettings().setDisplayZoomControls(false);

        view.getSettings().setLoadWithOverviewMode(true);

        view.getSettings().setUseWideViewPort(true);

        view.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                try {
                    view.setVisibility(View.VISIBLE);
                }catch (Exception ignore){}

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                try {
                }catch (Exception ignore){}

            }
        });

        view.loadUrl("file:///android_asset/vit_map.jpg");


        return rootView;
    }
}
