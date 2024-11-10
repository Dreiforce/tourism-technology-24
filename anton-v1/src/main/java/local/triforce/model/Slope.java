package local.triforce.model;

public class Slope {
    private long peopleOnSlope; // the more people the slower (and less fun)
    private double speed;
    private double funlevel; // modifier to of people drawn to this slope (usually)
    private String from, to;

    public Slope() {
    }

    public Slope(long peopleOnSlope, double speed, double funlevel,  String from, String to) {
        this.peopleOnSlope = peopleOnSlope;
        this.speed = speed;
        this.funlevel = funlevel;
        this.from = from;
        this.to = to;
    }

    public long getPeopleOnSlope() {
        return peopleOnSlope;
    }

    public void setPeopleOnSlope(long peopleOnSlope) {
        this.peopleOnSlope = peopleOnSlope;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getFunlevel() {
        return funlevel;
    }

    public void setFunlevel(double funlevel) {
        this.funlevel = funlevel;
    }
}
