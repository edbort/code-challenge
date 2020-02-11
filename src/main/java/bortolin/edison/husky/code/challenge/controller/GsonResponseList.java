package bortolin.edison.husky.code.challenge.controller;

import bortolin.edison.husky.code.challenge.util.Exclude;
import java.util.Calendar;
import java.util.List;

public class GsonResponseList<T> {

    @Exclude
    private long startTime;
        
    private long timeInMS;
    
    private int records;
    
    private List<T> result;
    
    private boolean cached;

    public GsonResponseList() {
        this.startTime = Calendar.getInstance().getTimeInMillis();
    }

    public GsonResponseList(boolean cached) {
        this();
        this.cached = cached;
    }
    
    public void setList(List<T> result) {
        this.result = result;
        this.records = result.size();
        this.timeInMS = Calendar.getInstance().getTimeInMillis() - startTime;
    }

    public long getTimeInMS() {
        return timeInMS;
    }

    public int getRecords() {
        return records;
    }

    public List<T> getResult() {
        return result;
    }

    public boolean isCached() {
        return cached;
    }
    
}