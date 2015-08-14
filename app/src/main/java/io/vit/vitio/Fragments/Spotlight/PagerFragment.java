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

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.vit.vitio.Fragments.Courses.CoursesFragment;
import io.vit.vitio.Fragments.SubjectViewFragment;
import io.vit.vitio.HomeActivity;
import io.vit.vitio.Instances.Course;
import io.vit.vitio.Instances.Mark;
import io.vit.vitio.Instances.Message;
import io.vit.vitio.Managers.DataHandler;
import io.vit.vitio.Managers.Parsers.ParseSpotlight;
import io.vit.vitio.Managers.Parsers.ParseTimeTable;
import io.vit.vitio.R;

/**
 * Created by shalini on 28-06-2015.
 */
public class PagerFragment extends Fragment {

    private RecyclerView recyclerView;
    private Typeface typeface;
    private SpotlightListAdapter adapter;
    private int MODE=0;
    private ParseSpotlight parseSpotlight;
    private TextView noContentView;
    private List<Message> myMessages;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.spotlight_pager_fragment, container, false);
        init(rootView);
        setInit();
        setData();
        return rootView;
    }


    private void init(ViewGroup rootView) {
        recyclerView=(RecyclerView)rootView.findViewById(R.id.spotlight_recycler_view);
        typeface = Typeface.createFromAsset(getResources().getAssets(), "fonts/Montserrat-Regular.ttf");
        noContentView=(TextView)rootView.findViewById(R.id.nocontent_text);
        swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh_layout);
    }


    private void setInit() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeRefreshLayout.setRefreshing(false);//this should be false for roatation
                    }
                }, 5000);


                swipeRefreshLayout.setEnabled(true);
                ((SpotlightFragment)getParentFragment()).setData();
            }


        });
    }

    private void setData() {
        if(getArguments().containsKey("mode")) {
            MODE = getArguments().getInt("mode");
            Log.d("mode", String.valueOf(MODE));
        }
        if(SpotlightFragment.parseSpotlight!=null) {
            parseSpotlight=SpotlightFragment.parseSpotlight;
            if(parseSpotlight.getAcademicsSpotlightList()==parseSpotlight.getCoeSpotlightList())
                Log.d("equal","eeual");
            switch (MODE) {
                case 0:
                    Log.d("page","0");
                    myMessages = parseSpotlight.getAcademicsSpotlightList();
                    break;
                case 1:
                    Log.d("page","1");
                    myMessages = parseSpotlight.getCoeSpotlightList();
                    break;
                case 2:
                    Log.d("page","2");
                    myMessages = parseSpotlight.getResearchSpotlightList();
                    break;
                default:
                    myMessages = new ArrayList<>();
            }
        }

        if(myMessages!=null&&myMessages.size()!=0) {
            adapter=new SpotlightListAdapter(getActivity(),myMessages);
            recyclerView.setAdapter(adapter);
            noContentView.setVisibility(TextView.GONE);
            Log.d("my", String.valueOf(MODE)+"*"+myMessages.size());
        }
        else{
            noContentView.setVisibility(TextView.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private class SpotlightListAdapter extends RecyclerView.Adapter<SpotlightListAdapter.SpotlightViewHolder> {
        private final List<Message> data;
        private LayoutInflater inflater;
        private Context c;

        public SpotlightListAdapter(Context context, List<Message> list) {
            data = list;
            c = context;
            inflater = LayoutInflater.from(c);
        }

        @Override
        public SpotlightListAdapter.SpotlightViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.spotlight_pager_recycler_row, parent, false);
            SpotlightViewHolder spotlightViewHolder = new SpotlightViewHolder(view);
            return spotlightViewHolder;
        }


        @Override
        public void onBindViewHolder(SpotlightViewHolder holder, int position) {
            Message info = data.get(position);
            holder.spotlightText.setText(info.getMESSAGE());
            if(info.hasValidUrl()){
                holder.spotlightText.setTextColor(getActivity().getResources().getColor(R.color.fade_blue));
            }
        }

        @Override
        public int getItemCount() {

            return data.size();
        }

        class SpotlightViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            TextView spotlightText,goToUrlText;
            LinearLayout layout, goToUrlBox;

            public SpotlightViewHolder(View itemView) {
                super(itemView);
                spotlightText = (TextView) itemView.findViewById(R.id.spotlight_text);
                //goToUrlText = (TextView) itemView.findViewById(R.id.gotourl_text);
                spotlightText.setTypeface(typeface);
                //goToUrlText.setTypeface(typeface);
                layout = (LinearLayout) itemView.findViewById(R.id.row_holder);
                //goToUrlBox = (LinearLayout) itemView.findViewById(R.id.gotourl_box);
                //goToUrlBox.setOnClickListener(this);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Message mes=data.get(getAdapterPosition());
                if(mes.hasValidUrl()){
                    mes.formatURL();
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mes.getURL()));
                    startActivity(browserIntent);
                }

            }

        }
    }
}
