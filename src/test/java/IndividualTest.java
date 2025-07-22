// Pacote de teste sugerido: scheduler.test
// Dependência: JUnit 5 (jupiter)

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

// Simulações mínimas das classes necessárias (ideal: importar do projeto real)
class Timeslot {
    final String day, start, end;
    Timeslot(String day, String start, String end) { this.day = day; this.start = start; this.end = end; }
    public boolean equals(Object o) { return o instanceof Timeslot t && day.equals(t.day) && start.equals(t.start) && end.equals(t.end); }
    public int hashCode() { return Objects.hash(day, start, end); }
}

class Room { final String name; Room(String name) { this.name = name; } }

class Professor {
    final String name;
    final Map<String, Set<String>> skills = new HashMap<>();
    Professor(String name) { this.name = name; }
    void addSkill(String lang, String level) { skills.computeIfAbsent(lang, k -> new HashSet<>()).add(level); }
    boolean canTeach(String lang, String level) { return skills.getOrDefault(lang, Set.of()).contains(level); }
}

class Lesson {
    final String group, language, level;
    final Timeslot timeslot;
    Professor professor;
    Room room;
    Lesson(String group, String language, String level, Timeslot timeslot) {
        this.group = group; this.language = language; this.level = level; this.timeslot = timeslot;
    }
    Lesson copy() {
        Lesson l = new Lesson(group, language, level, timeslot);
        l.professor = professor;
        l.room = room;
        return l;
    }
}

class Schedule {
    final List<Lesson> lessons;
    final int fitness;
    Schedule(List<Lesson> lessons) {
        this.lessons = lessons;
        this.fitness = evaluateFitness();
    }
    int evaluateFitness() {
        int conflicts = 0;
        for (int i = 0; i < lessons.size(); i++) {
            Lesson a = lessons.get(i);
            if (a.professor == null || a.room == null || !a.professor.canTeach(a.language, a.level)) conflicts++;
            for (int j = i + 1; j < lessons.size(); j++) {
                Lesson b = lessons.get(j);
                if (a.professor != null && a.professor.equals(b.professor) && a.timeslot.equals(b.timeslot)) conflicts++;
            }
        }
        return -conflicts;
    }
}

public class IndividualTest {

    @Test
    void testFitnessWithConflict() {
        Professor prof = new Professor("Prof A");
        prof.addSkill("Inglês", "Básico");

        Room room = new Room("Sala 1");
        Timeslot ts = new Timeslot("Seg", "08:00", "10:00");

        Lesson l1 = new Lesson("Grupo1", "Inglês", "Básico", ts);
        Lesson l2 = new Lesson("Grupo2", "Inglês", "Básico", ts);
        l1.professor = prof; l2.professor = prof;
        l1.room = room; l2.room = room;

        Schedule schedule = new Schedule(List.of(l1, l2));

        assertTrue(schedule.fitness < 0);
    }

    @Test
    void testFitnessWithoutConflict() {
        Professor prof = new Professor("Prof B");
        prof.addSkill("Inglês", "Básico");
        prof.addSkill("Espanhol", "Avançado");

        Room room = new Room("Sala X");
        Timeslot ts1 = new Timeslot("Seg", "08:00", "10:00");
        Timeslot ts2 = new Timeslot("Seg", "10:00", "12:00");

        Lesson l1 = new Lesson("G1", "Inglês", "Básico", ts1);
        Lesson l2 = new Lesson("G2", "Espanhol", "Avançado", ts2);
        l1.professor = prof; l2.professor = prof;
        l1.room = room; l2.room = room;

        Schedule schedule = new Schedule(List.of(l1, l2));

        assertEquals(0, schedule.fitness);
    }
}
