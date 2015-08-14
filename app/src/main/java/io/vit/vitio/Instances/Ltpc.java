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

import io.vit.vitio.Managers.Parsers.ParseResponse;

/**
 * Created by shalini on 21-06-2015.
 */
public class Ltpc {
    private int LECTURE;
    private int TUTORIALS;
    private int PRACTICAL;
    private int CREDITS;
    private String ltpcString;

    public Ltpc(int CREDITS, int TUTORIALS, int PRACTICAL, int LECTURE) {
        this.CREDITS = CREDITS;
        this.TUTORIALS = TUTORIALS;
        this.PRACTICAL = PRACTICAL;
        this.LECTURE = LECTURE;
    }

    public Ltpc(String s){
        if(s!=null) {

            if (s.length() == 4) {
                ltpcString=s;
                this.LECTURE = Integer.parseInt(String.valueOf(s.charAt(0)));
                this.TUTORIALS = Integer.parseInt(String.valueOf(s.charAt(1)));
                this.PRACTICAL = Integer.parseInt(String.valueOf(s.charAt(2)));
                this.CREDITS = Integer.parseInt(String.valueOf(s.charAt(3)));
            }
            else
            {
                this.LECTURE = 0;
                this.TUTORIALS = 0;
                this.PRACTICAL = 0;
                this.CREDITS = 0;
            }
        }
        else
        {
            this.LECTURE = 0;
            this.TUTORIALS = 0;
            this.PRACTICAL = 0;
            this.CREDITS = 0;
        }
    }

    public Ltpc(){
        this.LECTURE = 0;
        this.TUTORIALS = 0;
        this.PRACTICAL = 0;
        this.CREDITS = 0;
    }

    public int getLECTURE(){
        return LECTURE;
    }

    public int getTUTORIALS(){
        return TUTORIALS;
    }

    public int getPRACTICAL(){
        return PRACTICAL;
    }

    public int getCREDITS(){
        return CREDITS;
    }

    public void setLECTURE(int value){
        LECTURE=value;
    }

    public void setTUTORIALS(int value){
        TUTORIALS=value;
    }

    public void setPRACTICAL(int value){
        PRACTICAL=value;
    }

    public void setCREDITS(int value){
        CREDITS=value;
    }

    @Override
    public String toString() {
        return ltpcString;
    }
}
