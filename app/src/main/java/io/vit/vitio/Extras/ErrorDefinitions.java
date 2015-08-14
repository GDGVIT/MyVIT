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

package io.vit.vitio.Extras;

/**
 * Created by shalini on 21-06-2015.
 */
public class ErrorDefinitions {
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_TIMEDOUT = 11;
    public static final int CODE_INVALID = 12;
    public static final int CODE_CAPTCHAPARSING = 13;
    public static final int CODE_TOKENEXPIRED = 14;
    public static final int CODE_NODATA = 15;
    public static final int CODE_DATAPARSING = 16;
    public static final int CODE_TODO = 50;
    public static final int CODE_DEPRECATED = 60;
    public static final int CODE_VITDOWN = 89;
    public static final int CODE_MONGODOWM = 97;
    public static final int CODE_MAINTENANCE = 98;
    public static final int CODE_UNKNOWN = 99;
    public static final int CODE_NETWORK=152;

    public static final String getMessage(int code){
        String mes="" ;
        switch(code){
            case 0:
                mes="Successful Connection";
                break;
            case 11:
                mes="Session Timed Out";
                break;
            case 12:
                mes="Invalid Credentials";
                break;
            case 13:
                mes="Captcha Parsing";
                break;
            case 14:
                mes="Token Expired";
                break;
            case 15:
                mes="No data";
                break;
            case 16:
                mes="Data Parsing";
                break;
            case 50:
                mes="To do";
                break;
            case 60:
                mes="Code is deprecated";
                break;
            case 89:
                mes="VIT servers may be down.Please try again later.";
                break;
            case 97:
                mes="Mongo Down";
                break;
            case 98:
                mes="Code under maintenance";
                break;
            case 99:
                mes="Code is unknown";
                break;
            case 152:
                mes="No Connection";
                break;
            default:
                mes="Unknown Error";
        }
        return mes;
    }
}
