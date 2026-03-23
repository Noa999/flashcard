package com.flashcard;

public class Card {
    private String question;
    private String answer;
    private int correctCount;
    private int incorrectCount;
    private boolean lastAnswerCorrect;

    public Card(String question, String answer) {
        this.question = question;
        this.answer = answer;
        this.correctCount = 0;
        this.incorrectCount = 0;
        this.lastAnswerCorrect = true;
    }

    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
    public int getCorrectCount() { return correctCount; }
    public int getIncorrectCount() { return incorrectCount; }
    public boolean isLastAnswerCorrect() { return lastAnswerCorrect; }

    public void markCorrect() {
        correctCount++;
        lastAnswerCorrect = true;
    }

    public void markIncorrect() {
        incorrectCount++;
        lastAnswerCorrect = false;
    }
}