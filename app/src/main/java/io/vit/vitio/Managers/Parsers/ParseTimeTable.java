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

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.vit.vitio.Fragments.TimeTable.TimeTableListInfo;
import io.vit.vitio.Fragments.Today.TodayHeader;
import io.vit.vitio.Instances.Course;
import io.vit.vitio.Instances.Day;
import io.vit.vitio.Instances.Slot;
import io.vit.vitio.Instances.Timing;
import io.vit.vitio.Managers.DataHandler;

/**
 * Created by shalini on 22-06-2015.
 */


public class ParseTimeTable {
    private List<Course> allCourses;
    private List<List<TimeTableListInfo>> allDaysTimeTableList;
    private boolean isParsed = false;
    private DataHandler dataHandler;

    public ParseTimeTable(List<Course> courses, DataHandler dt) {
        allCourses = courses;
        allDaysTimeTableList = new ArrayList<>();
        dataHandler = dt;
    }

    public void parse() {
        List<List<Slot>> slotsTheoryMap;
        List<List<Slot>> slotsLabMap;
        //Log.d("sem", dataHandler.getSemester());
        if (dataHandler.getSemester().equals("SS")) {

            //Log.d("sem", "SS");
            slotsTheoryMap = DataHandler.SS_THEORY_SLOTS;
            slotsLabMap = DataHandler.SS_LAB_SLOTS;
        } else {
            //Log.d("sem", "WS");
            slotsTheoryMap = DataHandler.NS_THEORY_SLOTS;
            slotsLabMap = DataHandler.NS_LAB_SLOTS;
        }

        allDaysTimeTableList = new ArrayList<>();
        for (int i = 0; i < slotsTheoryMap.size(); i++) {
            List<TimeTableListInfo> timeTableListInfos = new ArrayList<>();
            List<Slot> slotTheoryList = slotsTheoryMap.get(i);
            List<Slot> slotLabList = slotsLabMap.get(i);
            for (int j = 0; j < slotTheoryList.size(); j++) {
                Slot slott = slotTheoryList.get(j);
                Slot slotl = slotLabList.get(j);
                TimeTableListInfo timeTableListInfo = new TimeTableListInfo();
                for (int k = 0; k < allCourses.size(); k++) {
                    Course course = allCourses.get(k);
                    String[] courseSlotsStrings = course.getCOURSE_SLOT().split("\\+");
                    //Log.d("lslot", slotl.getSlotCode());
                    //Log.d("tslot", slott.getSlotCode());
                    //Log.d("slot", course.getCOURSE_SLOT());
                    for (int l = 0; l < courseSlotsStrings.length; l++) {
                        //Log.d("infor", courseSlotsStrings[l]);
                        String s = courseSlotsStrings[l];
                        if (slott.getSlotCode().equals(s)) {
                            //Log.d("inif", "inif");
                            timeTableListInfo.name = course.getCOURSE_TITLE();
                            timeTableListInfo.venue = course.getCOURSE_VENUE();
                            timeTableListInfo.time12 = slott.getSlotTiming().toString(Timing.FORMAT12);
                            timeTableListInfo.time24 = slott.getSlotTiming().toString(Timing.FORMAT24);
                            timeTableListInfo.clsnbr = Integer.parseInt(course.getCLASS_NUMBER());
                            timeTableListInfo.per = course.getCOURSE_ATTENDANCE().getPERCENTAGE() + " %";
                            timeTableListInfo.typeShort=course.getCOURSE_TYPE_SHORT();
                            Log.d("typett",course.getCOURSE_TYPE_SHORT());

                            //Skipping similar slot
                            //Skip the first similar slot
                            if (j < slotTheoryList.size() - 1) {
                                if (slott.getSlotCode().equals(slotTheoryList.get(j + 1).getSlotCode()))
                                    continue;
                            }
                            //Attach the next similar slot with start time of previous one
                            if (j > 0) {
                                if (slott.getSlotCode().equals(slotTheoryList.get(j - 1).getSlotCode())) {
                                    Timing newTiming = new Timing(new Day(i), slotTheoryList.get(j - 1).getSlotTiming().getSTART_TIME(), slott.getSlotTiming().getEND_TIME());
                                    timeTableListInfo.time12 = newTiming.toString(Timing.FORMAT12);
                                    timeTableListInfo.time24 = newTiming.toString(Timing.FORMAT24);
                                }
                            }

                            timeTableListInfos.add(timeTableListInfo);
                            break;
                        } else if (slotl.getSlotCode().equals(s)) {
                            //Log.d("inelseif", "inelseif");
                            timeTableListInfo.name = course.getCOURSE_TITLE();
                            timeTableListInfo.venue = course.getCOURSE_VENUE();
                            timeTableListInfo.time12 = slotl.getSlotTiming().toString(Timing.FORMAT12);
                            timeTableListInfo.time24 = slotl.getSlotTiming().toString(Timing.FORMAT24);
                            timeTableListInfo.clsnbr = Integer.parseInt(course.getCLASS_NUMBER());
                            timeTableListInfo.per = course.getCOURSE_ATTENDANCE().getPERCENTAGE() + " %";
                            timeTableListInfo.typeShort= course.getCOURSE_TYPE_SHORT();
                            timeTableListInfos.add(timeTableListInfo);
                            break;
                        }
                    }

                }
            }
            allDaysTimeTableList.add(timeTableListInfos);
        }

    }

    public List<Timing> getCourseTimingByParsing(Course course){
        List<Timing> timingList=new ArrayList<>();
        List<List<Slot>> slotsMap;
        if(course.getCOURSE_TYPE_SHORT().equals("T")&&dataHandler.getSemester().equals("SS")){
            slotsMap=DataHandler.SS_THEORY_SLOTS;
        }
        else if(course.getCOURSE_TYPE_SHORT().equals("L")&&dataHandler.getSemester().equals("SS")){
            slotsMap=DataHandler.SS_LAB_SLOTS;
        }
        else if(course.getCOURSE_TYPE_SHORT().equals("T")){
            slotsMap=DataHandler.NS_THEORY_SLOTS;
        }
        else{
            slotsMap=DataHandler.NS_LAB_SLOTS;
        }

        for (int i = 0; i < slotsMap.size(); i++) {
            List<Slot> slotList = slotsMap.get(i);
            for (int j = 0; j < slotList.size(); j++) {
                Slot slot = slotList.get(j);
                    String[] courseSlotsStrings = course.getCOURSE_SLOT().split("\\+");
                    for (int l = 0; l < courseSlotsStrings.length; l++) {
                        String s = courseSlotsStrings[l];
                        if (slot.getSlotCode().equals(s)) {
                            timingList.add(slot.getSlotTiming());
                        }
                    }

                }
            }
        return timingList;
    }

















    public List<List<TimeTableListInfo>> getFullTimeTable() {
        return allDaysTimeTableList;
    }

    public List<TimeTableListInfo> getTodayTimeTable() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 2;
        if (day != -1)
            return allDaysTimeTableList.get(day);
        else
            return allDaysTimeTableList.get(0);
    }

    public Course getCurrentClass(List<TimeTableListInfo> today) {

        //String dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date());

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        //Log.d("hour", String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        //Log.d("minute", String.valueOf(calendar.get(Calendar.MINUTE)));
        int curTime = (hour * 60) + minute;
        for (int i = 0; i < today.size(); i++) {
            Course c = dataHandler.getCourse(String.valueOf(today.get(i).clsnbr));
            Timing t = Timing.decodeTimingFromString(today.get(i).time24);
            int st = (t.getHours(t.getSTART_TIME()) * 60) + t.getMinutes(t.getSTART_TIME());
            int et = (t.getHours(t.getEND_TIME()) * 60) + t.getMinutes(t.getEND_TIME());
            if (curTime >= st && curTime <= et) {
                return c;
            }
        }

        return null;
    }

    public TodayHeader getNextClass(List<TimeTableListInfo> today) {

        TodayHeader todayHeader = new TodayHeader();

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        //Log.d("hour", String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)));
        //Log.d("minute", String.valueOf(calendar.get(Calendar.MINUTE)));
        int curTime = (hour * 60) + minute;
        //Log.d("curtime", String.valueOf(curTime));
        int min = 0, flag = 0;
        for (int i = 0; i < today.size(); i++) {
            Course c = dataHandler.getCourse(String.valueOf(today.get(i).clsnbr));
            Timing t = Timing.decodeTimingFromString(today.get(i).time24);
            //Log.d("time", today.get(i).time + " start" + t.getSTART_TIME() + " end" + t.getEND_TIME());
            int st = (t.getHours(t.getSTART_TIME()) * 60) + t.getMinutes(t.getSTART_TIME());
            int et = (t.getHours(t.getEND_TIME()) * 60) + t.getMinutes(t.getEND_TIME());
            //Log.d("st", String.valueOf(st));
            //Log.d("min", String.valueOf(min));

            if ((curTime < st && min == 0) || (curTime < st && st <= min)) {
                //Log.d("inif", "iin");
                todayHeader.setCourse(c);
                int diff = st - curTime;
                if (diff <= 60) {
                    todayHeader.setStatus("next in " + diff + "min");
                } else if (diff > 60 && diff < 180) {
                    todayHeader.setStatus("next in " + diff / 60 + "hr " + diff % 60 + " min");
                } else {
                    todayHeader.setStatus(today.get(i).time12);
                }
                min = st;
            }
        }
        if(min==0){
            todayHeader.setCourse(null);
            todayHeader.setStatus("You are done for today!");
        }
        return todayHeader;

    }

    public List<Course> getFilteredCourses(int mode) {

        List<Course> courses=new ArrayList<>();
        for(int i=0;i<allCourses.size();i++){
            switch (mode){
                case 0:
                    return allCourses;
                case 1:
                    if( allCourses.get(i).getBuilding().toUpperCase().equals("SJT")){
                        courses.add(allCourses.get(i));
                    }
                    break;
                case 2:
                    if( allCourses.get(i).getBuilding().toUpperCase().equals("TT")){
                        courses.add(allCourses.get(i));
                    }
                    break;
                case 3:
                    if( allCourses.get(i).getBuilding().toUpperCase().equals("GDN")){
                        courses.add(allCourses.get(i));
                    }
                    break;
                case 4:
                    if( allCourses.get(i).getBuilding().toUpperCase().equals("SMV")){
                        courses.add(allCourses.get(i));
                    }
                    break;
                case 5:
                    if( allCourses.get(i).getBuilding().toUpperCase().equals("MB")){
                        courses.add(allCourses.get(i));
                    }
                    break;
                case 6:
                    if( allCourses.get(i).getBuilding().toUpperCase().equals("CDMM")){
                        courses.add(allCourses.get(i));
                    }
                    break;
                case 7:
                    if( allCourses.get(i).getBuilding().toUpperCase().equals("CBMR")){
                        courses.add(allCourses.get(i));
                    }
                    break;
                default:
                    return allCourses;

            }
        }
        return courses;
    }
}
