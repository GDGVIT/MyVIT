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

package io.vit.vitio.Managers.Parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.vit.vitio.Instances.Attendance;
import io.vit.vitio.Instances.Course;
import io.vit.vitio.Instances.Day;
import io.vit.vitio.Instances.Mark;
import io.vit.vitio.Instances.Timing;


/**
 * Created by shalini on 21-06-2015.
 */
public class ParseCourses extends ParseResponse {
    public ParseCourses(JSONObject json) {
        super(json);
    }

    public ParseCourses(String res) {
        super(res);
    }

    @Override
    public String getResponseStatusMessage() {
        return super.getResponseStatusMessage();
    }

    @Override
    public int getResponseStatusCode() {
        return super.getResponseStatusCode();
    }


    public List<Course> getCoursesList() {
        List<Course> courseList = new ArrayList<>();
        try {
            JSONArray coursesArray = mResponseObject.getJSONArray("courses");
            if (coursesArray != null) {
                for (int i = 0; i < coursesArray.length(); i++) {
                    JSONObject c = coursesArray.getJSONObject(i);

                    Course course = getCourse(c);
                    courseList.add(course);

                }
                return courseList;
            } else
                return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Course getCourse(JSONObject c) {
        Course course = new Course(c);
        try {
            course.setCLASS_NUMBER(c.getString("class_number"));
            course.setCOURSE_TITLE(c.getString("course_title"));
            course.setCOURSE_TYPE(c.getString("subject_type"));
            course.setCOURSE_CODE(c.getString("course_code"));
            course.setCOURSE_LTPC(c.getString("ltpc"));
            course.setCOURSE_MODE(c.getString("course_mode"));
            course.setCOURSE_OPTION(c.getString("course_option"));
            course.setCOURSE_SLOT(c.getString("slot"));
            course.setCOURSE_VENUE(c.getString("venue"));
            course.setCOURSE_FACULTY(c.getString("faculty"));
            course.setCOURSE_REGISTRATIONSTATUS(c.getString("registration_status"));
            course.setCOURSE_BILL_DATE(c.getString("bill_date"));
            course.setCOURSE_BILL_NUMBER(c.getString("bill_number"));
            course.setCOURSE_PROJECT_TITLE(c.getString("project_title"));


            course.setCOURSE_ATTENDANCE(getAttendance(c.getJSONObject("attendance")));
            course.setCOURSE_TIMING(getTimings(c.getJSONArray("timings")));
            course.setCOURSE_MARKS(getCouseMarks(c.getJSONObject("marks")));
            course.setCOURSE_TYPE_SHORT(getCourseTypeShort(course));
            return course;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getCourseTypeShort(Course course) {
        if (course != null) {
            if (course.getCOURSE_TYPE() != null) {
                if (course.getCOURSE_TYPE().toUpperCase().contains("LAB")) {
                    return "L";
                } else {
                    return "T";
                }
            } else return "T";
        } else return "T";
    }

    public static List<Mark> getCouseMarks(JSONObject markjson) {
        List<Mark> marks = new ArrayList<>();
        try {
            JSONArray ass = markjson.getJSONArray("assessments");
            Mark mark1 = new Mark(Mark.MARKS_CAT1, ass.getJSONObject(0).getString("scored_marks"), ass.getJSONObject(0).getString(Mark.MARKS_STATUS));
            Mark mark2 = new Mark(Mark.MARKS_CAT2, ass.getJSONObject(1).getString("scored_marks"), ass.getJSONObject(1).getString(Mark.MARKS_STATUS));
            Mark mark3 = new Mark(Mark.MARKS_QUIZ1, ass.getJSONObject(2).getString("scored_marks"), ass.getJSONObject(2).getString(Mark.MARKS_STATUS));
            Mark mark4 = new Mark(Mark.MARKS_QUIZ2, ass.getJSONObject(3).getString("scored_marks"), ass.getJSONObject(3).getString(Mark.MARKS_STATUS));
            Mark mark5 = new Mark(Mark.MARKS_QUIZ3, ass.getJSONObject(4).getString("scored_marks"), ass.getJSONObject(4).getString(Mark.MARKS_STATUS));
            Mark mark6 = new Mark(Mark.MARKS_ASSIGNMENT, ass.getJSONObject(5).getString("scored_marks"), ass.getJSONObject(5).getString(Mark.MARKS_STATUS));
            marks.add(mark1);
            marks.add(mark2);
            marks.add(mark3);
            marks.add(mark4);
            marks.add(mark5);
            marks.add(mark6);
            return marks;

        } catch (JSONException e) {
            //e.printStackTrace();
        }

        return null;
    }

    public static List<Timing> getTimings(JSONArray array) {

        List<Timing> list = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                Timing timing = new Timing();
                timing.setDAY(new Day(Integer.parseInt(object.getString("day"))));
                timing.setEND_TIME(object.getString("end_time"));
                timing.setSTART_TIME((object.getString("start_time")));
                list.add(timing);
            }
            return list;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Attendance getAttendance(JSONObject a) {
        Attendance attendance = new Attendance(a);
        try {
            attendance.setATTENDED_CLASSES(Integer.parseInt(a.getString("attended_classes")));
            attendance.setREGISTRATION_DATE(a.getString("registration_date"));
            attendance.setTOTAL_CLASSES(Integer.parseInt(a.getString("total_classes")));
            attendance.setPERCENTAGE(Integer.parseInt(a.getString("attendance_percentage")));
            attendance.setClasses(getClasses(attendance));
            return attendance;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Attendance.VClass> getClasses(Attendance a) {
        JSONArray array = null;
        List<Attendance.VClass> vclasses = new ArrayList<>();
        try {
            array = a.getJson().getJSONArray("details");
            for (int i = 0; i < array.length(); i++) {
                JSONObject j = array.getJSONObject(i);
                Attendance.VClass vClass = a.instantiateVClass(j);
                vClass.setDATE(j.getString("date"));
                vClass.setSERIAL_NO(Integer.parseInt(j.getString("sl")));
                vClass.setCLASS_UNITS(Integer.parseInt(j.getString("class_units")));
                vClass.setSLOT(j.getString("slot"));
                vClass.setREASON(j.getString("reason"));
                vClass.setSTATUS(j.getString("status"));
                vclasses.add(vClass);
            }
            return vclasses;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
