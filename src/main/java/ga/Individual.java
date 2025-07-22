package ga;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import domain.entities.Lesson;

// Indivíduo: Representa uma grade horária completa com todas as aulas atribuídas a professores e salas.

public class Individual {
    // Lista de aulas que compõem o cronograma deste indivíduo
    private final List<Lesson> schedule;
    // Valor de fitness (aptidão) do indivíduo
    private int fitness;

    // Construtor: recebe uma lista de aulas e calcula o fitness inicial
    public Individual(List<Lesson> schedule) {
        this.schedule = schedule;
        this.fitness = calculateFitness();
    }

    // Retorna o cronograma de aulas deste indivíduo
    public List<Lesson> getSchedule() {
        return schedule;
    }

    // Retorna o valor de fitness atual
    public int getFitness() {
        return fitness;
    }

    // Recalcula o fitness, útil após alterações no cronograma
    public void recalculateFitness() {
        fitness = calculateFitness();
    }

    // Calcula o fitness do indivíduo baseado em regras de alocação
    private int calculateFitness() {
        // Inicializa a pontuação de fitness começa em zero e será ajustado conforme as regras.
        int score = 0;
        // Conjunto para verificar conflitos de sala/horário - controla quais salas estão ocupadas por horário.
        Set<String> roomAssignments = new HashSet<>();
        // Conjunto para verificar conflitos de professor/horário - controla os horários dos professores.
        Set<String> professorAssignments = new HashSet<>();

        // Percorre todas as aulas do cronograma
        for (Lesson lesson : schedule) {
            boolean valid = true;

            // Verifica se o professor pode lecionar a disciplina e nível
            if (lesson.professor == null || !lesson.professor.canTeach(lesson.language, lesson.level)) {
                valid = false; // Professor inválido
            } else {
                score++; // Professor válido, incrementa pontuação
            }

            // Cria identificadores únicos para sala/horário e professor/horário
            String roomSlot = lesson.room + "@" + lesson.timeslot;
            String profSlot = lesson.professor + "@" + lesson.timeslot;

            // Verifica se já existe aula na mesma sala/horário
            if (!roomAssignments.add(roomSlot)) valid = false;
            // Verifica se o professor já está alocado nesse horário
            if (!professorAssignments.add(profSlot)) valid = false;

            /* Penaliza conflitos ou alocações inválidas 
                - Se qualquer verificação falhar (professor não qualificado, sala ou professor em conflito), subtrai 2 pontos.
                - Isso força a grade a evitar sobreposição e manter professores corretos.
            */
            if (!valid) score -= 2;
        }

        // Retorna o valor final de fitness
        return score;
    }
}
