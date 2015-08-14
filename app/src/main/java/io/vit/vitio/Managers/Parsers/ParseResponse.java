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

import org.json.JSONException;
import org.json.JSONObject;

import io.vit.vitio.Extras.ErrorDefinitions;

/**
 * Created by shalini on 21-06-2015.
 */
public class ParseResponse {
    protected int ERROR_STATUS = 0;
    protected JSONObject mResponseObject;
    protected String mResponseString;
    protected int RESPONSE_CODE = 9999;

    public ParseResponse(JSONObject json) {
        mResponseObject = json;
        ERROR_STATUS = 1;
    }

    public ParseResponse(String res) {
        mResponseString = res;
        try {
            mResponseObject = new JSONObject(res);
            ERROR_STATUS = 1;
        } catch (JSONException e) {
            e.printStackTrace();
            ERROR_STATUS = 0;
        }
    }

    public int getResponseStatusCode() {
        if (ERROR_STATUS == 1) {
            try {
                RESPONSE_CODE = Integer.parseInt(mResponseObject.getJSONObject("status").getString("code"));
                return RESPONSE_CODE;
            } catch (JSONException e) {
                e.printStackTrace();
                ERROR_STATUS = 0;
                RESPONSE_CODE = ErrorDefinitions.CODE_NODATA;
                return RESPONSE_CODE;
            }
        } else {
            RESPONSE_CODE = ErrorDefinitions.CODE_NODATA;
            return ErrorDefinitions.CODE_NODATA;
        }

    }

    public String getResponseStatusMessage() {
        if (RESPONSE_CODE == 9999) {
            getResponseStatusCode();
        }
        return ErrorDefinitions.getMessage(RESPONSE_CODE);
    }

    public String getSemester() {
        try {
            return mResponseObject.getString("semester");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}
