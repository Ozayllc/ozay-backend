package com.ozay.web.rest.dto;

/**
 * Created by naofumiezaki on 6/4/15.
 */
public class DiveLog {
    int totalUnits;
    int activeUnits;

    public int getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(int totalUnits) {
        this.totalUnits = totalUnits;
    }

    public int getActiveUnits() {
        return activeUnits;
    }

    public void setActiveUnits(int activeUnits) {
        this.activeUnits = activeUnits;
    }
}
