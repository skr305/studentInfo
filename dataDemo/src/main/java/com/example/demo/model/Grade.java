package com.example.demo.model;

public class Grade {
    public String id;
    public String cours;
    public int grade;

    public String getCours() {
        return cours;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCours(String cours) {
        this.cours = cours;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}