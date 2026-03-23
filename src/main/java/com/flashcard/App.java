package com.flashcard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        if (args.length == 0 || hasFlag(args, "--help")) {
            printHelp();
            return;
        }

        String cardsFile = args[0];
        String order = getOption(args, "--order", "random");
        int repetitions = Integer.parseInt(getOption(args, "--repetitions", "1"));
        boolean invertCards = hasFlag(args, "--invertCards");

        List<Card> cards = loadCards(cardsFile, invertCards);
        CardOrganizer organizer = getOrganizer(order);
        AchievementTracker tracker = new AchievementTracker();
        Scanner scanner = new Scanner(System.in);

        for (Card card : cards) {
            int correct = 0;
            while (correct < repetitions) {
                tracker.startRound();
                System.out.println("\nАсуулт: " + card.getQuestion());
                System.out.print("Таны хариулт: ");
                String answer = scanner.nextLine().trim();

                if (answer.equalsIgnoreCase(card.getAnswer())) {
                    System.out.println("✅ Зөв!");
                    card.markCorrect();
                    correct++;
                } else {
                    System.out.println("❌ Буруу! Зөв хариулт: " + card.getAnswer());
                    card.markIncorrect();
                }
                tracker.recordAnswer(card, answer.equalsIgnoreCase(card.getAnswer()));
                tracker.endRound(cards.size());
            }
        }

        cards = organizer.organize(cards);
        System.out.println("\n🎉 Дууслаа!");
        scanner.close();
    }

    private static List<Card> loadCards(String filename, boolean invert) throws Exception {
        List<Card> cards = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue;
            String[] parts = line.split("\\|");
            if (parts.length == 2) {
                String q = parts[0].trim();
                String a = parts[1].trim();
                cards.add(invert ? new Card(a, q) : new Card(q, a));
            }
        }
        reader.close();
        return cards;
    }

    private static CardOrganizer getOrganizer(String order) {
        switch (order) {
            case "worst-first": return new WorstFirstSorter();
            case "recent-mistakes-first": return new RecentMistakesFirstSorter();
            default: return new RandomSorter();
        }
    }

    private static boolean hasFlag(String[] args, String flag) {
        for (String arg : args) {
            if (arg.equals(flag)) return true;
        }
        return false;
    }

    private static String getOption(String[] args, String option, String defaultVal) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(option)) return args[i + 1];
        }
        return defaultVal;
    }

    private static void printHelp() {
        System.out.println("Хэрэглэх заавар:");
        System.out.println("  flashcard <cards-file> [options]");
        System.out.println();
        System.out.println("Сонголтууд:");
        System.out.println("  --help                    Тусламж харуулах");
        System.out.println("  --order <order>           random | worst-first | recent-mistakes-first");
        System.out.println("  --repetitions <num>       Зөв хариулах шаардлагатай удаа");
        System.out.println("  --invertCards             Асуулт, хариултыг солих");
    }
}