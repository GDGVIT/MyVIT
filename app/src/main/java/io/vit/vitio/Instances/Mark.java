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
public class Mark {
    public static String MARKS_CAT1="cat1";
    public static String MARKS_CAT2="cat2";
    public static String MARKS_STATUS="status";
    public static String MARKS_QUIZ1="quiz1";
    public static String MARKS_QUIZ2="quiz2";
    public static String MARKS_QUIZ3="quiz3";
    public static String MARKS_ASSIGNMENT="assignment";

    private String MARK;
    private String STATUS;
    private String EXAM_NAME;

    public Mark(){
        MARK="-";
        STATUS="";
        EXAM_NAME="";
    }

    public Mark(String EXAM_NAME, String MARK, String STATUS) {
        this.EXAM_NAME = EXAM_NAME;
        this.MARK = MARK;
        this.STATUS = STATUS;
    }

    public Mark(Mark m) {
        this.MARK=m.MARK;
        this.STATUS=m.STATUS;
        this.EXAM_NAME=m.EXAM_NAME;
    }

    public String getEXAM_NAME() {
        return EXAM_NAME;
    }

    public void setEXAM_NAME(String EXAM_NAME) {
        this.EXAM_NAME = EXAM_NAME;
    }

    public String getMARK() {
        return MARK;
    }

    public void setMARK(String MARK) {
        this.MARK = MARK;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }
}
