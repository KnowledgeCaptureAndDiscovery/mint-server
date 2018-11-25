package org.mint.server.classes;

import java.util.ArrayList;
import java.util.HashMap;

public class Task extends URIEntity {
  private static final long serialVersionUID = 862990933978159716L;
  public enum Status { ONGOING, DONE, FAILED };

  String type;
  Status status;
  String question;
  ArrayList<String> output;
  HashMap<String, Activity> activities;
  
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public Status getStatus() {
    return status;
  }
  public void setStatus(Status status) {
    this.status = status;
  }
  public String getQuestion() {
    return question;
  }
  public void setQuestion(String question) {
    this.question = question;
  }
  public ArrayList<String> getOutput() {
    return output;
  }
  public void setOutput(ArrayList<String> output) {
    this.output = output;
  }
  public HashMap<String, Activity> getActivities() {
    return activities;
  }
  public void setActivities(HashMap<String, Activity> activities) {
    this.activities = activities;
  }
}
