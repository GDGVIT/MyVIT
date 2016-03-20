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

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import io.vit.vitio.Extras.Themes.MyTheme;
import io.vit.vitio.Extras.TypeFaceSpan;
import io.vit.vitio.HomeActivity;
import io.vit.vitio.Managers.DataHandler;
import io.vit.vitio.R;
import io.vit.vitio.StartScreens.FragmentHolder;

/**
 * Created by shalini on 11-07-2015.
 */
public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private MyTheme myTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        init();
        setToolbar();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainPrefsFragment()).commit();
    }




    private void init() {
        toolbar=(Toolbar)findViewById(R.id.app_bar);
        myTheme=new MyTheme(this);
    }

    private void setToolbar() {

        toolbar.setBackgroundColor(getResources().getColor(R.color.darkgray));
        setSupportActionBar(toolbar);
        SpannableString s = new SpannableString("SETTINGS");
        myTheme.refreshTheme();
        s.setSpan(myTheme.getMyThemeTypeFaceSpan(), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnBack();

            }
        });
        changeStatusBarColor(getResources().getColor(R.color.darkergray));
    }

    /**
     * This fragment shows the preferences for the first header.
     */
    public static class MainPrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Make sure default values are applied.  In a real app, you would
            // want this in a shared function that is used to retrieve the
            // SharedPreferences wherever they are needed.
            //PreferenceManager.setDefaultValues(getActivity(),
              //      R.xml.advanced_preferences, false);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences_headers);

            findPreference("logout").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("Are you sure you want to logout?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                            DataHandler dataHandler = DataHandler.getInstance(getActivity());
                            dataHandler.clearAllData();
                            Intent intent = new Intent(getActivity(), FragmentHolder.class);
                            intent.putExtra("return", "logout");
                            getActivity().finish();
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();


                    return true;
                }
            });

            findPreference("comingsoon").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), ComingSoonActivity.class));

                    return true;
                }
            });


            findPreference("about").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), AboutActivity.class));

                    return true;
                }
            });

            findPreference("source").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), SourcesActivity.class));

                    return true;
                }
            });

            findPreference("feedback").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    startActivity(new Intent(getActivity(), FeedbackActivity.class));

                    return true;
                }
            });

            final MyTheme myTheme=new MyTheme(getActivity());
            final ListPreference themePreference=(ListPreference)findPreference("theme");
            final ListPreference fontPreference=(ListPreference)findPreference("font");
            themePreference.setSummary(myTheme.getMyThemeName());
            SpannableString s = new SpannableString(myTheme.getMyFontName());
            s.setSpan(myTheme.getMyThemeTypeFaceSpan(), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            fontPreference.setSummary(s);
            themePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d("sp", (String) newValue);
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("theme", (String) newValue);
                    editor.commit();
                    Log.d("sp2", preferences.getString("theme", "null"));
                    themePreference.setSummary(myTheme.getMyThemeName(Integer.parseInt((String) newValue)));
                    myTheme.refreshTheme();
                    return false;
                }
            });
            SpannableString entrySpans[]=new SpannableString[fontPreference.getEntries().length];
            for(int i=0;i<fontPreference.getEntries().length;i++){
                entrySpans[i] = new SpannableString(fontPreference.getEntries()[i]);
                entrySpans[i].setSpan(myTheme.getMyThemeTypeFaceSpan(i), 0, entrySpans[i].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            fontPreference.setEntries(entrySpans);
            fontPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Log.d("fp",(String)newValue);
                    SharedPreferences preferences=PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("font",(String)newValue);
                    editor.commit();
                    Log.d("fp2", preferences.getString("font","null"));
                    fontPreference.setSummary(myTheme.getMyFontName(Integer.parseInt((String) newValue)));
                    myTheme.refreshTheme();
                    return false;
                }
            });

        }


    }



    public void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(color);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setBackgroundColor(getResources().getColor(R.color.darkgray));
        SpannableString s = new SpannableString("SETTINGS");
        s.setSpan(myTheme.getMyThemeTypeFaceSpan(), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
    }

    private void returnBack() {
        startActivity(new Intent(this,HomeActivity.class));
        finish();
    }
}