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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.vit.vitio.Instances.Course;
import io.vit.vitio.Managers.Parsers.ParseCourses;

/**
 * Created by saurabh on 5/17/14.
 */
public class ConnectDatabase extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "vitioDatabase";

    // Contacts table name
    private static final String TABLE_COURSES = "courses";

    // Contacts Table Columns names
    private static final String KEY_CLASNBR = "course_class_number";
    private static final String KEY_TITLE = "course_title";
    private static final String KEY_SLOT = "course_slot";
    private static final String KEY_TYPE = "course_type";
    private static final String KEY_TYPE_SHORT = "course_type_short";
    private static final String KEY_LTPC = "course_ltpc";
    private static final String KEY_CODE = "course_code";
    private static final String KEY_MODE = "course_mode";
    private static final String KEY_OPTION = "course_option";
    private static final String KEY_VENUE = "course_venue";
    private static final String KEY_FACULTY = "course_faculty";
    private static final String KEY_REGISTRATIONSTATUS = "course_registrationstatus";
    private static final String KEY_BILL_DATE = "course_date";
    private static final String KEY_BILL_NUMBER = "course_bill_number";
    private static final String KEY_PROJECT_TITLE = "course_project_title";
    private static final String KEY_COURSE_JSON = "course_json";
    private static final String KEY_ATTENDANCE = "course_attendance";
    private static final String KEY_TIMINGS = "course_timings";
    private static final String KEY_MARKS = "course_marks";

    private static final String[] COLUMNS = {KEY_CLASNBR, KEY_TITLE, KEY_SLOT, KEY_TYPE, KEY_TYPE_SHORT,
            KEY_LTPC, KEY_CODE, KEY_MODE, KEY_OPTION, KEY_VENUE, KEY_FACULTY, KEY_REGISTRATIONSTATUS,
            KEY_BILL_DATE, KEY_BILL_NUMBER, KEY_PROJECT_TITLE, KEY_COURSE_JSON,
            KEY_ATTENDANCE, KEY_TIMINGS, KEY_MARKS};

    public ConnectDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_SUBJECTS_TABLE = "CREATE TABLE " + TABLE_COURSES + "("
                + KEY_CLASNBR + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_SLOT + " TEXT," + KEY_TYPE + " TEXT," + KEY_TYPE_SHORT + " TEXT," + KEY_LTPC + " TEXT,"
                + KEY_CODE + " TEXT," + KEY_MODE + " TEXT," + KEY_OPTION + " TEXT,"
                + KEY_VENUE + " TEXT," + KEY_FACULTY + " TEXT," + KEY_REGISTRATIONSTATUS + "  TEXT,"
                + KEY_BILL_DATE + " TEXT," + KEY_BILL_NUMBER + " TEXT," + KEY_PROJECT_TITLE + " TEXT,"
                + KEY_COURSE_JSON + " TEXT," + KEY_ATTENDANCE + " TEXT," + KEY_TIMINGS + " TEXT," + KEY_MARKS + " TEXT" + ");";
        sqLiteDatabase.execSQL(CREATE_SUBJECTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
            onCreate(sqLiteDatabase);
    }

    public void saveCourses(List<Course> courses) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();


            for (int i = 0; i < courses.size(); i++) {
                Course course = courses.get(i);

                ContentValues values = new ContentValues();

                values.put(COLUMNS[0], course.getCLASS_NUMBER());
                values.put(COLUMNS[1], course.getCOURSE_TITLE());
                values.put(COLUMNS[2], course.getCOURSE_SLOT());
                values.put(COLUMNS[3], course.getCOURSE_TYPE());
                values.put(COLUMNS[4], course.getCOURSE_TYPE_SHORT());
                Log.d("type", course.getCOURSE_TYPE_SHORT());
                values.put(COLUMNS[5], course.getCOURSE_LTPC().toString());
                values.put(COLUMNS[6], course.getCOURSE_CODE());
                values.put(COLUMNS[7], course.getCOURSE_MODE());
                values.put(COLUMNS[8], course.getCOURSE_OPTION());
                values.put(COLUMNS[9], course.getCOURSE_VENUE());
                values.put(COLUMNS[10], course.getCOURSE_FACULTY().toString());
                values.put(COLUMNS[11], course.getCOURSE_REGISTRATIONSTATUS());
                values.put(COLUMNS[12], course.getCOURSE_BILL_DATE());
                values.put(COLUMNS[13], course.getCOURSE_BILL_NUMBER());
                values.put(COLUMNS[14], course.getCOURSE_PROJECT_TITLE());
                values.put(COLUMNS[15], course.getCOURSE_JSON().toString());
                values.put(COLUMNS[16], course.getCOURSE_ATTENDANCE().getJson().toString());
                values.put(COLUMNS[17], course.getCOURSE_JSON().getJSONArray("timings").toString());
                values.put(COLUMNS[18], course.getCOURSE_JSON().getJSONObject("marks").toString());
                //db.insertWithOnConflict(TABLE_COURSES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                if(check()) {
                    Log.d("update","check()");
                    //onUpgrade(db,db.getVersion(),192564);
                    db.replace(TABLE_COURSES,null,values);
                    //db.update(TABLE_COURSES, values, null, null);
                }
                else {
                    Log.d("insert","check()");
                    db.insert(TABLE_COURSES, null, values);
                }
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            SQLiteDatabase _db = this.getWritableDatabase();
            if (_db != null && _db.isOpen()) {
                _db.close();
            }
        }

    }

    public Course getCourse(String classNmbr) {
        Course course = new Course();
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.query(TABLE_COURSES, COLUMNS, KEY_CLASNBR + "=?", new String[]{String.valueOf(classNmbr)}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            course.setCLASS_NUMBER(cursor.getString(0));
            course.setCOURSE_TITLE(cursor.getString(1));
            course.setCOURSE_SLOT(cursor.getString(2));
            course.setCOURSE_TYPE(cursor.getString(3));
            course.setCOURSE_TYPE_SHORT(cursor.getString(4));
            course.setCOURSE_LTPC(cursor.getString(5));
            course.setCOURSE_CODE(cursor.getString(6));
            course.setCOURSE_MODE(cursor.getString(7));
            course.setCOURSE_OPTION(cursor.getString(8));
            course.setCOURSE_VENUE(cursor.getString(9));
            course.setCOURSE_FACULTY(cursor.getString(10));
            course.setCOURSE_REGISTRATIONSTATUS(cursor.getString(11));
            course.setCOURSE_BILL_DATE(cursor.getString(12));
            course.setCOURSE_BILL_NUMBER(cursor.getString(13));
            course.setCOURSE_PROJECT_TITLE(cursor.getString(14));
            course.setJson(new JSONObject(cursor.getString(15)));
            course.setCOURSE_ATTENDANCE(ParseCourses.getAttendance(new JSONObject(cursor.getString(16))));
            course.setCOURSE_TIMING(ParseCourses.getTimings(new JSONArray(cursor.getString(17))));
            course.setCOURSE_MARKS(ParseCourses.getCouseMarks(new JSONObject(cursor.getString(18))));


            cursor.close();
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
            SQLiteDatabase _db = this.getWritableDatabase();
            if (_db != null && _db.isOpen()) {
                _db.close();
            }
        }


        return course;
    }

    public int getCoursesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_COURSES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        return count;
    }

    public List<Course> getCoursesList() {
        List<Course> courses = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_COURSES;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    Course course = new Course();
                    course.setCLASS_NUMBER(cursor.getString(0));
                    course.setCOURSE_TITLE(cursor.getString(1));
                    course.setCOURSE_SLOT(cursor.getString(2));
                    course.setCOURSE_TYPE(cursor.getString(3));
                    course.setCOURSE_TYPE_SHORT(cursor.getString(4));
                    course.setCOURSE_LTPC(cursor.getString(5));
                    course.setCOURSE_CODE(cursor.getString(6));
                    course.setCOURSE_MODE(cursor.getString(7));
                    course.setCOURSE_OPTION(cursor.getString(8));
                    course.setCOURSE_VENUE(cursor.getString(9));
                    course.setCOURSE_FACULTY(cursor.getString(10));
                    course.setCOURSE_REGISTRATIONSTATUS(cursor.getString(11));
                    course.setCOURSE_BILL_DATE(cursor.getString(12));
                    course.setCOURSE_BILL_NUMBER(cursor.getString(13));
                    course.setCOURSE_PROJECT_TITLE(cursor.getString(14));
                    course.setJson(new JSONObject(cursor.getString(15)));
                    course.setCOURSE_ATTENDANCE(ParseCourses.getAttendance(new JSONObject(cursor.getString(16))));
                    course.setCOURSE_TIMING(ParseCourses.getTimings(new JSONArray(cursor.getString(17))));
                    course.setCOURSE_MARKS(ParseCourses.getCouseMarks(new JSONObject(cursor.getString(18))));
                    courses.add(course);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            SQLiteDatabase _db = this.getWritableDatabase();
            if (_db != null && _db.isOpen()) {
                _db.close();
            }
        }
        return courses;
    }

    public Boolean check() {
        String countQuery = "SELECT  * FROM " + TABLE_COURSES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor == null || cursor.moveToFirst() == false) {
            return false;
        } else
            return true;
    }

    public void clear(){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        String deleteQuery="DELETE FROM "+TABLE_COURSES+";";
        sqLiteDatabase.delete(TABLE_COURSES,"1",null);
        Cursor cursor = sqLiteDatabase.rawQuery(deleteQuery, null);
        cursor.close();
        sqLiteDatabase.close();
    }
}
