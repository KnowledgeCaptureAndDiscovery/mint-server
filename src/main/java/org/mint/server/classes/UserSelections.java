package org.mint.server.classes;

import java.util.ArrayList;

import org.mint.server.classes.question.ModelingQuestion;

public class UserSelections {
  ModelingQuestion question;
  ArrayList<DataSpecification> data;
  ArrayList<Task> tasks;
  
  public ModelingQuestion getQuestion() {
    return question;
  }
  public void setQuestion(ModelingQuestion question) {
    this.question = question;
  }
  public ArrayList<DataSpecification> getData() {
    return data;
  }
  public void setData(ArrayList<DataSpecification> data) {
    this.data = data;
  }
  public ArrayList<Task> getTasks() {
    return tasks;
  }
  public void setTasks(ArrayList<Task> tasks) {
    this.tasks = tasks;
  }
}
