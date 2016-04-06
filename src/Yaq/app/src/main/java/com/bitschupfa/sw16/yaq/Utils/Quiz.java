package com.bitschupfa.sw16.yaq.Utils;

import com.bitschupfa.sw16.yaq.Database.TextQuestion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Quiz implements Serializable {

    List<TextQuestion> questions;

    int current_question;

    public Quiz() {
        questions = new ArrayList<>();
        current_question = 0;
    }

    public void addQuestion(TextQuestion question) {
        questions.add(question);
    }

    public void addQuestions(List questions) {
        questions.addAll(questions);
    }

    public TextQuestion getCurrentQuestion() {
        return questions.get(current_question);
    }

    public void incrementCurrentQuestionCounter() {
        current_question++;
    }

    public int getCurrentQuestionCounter() {
        return current_question;
    }

    public List<TextQuestion> getQuestions() {
        return questions;
    }

    public void shuffleQuestions() {
        Collections.shuffle(questions);
    }

    @Deprecated
    public Quiz createTmpQuiz() {

        questions.add(new TextQuestion("Wann wurde die Mauer in Berlin niedergerissen?", "1989", "1992", "1991", "1990", 1, 1));
        questions.add(new TextQuestion("Wer wurde 2006 Fussball Weltmeister?", "Italien", "Deutschland", "Spanien", "Brasilien", 1, 1));
        questions.add(new TextQuestion("Vor welchem Tieren fürchtete sich Napoleon?", "Katzen", "Hunden", "Spinnen", "Schlangen", 1, 1));
        questions.add(new TextQuestion("Welcher Kontinent ist der Größte?", "Asien", "Europa", "Afrika", "Australien", 1, 1));
        shuffleQuestions();
        return this;
    }
}
