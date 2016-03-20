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

/**
 * Created by shalini on 22-06-2015.
 */
public class Timing {

    public static int FORMAT12=0;
    public static int FORMAT24=1;
    private Day DAY;
    private String START_TIME;
    private String END_TIME;

    public Timing(Day DAY, String START_TIME, String END_TIME) {
        this.DAY = DAY;
        this.END_TIME = END_TIME;
        this.START_TIME = START_TIME;
    }

    public Timing() {
        this.DAY=new Day();
        this.START_TIME="0";
        this.END_TIME="0";
    }

    public void setDAY(Day day){
        this.DAY=day;
    }

    public void setSTART_TIME(String start_time){
        this.START_TIME=start_time;
    }

    public void setEND_TIME(String end_time){
        this.END_TIME=end_time;
    }

    public Day getDAY(){
        return this.DAY;
    }

    public String getSTART_TIME(){
        return this.START_TIME;
    }

    public String getEND_TIME(){
        return this.END_TIME;
    }

    public String formatTime(String s, int mode){
        int time=Integer.parseInt((s.split(":"))[0]);
        if(mode==FORMAT12) {
            if (time < 12) {
                return s + " AM";
            } else {
                if (time != 12)
                    time = time % 12;
                String pmTime = time + ":" + s.split(":")[1];
                return pmTime + " PM";
            }
        }
        else if(mode==FORMAT24){
            if (time < 12) {
                return s + " AM";
            } else {
                return s + " PM";
            }
        }
        else return ":";
    }

    public String toString(int mode) {
            return formatTime(START_TIME, mode) + " - " + formatTime(END_TIME, mode);
    }

    public int getHours(String time){
        int hours=Integer.parseInt((time.split(":"))[0]);
        return hours;
    }

    public int getMinutes(String time){
        int minutes=Integer.parseInt((time.split(":"))[1]);
        return minutes;
    }

    public static Timing decodeTimingFromString(String s){
        Timing decodedT=new Timing();
        String split[]=s.split("-");
        split[0]=split[0].replaceAll("[A-Za-z]*", "").trim();
        split[1]=split[1].replaceAll("[A-Za-z]*", "").trim();
        decodedT.setSTART_TIME(split[0]);
        decodedT.setEND_TIME(split[1]);
        return decodedT;
    }

    public static Timing decodeRawTimingFromInstance(Timing t){
        Timing decodedT=new Timing();
        t.getSTART_TIME().replaceAll("[A-Za-z]*", "").trim();
        t.getEND_TIME().replaceAll("[A-Za-z]*", "").trim();
        t.setSTART_TIME(t.getSTART_TIME().substring(0,t.getSTART_TIME().indexOf(":",t.getSTART_TIME().indexOf(":")+1)));
        t.setEND_TIME(t.getEND_TIME().substring(0,t.getEND_TIME().indexOf(":",t.getEND_TIME().indexOf(":")+1)));
        return t;
    }
}
