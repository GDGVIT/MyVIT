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

import java.util.Arrays;

/*
 * Created by shalini on 22-06-2015.
 */
public class Month {
    public static final int[] MONTH_NUMBERS = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    public static final String[] MONTH_NAMES = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    protected int monthNumber;
    protected String monthName;

    public Month() {
        monthName = MONTH_NAMES[0];
        monthNumber = MONTH_NUMBERS[0];
    }

    public Month(String name) {
        this.monthName = name;
        this.monthNumber = Arrays.binarySearch(MONTH_NAMES, name);
    }

    public Month(int monthNumber) {
        this(MONTH_NAMES[monthNumber], monthNumber);
    }


    public Month(String monthName, int monthNumber) {
        this.monthName = monthName;
        this.monthNumber = monthNumber;
    }

    public void setMonthName(String value) {
        this.monthName = value;
    }

    public void setMonthNumber(int value) {
        this.monthNumber = value;
    }

    public String getMonthName() {
        return this.monthName;
    }

    public int getMonthNumber() {
        return this.monthNumber;
    }
}
