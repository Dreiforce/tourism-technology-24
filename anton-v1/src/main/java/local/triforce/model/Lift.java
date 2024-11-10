package local.triforce.model;

public class Lift {


    private double moveRatePeoplePerHour = 0;
    private String from;
    private String to;

    public Lift() {
    }
    public Lift(double moveRatePeoplePerHour, String from, String to) {
        this.moveRatePeoplePerHour = moveRatePeoplePerHour;
        this.from = from;
        this.to = to;
    }

    public double getMoveRatePeoplePerHour() {
        return moveRatePeoplePerHour;
    }

    public void setMoveRatePeoplePerHour(double moveRatePeoplePerHour) {
        this.moveRatePeoplePerHour = moveRatePeoplePerHour;
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
}
