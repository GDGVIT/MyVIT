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

package io.vit.vitio.Managers;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.vit.vitio.Extras.ErrorDefinitions;
import io.vit.vitio.Extras.ReturnParcel;
import io.vit.vitio.Managers.Parsers.ParseGeneral;
import io.vit.vitio.Managers.Parsers.ParseResponse;
import io.vit.vitio.Managers.Parsers.ParseCourses;
import io.vit.vitio.Managers.Parsers.ParseSpotlight;

/**
 * Created by shalini on 18-06-2015.
 */
public class ConnectAPI {

    //Constants
    public static final int SERVERTEST_CODE=0;
    public static final int LOGIN_CODE=1;
    public static final int REFRESH_CODE=2;
    public static final int GENERIC_CODE=-1;
    //Initialize HTTP URLs
    private final String VELLORE_LOGIN_URL="http://vitacademics-rel.herokuapp.com/api/v2/vellore/login";
    private final String CHENNAI_LOGIN_URL="http://vitacademics-rel.herokuapp.com/api/v2/chennai/login";
    private final String VELLORE_REFRESH_URL="http://vitacademics-rel.herokuapp.com/api/v2/vellore/refresh";
    private final String CHENNAI_REFRESH_URL="http://vitacademics-rel.herokuapp.com/api/v2/chennai/refresh";
    private final String SERVERTEST_URL="http://vitacademics-rel.herokuapp.com/api/v2/system";
    private final String SPOTLIGHT_URL="http://facademics-test.appspot.com/spotlight";


    private DataHandler dataHandler;
    private Context mContext;
    private AppController appController;
    private RequestListener mListener;

    public ConnectAPI(Context context){
        mContext=context;
        dataHandler=DataHandler.getInstance(mContext);
        appController=AppController.getInstance();
    }

    public void serverTest(){
        if(mListener!=null) {
            mListener.onRequestInitiated(SERVERTEST_CODE);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    SERVERTEST_URL.trim(), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("response","recieved");
                    if (response != null) {
                        ParseResponse parseResponse = new ParseResponse(response);
                        ReturnParcel parcel = new ReturnParcel(parseResponse.getResponseStatusCode());
                        mListener.onRequestCompleted(parcel,SERVERTEST_CODE);
                    } else {
                        ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NODATA);
                        mListener.onErrorRequest(parcel,SERVERTEST_CODE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NETWORK);
                    mListener.onErrorRequest(parcel,SERVERTEST_CODE);
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(8000,
                    2,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            appController.addToRequestQueue(jsonObjectRequest, "servertest");
        }
        else{
            return;
        }
    }

    public void login(){
        if(mListener!=null) {


            mListener.onRequestInitiated(LOGIN_CODE);

            String url;
            if (dataHandler.getCampus().equals("vellore")) {
                url = VELLORE_LOGIN_URL;
            } else {
                url = CHENNAI_LOGIN_URL;
            }
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url.trim(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("loginresponse",response);
                    if (response != null) {
                        ParseGeneral parseGeneral = new ParseGeneral(response);
                        ReturnParcel parcel = new ReturnParcel(parseGeneral.getResponseStatusCode());
                        parcel.setRETURN_PARCEL_OBJECT(parseGeneral);
                        if(parseGeneral.validateLogin()){
                            mListener.onRequestCompleted(parcel,LOGIN_CODE);
                        }
                        else{
                            mListener.onErrorRequest(parcel,LOGIN_CODE);
                        }

                    } else {
                        ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NODATA);
                        mListener.onErrorRequest(parcel,LOGIN_CODE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NETWORK);
                    mListener.onErrorRequest(parcel,LOGIN_CODE);
                }
            })
            {



                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Log.d("getParams", "called");
                    Map<String ,String> postMap=new HashMap<>();
                    postMap.put("regno",dataHandler.getRegNo());
                    postMap.put("dob",dataHandler.getDOB());
                    postMap.put("mobile",dataHandler.getPhoneNo());

                    return postMap;
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000,
                    2,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            appController.addToRequestQueue(stringRequest, "loginrequest");
        }
        else{
            return;
        }
    }

       public void refresh(){
        if(mListener!=null) {
            mListener.onRequestInitiated(REFRESH_CODE);
            String url;
            if (dataHandler.getCampus().equals("vellore")) {
                url = VELLORE_REFRESH_URL;
            } else
                url = CHENNAI_REFRESH_URL;

            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    url.trim(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {

                        Log.d("refresh","response");
                        ParseCourses parseCourses = new ParseCourses(response);
                        ReturnParcel parcel = new ReturnParcel(parseCourses.getResponseStatusCode());
                        parcel.setRETURN_PARCEL_OBJECT(parseCourses);
                        if(parseCourses.getResponseStatusCode()==ErrorDefinitions.CODE_SUCCESS) {
                            dataHandler.saveCourseList(parseCourses.getCoursesList());
                            dataHandler.saveSemester(parseCourses.getSemester());
                        }
                        mListener.onRequestCompleted(parcel, REFRESH_CODE);
                    } else {
                        ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NODATA);
                        mListener.onErrorRequest(parcel,REFRESH_CODE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NETWORK);
                    mListener.onErrorRequest(parcel,REFRESH_CODE);
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String ,String> postMap=new HashMap<>();
                    postMap.put("regno",dataHandler.getRegNo());
                    postMap.put("dob",dataHandler.getDOB());
                    postMap.put("mobile",dataHandler.getPhoneNo());

                    return postMap;
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("Content-Type","application/x-www-form-urlencoded");
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000,
                    2,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            appController.addToRequestQueue(stringRequest, "refreshrequest");
        }
        else{
            return;
        }
    }

    public void fetchSpotlight(){
        if(mListener!=null) {
            mListener.onRequestInitiated(GENERIC_CODE);
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    SPOTLIGHT_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("sres",response);
                    if (response != null) {
                        ParseSpotlight parseSpotlight= new ParseSpotlight(response);
                        parseSpotlight.parse();
                        ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_SUCCESS);
                        parcel.setRETURN_PARCEL_OBJECT(parseSpotlight);
                        mListener.onRequestCompleted(parcel, GENERIC_CODE);
                    } else {
                        ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NODATA);
                        mListener.onErrorRequest(parcel,GENERIC_CODE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    ReturnParcel parcel = new ReturnParcel(ErrorDefinitions.CODE_NETWORK);
                    mListener.onErrorRequest(parcel,GENERIC_CODE);
                }
            });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            appController.addToRequestQueue(stringRequest, "spotlightrequest");
        }
        else{
            return;
        }
    }


    public void setOnRequestListener(RequestListener listener){
        this.mListener=listener;
    }

    public void changeOnRequestListener(RequestListener listener){
        this.mListener=listener;
    }

    public interface RequestListener{

        public void onRequestInitiated(int code);

        public void onRequestCompleted(ReturnParcel parcel, int code);

        public void onErrorRequest(ReturnParcel parcel, int code);

    }

}
