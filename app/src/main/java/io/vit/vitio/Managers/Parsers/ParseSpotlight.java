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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.vit.vitio.Instances.Message;

/**
 * Created by shalini on 21-06-2015.
 */
public class ParseSpotlight {

    private JSONArray myJsonArray;
    private List<Message> coeSpotlightList, academicsSpotlightList, researchspotlightList;
    private boolean VALIDITY = true;

    protected ParseSpotlight(JSONArray json) {
        myJsonArray = json;
        VALIDITY = true;
    }

    public ParseSpotlight(String res) {
        try {
            JSONArray array = new JSONArray(res);
            myJsonArray = array;
        } catch (JSONException e) {
            e.printStackTrace();
            VALIDITY = false;
        }

    }

    public boolean parse() {
        coeSpotlightList = new ArrayList<>();
        academicsSpotlightList = new ArrayList<>();
        researchspotlightList = new ArrayList<>();
        JSONArray inarray = null;
        try {
            if (VALIDITY) {
                for (int i = 0; i < myJsonArray.length(); i++) {
                    JSONObject object = myJsonArray.getJSONObject(i);
                    switch (object.getString("source")) {
                        case Message.OWNER_COE:
                            inarray = object.getJSONArray("content");
                            Log.d("owner", "coe");
                            for (int j = 0; j < inarray.length(); j++) {
                                JSONObject inobject = inarray.getJSONObject(j);
                                Message message = new Message();
                                message.setOWNER(Message.OWNER_COE);
                                message.setMESSAGE(inobject.getString("message"));
                                message.setURL(inobject.getString("url"));
                                Log.d("message", message.getMESSAGE());
                                Log.d("url", message.getURL());
                                coeSpotlightList.add(message);
                            }
                            break;
                        case Message.OWNER_ACADEMICS:
                            Log.d("owner", "acad");
                            inarray = object.getJSONArray("content");
                            for (int j = 0; j < inarray.length(); j++) {
                                JSONObject inobject = inarray.getJSONObject(j);
                                Message message = new Message();
                                message.setOWNER(Message.OWNER_ACADEMICS);
                                message.setMESSAGE(inobject.getString("message"));
                                message.setURL(inobject.getString("url"));
                                Log.d("message", message.getMESSAGE());
                                Log.d("url", message.getURL());
                                academicsSpotlightList.add(message);
                            }
                            break;
                        case Message.OWNER_RESEARCH:
                            Log.d("owner", "res");
                            inarray = object.getJSONArray("content");
                            for (int j = 0; j < inarray.length(); j++) {
                                JSONObject inobject = inarray.getJSONObject(j);
                                Message message = new Message();
                                message.setOWNER(Message.OWNER_RESEARCH);
                                message.setMESSAGE(inobject.getString("message"));
                                message.setURL(inobject.getString("url"));
                                Log.d("message", message.getMESSAGE());
                                Log.d("url", message.getURL());
                                researchspotlightList.add(message);
                            }
                            break;
                        default:
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Message> getCoeSpotlightList() {
        return coeSpotlightList;
    }

    public List<Message> getAcademicsSpotlightList() {
        return academicsSpotlightList;
    }

    public List<Message> getResearchSpotlightList() {
        return researchspotlightList;
    }

}
