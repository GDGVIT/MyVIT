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

package io.vit.vitio.Instances;

import android.util.Log;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by shalini on 21-06-2015.
 */
public class Attendance {
    private String REGISTRATION_DATE;
    private int ATTENDED_CLASSES;
    private int TOTAL_CLASSES;
    private int PERCENTAGE;
    private List<VClass> classes;

    private JSONObject ATTENDANCE_JSON;

    public Attendance() {

    }

    public Attendance(JSONObject object) {
        ATTENDANCE_JSON = object;
    }

    public Attendance(JSONObject json, int ATTENDED_CLASSES, int TOTAL_CLASSES, List<VClass> classes, int PERCENTAGE, String REGISTRATION_DATE) {
        this.ATTENDANCE_JSON = json;
        this.ATTENDED_CLASSES = ATTENDED_CLASSES;
        this.classes = classes;
        this.PERCENTAGE = PERCENTAGE;
        this.REGISTRATION_DATE = REGISTRATION_DATE;
        this.TOTAL_CLASSES = TOTAL_CLASSES;
    }

    public Attendance(int ATTENDED_CLASSES, int TOTAL_CLASSES, List<VClass> classes, int PERCENTAGE, String REGISTRATION_DATE) {

        this.ATTENDED_CLASSES = ATTENDED_CLASSES;
        this.classes = classes;
        this.PERCENTAGE = PERCENTAGE;
        this.REGISTRATION_DATE = REGISTRATION_DATE;
        this.TOTAL_CLASSES = TOTAL_CLASSES;
    }

    public Attendance(int ATTENDED_CLASSES, int TOTAL_CLASSES, List<VClass> classes, int PERCENTAGE) {

        this.ATTENDED_CLASSES = ATTENDED_CLASSES;
        this.classes = classes;
        this.PERCENTAGE = PERCENTAGE;
        this.REGISTRATION_DATE = "";
        this.TOTAL_CLASSES = TOTAL_CLASSES;
    }


    public void setATTENDED_CLASSES(int value) {
        this.ATTENDED_CLASSES = value;
    }

    public void setREGISTRATION_DATE(String value) {
        this.REGISTRATION_DATE = value;
    }

    public void setTOTAL_CLASSES(int value) {
        this.TOTAL_CLASSES = value;
    }

    public void setPERCENTAGE(int value) {
        this.PERCENTAGE = value;
    }

    public void setClasses(List<VClass> classes) {
        this.classes = classes;
    }

    public void setJson(JSONObject object) {
        this.ATTENDANCE_JSON = object;
    }

    public void addClass(VClass vClass) {
        this.classes.add(vClass);
    }


    public String getREGISTRATION_DATE() {
        return this.REGISTRATION_DATE;
    }

    public int getATTENDED_CLASSES() {
        return this.ATTENDED_CLASSES;
    }

    public int getTOTAL_CLASSES() {
        return this.TOTAL_CLASSES;
    }

    public int getPERCENTAGE() {
        return this.PERCENTAGE;
    }

    public List<VClass> getClasses() {
        return this.classes;
    }

    public JSONObject getJson() {
        return this.ATTENDANCE_JSON;
    }

    public VClass instantiateVClass(JSONObject j) {
        return new VClass(j);
    }

    public int getModifiedPercentage(int attended, int total) {
        if (total != 0) {
            return (int)Math.ceil(((double) attended / total) * 100f);
        }
        return 0;
    }

    public class VClass {
        private final String STATUS_PRESENT = "Present";
        private final String STATUS_ABSENT = "Absent";
        private final String STATUS_ONDUTY = "On Duty";


        private int SERIAL_NO;
        private String DATE;
        private String SLOT;
        private String STATUS;
        private int CLASS_UNITS;
        private String REASON;
        private JSONObject VCLASS_JSON;

        public VClass() {

        }

        public VClass(JSONObject object) {
            VCLASS_JSON = object;
        }

        public void setJson(JSONObject object) {
            this.VCLASS_JSON = object;
        }

        public void setSERIAL_NO(int value) {
            this.SERIAL_NO = value;
        }

        public void setDATE(String value) {
            this.DATE = value;
        }

        public void setSLOT(String value) {
            this.SLOT = value;
        }

        public void setSTATUS(String value) {
            this.STATUS = value;
        }

        public void setCLASS_UNITS(int value) {
            this.CLASS_UNITS = value;
        }

        public void setREASON(String value) {
            this.REASON = value;
        }

        public int getSERIAL_NO() {
            return this.SERIAL_NO;
        }

        public String getDATE() {
            return this.DATE;
        }

        public String getSLOT() {
            return this.SLOT;
        }

        public String getSTATUS() {
            return this.STATUS;
        }

        public int getCLASS_UNITS() {
            return this.CLASS_UNITS;
        }

        public String getREASON() {
            return this.REASON;
        }

        public int getDay() {
            Date date = new Date();
            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
            try {
                String d[]=DATE.split("-");
                //d[1]= String.valueOf(Integer.parseInt(d[1])-1);
                Log.d("lenght",d[1]+"*"+d[1].length());
                if(d[1].length()==1){
                    d[1]="0"+d[1];
                }
                date = date_format.parse(d[0]+"-"+d[1]+"-"+d[2]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            Log.d("day", String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)));
            return calendar.get(Calendar.DAY_OF_WEEK)-2;
        }

        public String getFormattedDate(){
            String d[]=DATE.split("-");
            Month month=new Month(Integer.parseInt(d[1])-1);
            return d[2]+" "+month.getMonthName();
        }

        public JSONObject getJson() {
            return this.VCLASS_JSON;
        }
    }
}
