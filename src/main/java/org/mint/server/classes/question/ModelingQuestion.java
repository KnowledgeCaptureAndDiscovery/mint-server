package org.mint.server.classes.question;

import java.util.ArrayList;

import org.mint.server.classes.URIEntity;
import org.mint.server.classes.question.template.TemplateTimePeriod;

public class ModelingQuestion extends URIEntity {
  private static final long serialVersionUID = -6674193101956139852L;

  public enum QuestionType { DIAGNOSTIC, PROGNOSTIC, COUNTERFACTUAL } ;
  
  QuestionType type;

  String region;
  TemplateTimePeriod timePeriod;
  String intervention;
  
  String graph;
  ArrayList<String> drivingVariables;
  ArrayList<String> responseVariables;
  ArrayList<String> models;
  
  public ModelingQuestion() {
    super();
  }
  
  public ModelingQuestion(String id, String name, QuestionType type) {
    super(id, name);
    this.type = type;
    this.models = new ArrayList<String>();
    this.drivingVariables = new ArrayList<String>();
    this.responseVariables = new ArrayList<String>();
  }

  public QuestionType getType() {
    return type;
  }

  public void setType(QuestionType type) {
    this.type = type;
  }

  public ArrayList<String> getDrivingVariables() {
    return drivingVariables;
  }

  public void setDrivingVariables(ArrayList<String> drivingVariables) {
    this.drivingVariables = drivingVariables;
  }

  public ArrayList<String> getResponseVariables() {
    return responseVariables;
  }

  public void setResponseVariables(ArrayList<String> responseVariables) {
    this.responseVariables = responseVariables;
  }

  public String getGraph() {
    return graph;
  }

  public void setGraph(String graph) {
    this.graph = graph;
  }

  public String getRegion() {
    return region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public TemplateTimePeriod getTimePeriod() {
    return timePeriod;
  }

  public void setTimePeriod(TemplateTimePeriod timePeriod) {
    this.timePeriod = timePeriod;
  }

  public String getIntervention() {
    return intervention;
  }

  public void setIntervention(String intervention) {
    this.intervention = intervention;
  }

  public ArrayList<String> getModels() {
    return models;
  }

  public void setModels(ArrayList<String> models) {
    this.models = models;
  }
}
