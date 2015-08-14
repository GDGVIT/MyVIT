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

package io.vit.vitio.Managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.vit.vitio.Instances.Course;
import io.vit.vitio.Instances.Day;
import io.vit.vitio.Instances.Slot;
import io.vit.vitio.Instances.Timing;

/**
 * Created by shalini on 17-06-2015.
 */
public class DataHandler {

    public static List<List<Slot>> NS_THEORY_SLOTS;
    public static List<List<Slot>> NS_LAB_SLOTS;
    public static List<List<Slot>> SS_THEORY_SLOTS;
    public static List<List<Slot>> SS_LAB_SLOTS;

    static {
        NS_THEORY_SLOTS = new ArrayList<>();
        NS_LAB_SLOTS = new ArrayList<>();
        SS_THEORY_SLOTS = new ArrayList<>();
        SS_LAB_SLOTS = new ArrayList<>();
        String ntslots[][] = {
                {"A1", "F1", "C1", "E1", "TD1", "X", "A2", "F2", "C2", "E2", "TD2", "X", "H1", "K1"},
                {"B1", "G1", "D1", "TA1", "TF1", "X", "B2", "G2", "D2", "TA2", "TF2", "X", "H2", "K2"},
                {"C1", "F1", "E1", "TB1", "TG1", "X", "C2", "F2", "E2", "TB2", "TG2", "X", "H3", "K3"},
                {"D1", "A1", "F1", "C1", "TE1", "X", "D2", "A2", "F2", "C2", "TE2", "X", "H4", "K4"},
                {"E1", "B1", "G1", "D1", "TC1", "X", "E2", "B2", "G2", "D2", "TC2", "X", "H5", "K5"},
                {"X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X", "X"}};
        String nlslots[][] = {
                {"L1", "L2", "L3", "L4", "L5", "L6", "L31", "L32", "L33", "L34", "L35", "L36", "L61", "L62"},
                {"L7", "L8", "L9", "L10", "L11", "L12", "L37", "L38", "L39", "L40", "L41", "L42", "L63", "L64"},
                {"L13", "L14", "L15", "L16", "L17", "L18", "L43", "L44", "L45", "L46", "L47", "L48", "L65", "L66"},
                {"L19", "L20", "L21", "L22", "L23", "L24", "L49", "L50", "L51", "L52", "L53", "L54", "L67", "L68"},
                {"L25", "L26", "L27", "L28", "L29", "L30", "L55", "L56", "L57", "L58", "L59", "L60", "L69", "L70"},
                {"L71", "L72", "L73", "L74", "L75", "L76", "L77", "L78", "L79", "L80", "L81", "L82", "X", "X"}};
        String stslots[][] = {
                {"A", "A", "B", "B", "C", "C", "D", "D"},
                {"B", "B", "A", "A", "D", "D", "C", "C"},
                {"A", "A", "B", "B", "C", "C", "D", "D"},
                {"B", "B", "A", "A", "D", "D", "C", "C"},
                {"A", "TA", "B", "TB", "C", "TC", "D", "TD"},
                {"TA", "TA", "TB", "TB", "TC", "TC", "TD", "TD"}};
        String slslots[][] = {
                {"L1", "L1", "L2", "L2", "L13", "L13", "L14", "L14"},
                {"L3", "L3", "L4", "L4", "L15", "L15", "L16", "L16"},
                {"L5", "L5", "L6", "L6", "L17", "L17", "L18", "L18"},
                {"L7", "L7", "L8", "L8", "L19", "L19", "L20", "L20"},
                {"L9", "L9", "L10", "L10", "L21", "L21", "L22", "L22"},
                {"L11", "L11", "L12", "L12", "L23", "L23", "L24", "L24"}};

        String ntstime[] = {"8:00", "9:00", "10:00", "11:00", "12:00", "12:40", "14:00", "15:00", "16:00", "17:00", "18:00", "18:40", "19:00", "20:00"};
        String nlstime[] = {"8:00", "9:00", "10:00", "11:00", "11:50", "12:40", "14:00", "15:00", "16:00", "17:00", "17:50", "18:40", "19:30", "20:20"};
        String ntetime[] = {"8:50", "9:50", "10:50", "11:50", "12:50", "13:30", "14:50", "15:50", "16:50", "17:50", "18:50", "19:30", "19:50", "20:50"};
        String nletime[] = {"8:50", "9:50", "10:50", "11:50", "12:40", "13:30", "14:50", "15:50", "16:50", "17:50", "18:40", "19:30", "20:20", "21:10"};

        String ststime[] = {"8:30", "9:20", "10:30", "11:20", "14:00", "14:50", "16:00", "16:50"};
        String slstime[] = {"8:30", "9:20", "10:30", "11:20", "14:00", "14:50", "16:00", "16:50"};
        String stetime[] = {"9:20", "10:10", "11:20", "12:10", "14:50", "15:40", "16:50", "17:40"};
        String sletime[] = {"9:20", "10:10", "11:20", "12:10", "14:50", "15:40", "16:50", "17:40"};

        for (int i = 0; i < ntslots.length && i < nlslots.length; i++) {
            List<Slot> mapt = new ArrayList<>();
            List<Slot> mapl = new ArrayList<>();
            for (int j = 0; j < ntslots[i].length; j++) {
                Timing timingt = new Timing(new Day(i), ntstime[j], ntetime[j]);
                Timing timingl = new Timing(new Day(i), nlstime[j], nletime[j]);
                Slot t = new Slot(ntslots[i][j], timingt);
                Slot l = new Slot(nlslots[i][j], timingl);
                mapt.add(t);
                mapl.add(l);
            }

            NS_THEORY_SLOTS.add(mapt);
            NS_LAB_SLOTS.add(mapl);
        }

        for (int i = 0; i < stslots.length && i < slslots.length; i++) {
            List<Slot> mapt = new ArrayList<>();
            List<Slot> mapl = new ArrayList<>();
            for (int j = 0; j < stslots[i].length; j++) {
                Timing timingt = new Timing(new Day(i), ststime[j], stetime[j]);
                Timing timingl = new Timing(new Day(i), slstime[j], sletime[j]);
                Slot t = new Slot(stslots[i][j], timingt);
                Slot l = new Slot(slslots[i][j], timingl);
                mapt.add(t);
                mapl.add(l);
            }

            SS_THEORY_SLOTS.add(mapt);
            SS_LAB_SLOTS.add(mapl);
        }

    }


    private Context context;
    private SharedPreferences preferences;
    private ConnectDatabase database;
    private static DataHandler mInstance;

    public DataHandler(Context c) {
        this.context = c;

        database = new ConnectDatabase(context);
        try {
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
        mInstance = this;

    }

    public static DataHandler getInstance(Context context) {
        if (mInstance == null)
            mInstance = new DataHandler(context.getApplicationContext());
        return mInstance;
    }


    private void saveString(String key, String string) {
        preferences.edit().putString(key, string).commit();
    }

    public String getString(String key, String def) {

        return preferences.getString(key, def);
    }


    public void saveFirstTimeUser(String s) {
        saveString("firstTimeUser", s);
    }

    public Boolean getFirstTimeUser() {
        return Boolean.parseBoolean(getString("firstTimeUser", "true"));

    }

    public void saveCampus(String s) {

        saveString("campus", s);
    }

    public String getCampus() {

        return getString("campus", "vellore");
    }

    public void saveRegNo(String s) {
        saveString("regno", s);
    }

    public String getRegNo() {

        return getString("regno", "13XXXYYYY");
    }

    public void saveDOB(String s) {
        saveString("dob", s);
    }

    public String getDOB() {

        return getString("dob", "XXYYZZZZ");
    }

    public void savePhoneNo(String s) {
        saveString("phoneno", s);
    }

    public String getPhoneNo() {

        return getString("phoneno", "XXXXXXXXXX");
    }

    public void saveSemester(String s) {
        saveString("semester", s);
    }

    public String getSemester() {

        return getString("semester", "XX");
    }

    public void saveCourseList(List<Course> courses) {

        if (database != null) {
            database.saveCourses(courses);
        }
    }

    public List<Course> getCoursesList() {

        return database.getCoursesList();
    }

    public Course getCourse(String cn) {
        return database.getCourse(cn);
    }

    public int getCourseCount() {
        return database.getCoursesCount();
    }

    public boolean isDatabaseBuild() {
        Boolean bool = database.check();
        return bool;
    }

    public boolean isWeekend() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.DAY_OF_WEEK) == 1)
            return true;
        else
            return false;
    }

    public void saveGCMRegId(String regId) {
        saveString("gcmRegId",regId);
    }

    public String getGCMRegId(){
        return getString("gcmRegId","");
    }

    public void clearAllData() {
        database.clear();
        saveFirstTimeUser("true");
    }
}
