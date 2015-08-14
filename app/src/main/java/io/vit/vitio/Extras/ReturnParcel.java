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
 * Created by shalini on 22-06-2015.
 */
public class ReturnParcel {
    private int RETURN_CODE;
    private String RETURN_MESSAGE;
    private Object RETURN_PARCEL_OBJECT;

    public ReturnParcel(int code){
        RETURN_CODE=code;
        RETURN_MESSAGE=ErrorDefinitions.getMessage(code);
    }

    public ReturnParcel(){

    }

    public void setRETURN_CODE(int code){
        RETURN_CODE=code;
    }

    public void setRETURN_MESSAGE(String mes){
        RETURN_MESSAGE=mes;
    }

    public void setRETURN_PARCEL_OBJECT(Object object){
        RETURN_PARCEL_OBJECT=object;
    }

    public int getRETURN_CODE(){
        return RETURN_CODE;
    }

    public String getRETURN_MESSAGE(){
        return RETURN_MESSAGE;
    }

    public Object getRETURN_PARCEL_OBJECT(){
        return RETURN_PARCEL_OBJECT;
    }

}
