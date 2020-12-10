package com.example.finalgroupproject;

public class Event {
    private String eventTitle;
    private String startDate;
    private double max;
    private double min;
    private String URL;
    protected long id;
    public Event(){

    }
    public Event(String eventTitle, String startDate, double min,double max, String URL, long id) {
        this.eventTitle = eventTitle;
        this.startDate = startDate;
        this.min = min;
        this.max = max;
        this.URL = URL;
        this.id = id;
    }

    public void setID(long id){this.id=id;}
    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }
    public void setMin(double min) {
        this.min = min;
    }
    public long getId(){
        return this.id;
    }
}
