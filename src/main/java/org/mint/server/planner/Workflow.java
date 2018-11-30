package org.mint.server.planner;

import java.util.ArrayList;

import org.mint.server.classes.URIEntity;
import org.mint.server.classes.graph.Variable;
import org.mint.server.classes.model.Model;

public class Workflow extends URIEntity {
  private static final long serialVersionUID = 4890370333549158851L;

  ArrayList<Model> models;
  ArrayList<Variable> variables; 
  ArrayList<WorkflowLink> links;
  
  public ArrayList<Model> getModels() {
    return models;
  }
  public void setModels(ArrayList<Model> models) {
    this.models = models;
  }
  public ArrayList<Variable> getVariables() {
    return variables;
  }
  public void setVariables(ArrayList<Variable> variables) {
    this.variables = variables;
  }
  public ArrayList<WorkflowLink> getLinks() {
    return links;
  }
  public void setLinks(ArrayList<WorkflowLink> links) {
    this.links = links;
  }
  
}
