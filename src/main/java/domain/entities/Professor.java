package domain.entities;

import java.util.*;

public class Professor {
    private final String name;
    private final Map<String, Set<String>> skills = new HashMap<>();

    public Professor(String name) { this.name = name; }

    public void addSkill(String language, String level) {
        skills.computeIfAbsent(language, k -> new HashSet<>()).add(level);
    }

    public boolean canTeach(String language, String level) {
        return skills.getOrDefault(language, Collections.emptySet()).contains(level);
    }

    public String getName() { return name; }
}
