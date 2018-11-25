package org.mint.server.classes.vocabulary;

import java.util.ArrayList;

import org.mint.server.classes.URIEntity;
import org.mint.server.classes.question.ModelingQuestion;

public class QuestionTemplate extends URIEntity {
  private static final long serialVersionUID = -6540468638453162722L;
  
  ModelingQuestion.Type type;
  ArrayList<String> interventions;
  ArrayList<String> events;
  ArrayList<String> regions;
  ArrayList<Integer> years;

  public ModelingQuestion.Type getType() {
    return type;
  }
  public void setType(ModelingQuestion.Type type) {
    this.type = type;
  }
  public ArrayList<String> getInterventions() {
    return interventions;
  }
  public void setInterventions(ArrayList<String> interventions) {
    this.interventions = interventions;
  }
  public ArrayList<String> getEvents() {
    return events;
  }
  public void setEvents(ArrayList<String> events) {
    this.events = events;
  }
  public ArrayList<String> getRegions() {
    return regions;
  }
  public void setRegions(ArrayList<String> regions) {
    this.regions = regions;
  }
  public ArrayList<Integer> getYears() {
    return years;
  }
  public void setYears(ArrayList<Integer> years) {
    this.years = years;
  }

}
