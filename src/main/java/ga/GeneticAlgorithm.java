package ga;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import domain.entities.Lesson;
import domain.entities.Professor;
import domain.entities.Room;

// Classe principal do Algoritmo Genético para resolver o problema de escalonamento de aulas
public class GeneticAlgorithm {

    // Listas de professores, salas e aulas disponíveis
    private final List<Professor> professors;
    private final List<Room> rooms;
    private final List<Lesson> lessons;
    // Parâmetros do algoritmo genético
    private final int populationSize; // Tamanho da população
    private final int generations;    // Número de gerações
    private final double mutationRate;// Taxa de mutação
    private final Random random = new Random(); // Gerador de números aleatórios

    // Construtor recebe os dados e parâmetros do algoritmo
    public GeneticAlgorithm(List<Professor> professors, List<Room> rooms, List<Lesson> lessons, int populationSize, int generations, double mutationRate) {
        this.professors = professors;
        this.rooms = rooms;
        this.lessons = lessons;
        this.populationSize = populationSize;
        this.generations = generations;
        this.mutationRate = mutationRate;
    }

    // Método principal que executa o algoritmo genético
    public Individual run() {
        List<Individual> population = initializePopulation(); // 1. Inicializa a população

        for (int gen = 0; gen < generations; gen++) {
            population = evolvePopulation(population); // 3. Evolui a população a cada geração
        }

        return getBestIndividual(population); // 7. Retorna o melhor indivíduo encontrado
    }

    // 1. Inicialização da população com cronogramas aleatórios
    private List<Individual> initializePopulation() {
        List<Individual> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(new Individual(generateRandomSchedule())); // Cada indivíduo recebe um cronograma aleatório
        }
        return population;
    }

    // 2. Geração de um cronograma aleatório para um indivíduo
    private List<Lesson> generateRandomSchedule() {
        List<Lesson> schedule = new ArrayList<>();
        for (Lesson lesson : lessons) {
            Lesson copy = lesson.copy(); // Copia a aula para não alterar o original
            copy.professor = randomElement(professors); // Atribui professor aleatório
            copy.room = randomElement(rooms);           // Atribui sala aleatória
            schedule.add(copy);
        }
        return schedule;
    }

    // 3. Evolução da população: seleção, cruzamento e mutação
    private List<Individual> evolvePopulation(List<Individual> population) {
        population.sort(Comparator.comparingInt(Individual::getFitness).reversed()); // Ordena por fitness (aptidão)
        List<Individual> nextGen = new ArrayList<>(population.subList(0, 2)); // Elitismo: mantém os 2 melhores

        while (nextGen.size() < populationSize) {
            Individual parent1 = selectParent(population); // Seleciona pai 1
            Individual parent2 = selectParent(population); // Seleciona pai 2
            Individual child = crossover(parent1, parent2); // 5. Cruzamento: gera filho
            mutate(child); // 6. Mutação: altera aleatoriamente o filho
            nextGen.add(child); // Adiciona filho à próxima geração
        }

        return nextGen;
    }

    // 4. Seleção dos pais: escolhe aleatoriamente entre os 5 melhores
    private Individual selectParent(List<Individual> population) {
        return population.get(random.nextInt(Math.min(5, population.size())));
    }

    // 5. Cruzamento: mistura os cronogramas dos pais para criar um filho
    private Individual crossover(Individual p1, Individual p2) {
        List<Lesson> newSchedule = new ArrayList<>();
        for (int i = 0; i < p1.getSchedule().size(); i++) {
            Lesson lesson = random.nextBoolean() ? p1.getSchedule().get(i) : p2.getSchedule().get(i);
            newSchedule.add(lesson.copy()); // Copia a aula para o novo cronograma
        }
        return new Individual(newSchedule);
    }

    // 6. Mutação: altera aleatoriamente professor ou sala de uma aula
    private void mutate(Individual individual) {
        for (Lesson lesson : individual.getSchedule()) {
            if (random.nextDouble() < mutationRate) {
                lesson.professor = randomElement(professors); // Troca professor
            }
            if (random.nextDouble() < mutationRate) {
                lesson.room = randomElement(rooms); // Troca sala
            }
        }
        individual.recalculateFitness(); // Recalcula a aptidão após mutação
    }

    // 7. Seleção do melhor indivíduo da população
    private Individual getBestIndividual(List<Individual> population) {
        return Collections.max(population, Comparator.comparingInt(Individual::getFitness));
    }

    // Utilitário para obter elemento aleatório de uma lista
    private <T> T randomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }
}
