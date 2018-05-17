package com.winter.dreamhub.api.model;

/**
 * Created by hoaxoan on 8/26/2016.
 */
public class Contract {
    private Integer durationMinutes;
    private String interval;
    private Integer numInstancesPerInterval;
    private Long untilMillisUtc;

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public Integer getNumInstancesPerInterval() {
        return numInstancesPerInterval;
    }

    public void setNumInstancesPerInterval(Integer numInstancesPerInterval) {
        this.numInstancesPerInterval = numInstancesPerInterval;
    }

    public Long getUntilMillisUtc() {
        return untilMillisUtc;
    }

    public void setUntilMillisUtc(Long untilMillisUtc) {
        this.untilMillisUtc = untilMillisUtc;
    }
}
