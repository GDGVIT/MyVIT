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

import android.util.Patterns;

/**
 * Created by shalini on 30-06-2015.
 */
public class Message {

    public static final String OWNER_COE="CoE Spotlight";
    public static final String OWNER_ACADEMICS="Academics/Events Spotlight";
    public static final String OWNER_RESEARCH="Research Spotlight";
    public static final String BASE_URL="http://academics.vit.ac.in/";

    private String OWNER;
    private String URL;
    private String MESSAGE;


    public Message() {
        URL="";
        MESSAGE="-";
    }

    public Message(String MESSAGE, String URL) {
        this.MESSAGE = MESSAGE;
        this.URL = URL.trim();
    }

    public Message(String MESSAGE) {
        this.MESSAGE = MESSAGE;
        this.URL="";
    }


    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL.trim();
    }

    public void formatURL(){
        if(URL.contains("spotlight_file")){
            URL=BASE_URL+URL;
        }
    }

    public boolean hasValidUrl(){
        if(URL.equals("")||URL==null||URL.equals("-")){
            return false;
        }
        else{
            return true;
        }
    }

    public String getOWNER() {
        return OWNER;
    }

    public void setOWNER(String OWNER) {
        this.OWNER = OWNER;
    }


}
