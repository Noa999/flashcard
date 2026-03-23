package com.flashcard;

import java.util.HashSet;
import java.util.Set;

public class AchievementTracker {
    private Set<String> achievements = new HashSet<>();
    private long roundStartTime;
    private boolean allCorrectThisRound = true;

    public void startRound() {
        roundStartTime = System.currentTimeMillis();
        allCorrectThisRound = true;
    }

    public void recordAnswer(Card card, boolean correct) {
        if (!correct) {
            allCorrectThisRound = false;
        }

        // REPEAT: Нэг картад 5-аас олон удаа хариулсан
        if (card.getCorrectCount() + card.getIncorrectCount() > 5) {
            unlock("REPEAT");
        }

        // CONFIDENT: Нэг картад дор хаяж 3 удаа зөв хариулсан
        if (card.getCorrectCount() >= 3) {
            unlock("CONFIDENT");
        }
    }

    public void endRound(int totalCards) {
        long elapsed = System.currentTimeMillis() - roundStartTime;
        double avgSeconds = (double) elapsed / 1000 / totalCards;

        // Хурдан хариулсан амжилт
        if (avgSeconds < 5) {
            unlock("SPEEDY");
        }

        // CORRECT: Бүх карт зөв хариулагдсан
        if (allCorrectThisRound) {
            unlock("CORRECT");
        }
    }

    private void unlock(String name) {
        if (achievements.add(name)) {
            System.out.println("🏆 Амжилт гаргалаа: " + name);
        }
    }

    public Set<String> getAchievements() {
        return achievements;
    }
}
