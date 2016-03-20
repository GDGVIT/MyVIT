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

package io.vit.vitio.Extras.Themes;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import io.vit.vitio.Extras.TypeFaceSpan;
import io.vit.vitio.R;

/**
 * Created by shalini on 23-08-2015.
 */
public class MyTheme {

    public static String MYTHEME_STANDARD = "Standard";
    public static String MYTHEME_CLASSIC = "Classic";
    public static String MYTHEME_INDIGO = "Indigo";
    public static String MYTHEME_MONO = "Mono";

    public static String MYFONT_MONTSERRAT = "Montserrat";
    public static String MYFONT_ROBOTO = "Roboto";
    public static String MYFONT_ROBOTO_ITALIC = "Roboto Italic";
    public static String MYFONT_SEGOE_REGULAR = "Segoe Regular";

    private String themeName, fontName;
    private int themeCode, fontCode;
    private Context appContext;
    private int toolbarArrayCode, statusArrayCode, navBarColor;

    public MyTheme(Context c) {
        appContext = c;
        refreshTheme();
    }

    public void refreshTheme() {
        if (appContext != null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
            String myThemeCode = preferences.getString("theme", "0");
            String myFontCode = preferences.getString("font", "0");
            themeCode = Integer.parseInt(myThemeCode);
            fontCode = Integer.parseInt(myFontCode);
            setArrayCodes();
            themeName = getMyThemeName();
            fontName = getMyFontName();
        }
    }


    public TypedArray getToolbarColorTypedArray() {
        if (appContext != null) {
            TypedArray array = appContext.getResources().obtainTypedArray(toolbarArrayCode);
            return array;
        } else {
            return null;
        }
    }

    public TypedArray getStatusColorTypedArray() {
        if (appContext != null) {
            TypedArray array = appContext.getResources().obtainTypedArray(statusArrayCode);
            return array;
        } else {
            return null;
        }
    }

    public int getMyThemeAccentColor(){
        switch (themeCode) {
            case 0:
                return R.color.darkviolet;
            case 1:
                return R.color.grayaccent;
            case 2:
                return R.color.lime_pd;
            case 3:
                return R.color.grayaccent;
            case 4:
                return R.color.black;
            default:
                return R.color.darkviolet;
        }
    }

    public int getMyThemeSecondaryAccentColor(){
        switch (themeCode) {
            case 0:
                return R.color.violet;
            case 1:
                return R.color.gray2;
            case 2:
                return R.color.lime_p;
            case 3:
                return R.color.gray2;
            case 4:
                return R.color.black;
            default:
                return R.color.violet;
        }
    }

    public Typeface getMyThemeTypeface() {
        if (appContext != null) {
            Typeface typeface;
            switch (fontCode) {
                case 0:
                    typeface = Typeface.createFromAsset(appContext.getResources().getAssets(), "fonts/Montserrat-Regular.ttf");
                    break;
                case 1:
                    typeface = Typeface.createFromAsset(appContext.getResources().getAssets(), "fonts/Roboto-Medium.ttf");
                    break;
                case 2:
                    typeface = Typeface.createFromAsset(appContext.getResources().getAssets(), "fonts/Segoe-Regular.ttf");
                    break;
                case 3:
                    typeface = Typeface.createFromAsset(appContext.getResources().getAssets(), "fonts/Roboto-MediumItalic.ttf");
                    break;
                default:
                    typeface = Typeface.createFromAsset(appContext.getResources().getAssets(), "fonts/Montserrat-Regular.ttf");
            }
            return  typeface;
        }
        else
            return null;
    }

    public TypeFaceSpan getMyThemeTypeFaceSpan() {
        if (appContext != null) {
            TypeFaceSpan typefaceSpan;
            switch (fontCode) {
                case 0:
                    typefaceSpan = new TypeFaceSpan(appContext, "Montserrat-Regular.ttf");
                    break;
                case 1:
                    typefaceSpan = new TypeFaceSpan(appContext, "Roboto-Medium.ttf");
                    break;
                case 2:
                    typefaceSpan = new TypeFaceSpan(appContext, "Segoe-Regular.ttf");
                    break;
                case 3:
                    typefaceSpan = new TypeFaceSpan(appContext, "Roboto-MediumItalic.ttf");
                    break;
                default:
                    typefaceSpan = new TypeFaceSpan(appContext, "Montserrat-Regular.ttf");
            }
            return  typefaceSpan;
        }
        else
            return null;
    }

    public TypeFaceSpan getMyThemeTypeFaceSpan(int fcode) {
        if (appContext != null) {
            TypeFaceSpan typefaceSpan;
            switch (fcode) {
                case 0:
                    typefaceSpan = new TypeFaceSpan(appContext, "Montserrat-Regular.ttf");
                    break;
                case 1:
                    typefaceSpan = new TypeFaceSpan(appContext, "Roboto-Medium.ttf");
                    break;
                case 2:
                    typefaceSpan = new TypeFaceSpan(appContext, "Segoe-Regular.ttf");
                    break;
                case 3:
                    typefaceSpan = new TypeFaceSpan(appContext, "Roboto-MediumItalic.ttf");
                    break;
                default:
                    typefaceSpan = new TypeFaceSpan(appContext, "Montserrat-Regular.ttf");
            }
            return  typefaceSpan;
        }
        else
            return null;
    }

    private void setArrayCodes() {
        if (appContext != null) {
            switch (themeCode) {
                case 0:
                    toolbarArrayCode = R.array.toolbar_colors_standard;
                    statusArrayCode = R.array.statusbar_colors_standard;
                    break;
                case 1:
                    toolbarArrayCode = R.array.toolbar_colors_classic;
                    statusArrayCode = R.array.statusbar_colors_classic;
                    break;
                case 2:
                    toolbarArrayCode = R.array.toolbar_colors_glass;
                    statusArrayCode = R.array.statusbar_colors_glass;
                    break;
                case 3:
                    toolbarArrayCode = R.array.toolbar_colors_mono;
                    statusArrayCode = R.array.statusbar_colors_mono;
                    break;
                default:
                    toolbarArrayCode = R.array.toolbar_colors_standard;
                    statusArrayCode = R.array.statusbar_colors_standard;
            }
        }
    }

    public String getMyThemeName() {
        switch (themeCode) {
            case 0:
                return MYTHEME_STANDARD;
            case 1:
                return MYTHEME_CLASSIC;
            case 2:
                return MYTHEME_INDIGO;
            case 3:
                return MYTHEME_MONO;
            default:
                return MYTHEME_STANDARD;
        }
    }


    public String getMyThemeName(int tcode) {
        switch (tcode) {
            case 0:
                return MYTHEME_STANDARD;
            case 1:
                return MYTHEME_CLASSIC;
            case 2:
            return MYTHEME_INDIGO;
            case 3:
            return MYTHEME_MONO;
            default:
            return MYTHEME_STANDARD;
        }
    }

    public String getMyFontName() {

        switch (fontCode) {
            case 0:
                return MYFONT_MONTSERRAT;
            case 1:
                return MYFONT_ROBOTO;
            case 2:
                return MYFONT_SEGOE_REGULAR;
            case 3:
                return MYFONT_ROBOTO_ITALIC;
            default:
                return MYFONT_MONTSERRAT;
        }
    }

    public String getMyFontName(int fcode) {

        switch (fcode) {
            case 0:
                return MYFONT_MONTSERRAT;
            case 1:
                return MYFONT_ROBOTO;
            case 2:
                return MYFONT_SEGOE_REGULAR;
            case 3:
                return MYFONT_ROBOTO_ITALIC;
            default:
                return MYFONT_MONTSERRAT;
        }
    }

    public int getMyThemeCode() {
        return themeCode;
    }

    public int getMyFontCode() {
        return fontCode;
    }
}
