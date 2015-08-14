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
 * Created by shalini on 24-06-2015.
 */
public class Slot {
    private String slotCode;
    private Timing slotTiming;

    public Slot() {
    }

    public Slot(String slotCode, Timing slotTiming) {
        this.slotCode = slotCode;
        this.slotTiming = slotTiming;
    }

    public Timing getSlotTiming() {
        return slotTiming;
    }

    public void setSlotTiming(Timing slotTiming) {
        this.slotTiming = slotTiming;
    }

    public String getSlotCode() {
        return slotCode;
    }

    public void setSlotCode(String slotCode) {
        this.slotCode = slotCode;
    }
}
