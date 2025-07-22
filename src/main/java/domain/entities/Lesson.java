package domain.entities;

public class Lesson {
    final String group;
    public final String language;
    public final String level;
    public final Timeslot timeslot;
    public Professor professor;
    public Room room;

    public Lesson(String group, String language, String level, Timeslot timeslot) {
        this.group = group;
        this.language = language;
        this.level = level;
        this.timeslot = timeslot;
    }

    public Lesson copy() {
        Lesson clone = new Lesson(group, language, level, timeslot);
        clone.professor = professor;
        clone.room = room;
        return clone;
    }

    @Override public String toString() {
        return String.format("%s (%s %s) - %s - Prof: %s - Sala: %s",
                group, language, level, timeslot,
                professor != null ? professor.getName() : "?",
                room != null ? room.name() : "?");
    }
}
