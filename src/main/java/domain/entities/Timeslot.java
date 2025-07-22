package domain.entities;

public record Timeslot(String day, String start, String end) {
    @Override public String toString() { return day + " " + start + " - " + end; }
}