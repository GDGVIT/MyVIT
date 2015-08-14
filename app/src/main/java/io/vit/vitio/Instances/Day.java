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

/**
 * Created by shalini on 22-06-2015.
 */
public class Day {
    public static final int[] DAY_NUMBERS = {0, 1, 2, 3, 4, 5};
    public static final String[] DAY_NAMES = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    protected int dayNumber;
    protected String dayName;

    public Day() {
        dayName = DAY_NAMES[0];
        dayNumber = DAY_NUMBERS[0];
    }

    public Day(String name) {
        this.dayName = name;
        this.dayNumber = Arrays.binarySearch(DAY_NAMES, name);
    }

    public Day(int dayNumber) {
        this(DAY_NAMES[dayNumber], dayNumber);
    }


    public Day(String dayName, int dayNumber) {
        this.dayName = dayName;
        this.dayNumber = dayNumber;
    }

    public void setDayName(String value) {
        this.dayName = value;
    }

    public void setDayNumber(int value) {
        this.dayNumber = value;
    }

    public String getDayName() {
        return this.dayName;
    }

    public int getDayNumber() {
        return this.dayNumber;
    }
}
