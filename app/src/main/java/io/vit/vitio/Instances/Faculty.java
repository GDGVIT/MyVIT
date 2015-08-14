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
 * Created by shalini on 21-06-2015.
 */
public class Faculty {
    private String NAME;
    private String SCHOOL;
    private String facultyString;

    public Faculty(String NAME, String SCHOOL) {
        this.NAME = NAME;
        this.SCHOOL = SCHOOL;
    }

    public Faculty(String s){
        if(s!=null) {
            facultyString=s;
            String arr[]=s.split("-");
            if(arr.length==2){
                this.NAME=arr[0].trim();
                this.SCHOOL=arr[1].trim();
            }
            else
            {
                this.NAME = "N/A";
                this.SCHOOL = "N/A";
            }
        }
        else
        {
            this.NAME = "N/A";
            this.SCHOOL = "N/A";
        }
    }

    public Faculty(Faculty f){
        this.NAME=f.getNAME();
        this.SCHOOL=f.getSCHOOL();
    }

    public Faculty(){
        this.NAME = "N/A";
        this.SCHOOL = "N/A";
    }

    public String getNAME(){
        return NAME;
    }

    public String getSCHOOL(){
        return SCHOOL;
    }


    public void setNAME(String value){
        NAME=value;
    }

    public void setSCHOOL(String value){
        SCHOOL=value;
    }

    @Override
    public String toString() {
        return facultyString;
    }
}
