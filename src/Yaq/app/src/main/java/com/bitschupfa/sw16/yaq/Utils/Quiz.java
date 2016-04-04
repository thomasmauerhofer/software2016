package com.bitschupfa.sw16.yaq.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Quiz implements Serializable {

    List<Question> questions;

    int current_question;

    public Quiz() {
        questions = new ArrayList<>();
        current_question = 0;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void addQuestions(List questions) {
        questions.addAll(questions);
    }

    public Question getCurrentQuestion() {
        return questions.get(current_question);
    }

    public void incrementCurrentQuestionCounter() {
        current_question++;
    }

    public int getCurrentQuestionCounter() {
        return current_question;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void shuffleQuestions() {
        Collections.shuffle(questions);
    }

    @Deprecated
    public Quiz createTmpQuiz() {

        questions.add(new Question("Wann wurde die Mauer in Berlin niedergerissen?", "1989", "1992", "1991", "1990", "1989"));
        questions.add(new Question("Wer wurde 2006 Fussball Weltmeister?", "Italien", "Deutschland", "Spanien", "Brasilien", "Italien"));
        questions.add(new Question("Vor welchem Tieren fürchtete sich Napoleon?", "Katzen", "Hunden", "Spinnen", "Schlangen", "Katzen"));
        questions.add(new Question("Welcher Kontinent ist der Größte?", "Asien", "Europa", "Afrika", "Australien", "Asien"));
        shuffleQuestions();
        return this;
    }
}
