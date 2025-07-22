import java.util.ArrayList;
import java.util.List;

import domain.entities.Lesson;
import domain.entities.Professor;
import domain.entities.Room;
import domain.entities.Timeslot;
import ga.GeneticAlgorithm;
import ga.Individual;

public class Timetabling {
    private static final int POPULATION_SIZE = 10;
    private static final int GENERATIONS = 50;
    private static final double MUTATION_RATE = 0.1;

    private static final List<Professor> professors = new ArrayList<>();
    private static final List<Room> rooms = List.of(new Room("101"), new Room("102"));
    private static final List<Lesson> lessons = new ArrayList<>();

    public static void main(String[] args) {
        setupInitialData();

        GeneticAlgorithm ga = new GeneticAlgorithm(professors, rooms, lessons, POPULATION_SIZE, GENERATIONS, MUTATION_RATE);
        Individual best = ga.run();

        System.out.println("\nMelhor escala encontrada:");
        best.getSchedule().forEach(System.out::println);
        System.out.println("Fitness: " + best.getFitness());
    }

    private static void setupInitialData() {
        Professor ana = new Professor("Ana");
        ana.addSkill("Inglês", "C1");
        ana.addSkill("Espanhol", "A2");

        Professor bruno = new Professor("Bruno");
        bruno.addSkill("Inglês", "B1");

        professors.addAll(List.of(ana, bruno));

        lessons.add(new Lesson("Turma1", "Inglês", "B1", new Timeslot("Seg", "08:00", "10:00")));
        lessons.add(new Lesson("Turma2", "Espanhol", "A2", new Timeslot("Ter", "10:00", "12:00")));
    }

//    Fluxo de Execução Completo
//    Geração 0 (Inicial)
//    Individual 1: [Ana/Sala101, Ana/Sala102]     Fitness: 0 (Ana não sabe espanhol)
//    Individual 2: [Bruno/Sala101, Bruno/Sala102] Fitness: -1 (Bruno não sabe espanhol)
//    Individual 3: [Ana/Sala102, Ana/Sala101]     Fitness: 0
//            [...]
//    Individual 10: [Bruno/Sala101, Ana/Sala102]  Fitness: 2 (PERFEITO!)
// =====================================================================
//    Gerações Intermediárias
//    O algoritmo promove os melhores indivíduos:
//    - Indivíduos com fitness alto geram mais descendentes
//    - Mutações ocasionais exploram novas soluções
//    - População gradualmente melhora
//
//    Geração Final (50)
//    Melhores indivíduos convergem para soluções ótimas:
//    Individual 1: [Ana/Sala101, Ana/Sala102]    Fitness: 2
//    Individual 2: [Ana/Sala102, Ana/Sala101]    Fitness: 2
//    Individual 3: [Ana/Sala101, Ana/Sala102]    Fitness: 2
}
