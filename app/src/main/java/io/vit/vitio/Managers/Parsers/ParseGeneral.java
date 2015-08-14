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

import io.vit.vitio.Extras.ErrorDefinitions;

/**
 * Created by shalini on 21-06-2015.
 */
public class ParseGeneral extends ParseResponse {
    public ParseGeneral(JSONObject json) {
        super(json);
    }

    public ParseGeneral(String res) {
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

    public boolean validateLogin() {
        if (getResponseStatusCode() == ErrorDefinitions.CODE_SUCCESS) {
            return true;
        } else
            return false;
    }



    public String getRefreshedTime() {
        try {
            return mResponseObject.getString("refreshed");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean isCached(){
        try {
            return Boolean.parseBoolean(mResponseObject.getString("cached"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public JSONArray getCoursesJson(){
        try {
            return mResponseObject.getJSONArray("courses");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


}
