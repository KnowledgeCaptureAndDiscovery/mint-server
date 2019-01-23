package org.mint.server.classes.rawcag;

import java.util.ArrayList;

public class RawCAG {
  String name;
  String created_by;
  String dateCreated;
  ArrayList<CagVariable> variables;
  ArrayList<CagEdge> edge_data;
  String timeStep;
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getCreated_by() {
    return created_by;
  }
  public void setCreated_by(String created_by) {
    this.created_by = created_by;
  }
  public String getDateCreated() {
    return dateCreated;
  }
  public void setDateCreated(String dateCreated) {
    this.dateCreated = dateCreated;
  }
  public ArrayList<CagVariable> getVariables() {
    return variables;
  }
  public void setVariables(ArrayList<CagVariable> variables) {
    this.variables = variables;
  }
  public ArrayList<CagEdge> getEdge_data() {
    return edge_data;
  }
  public void setEdge_data(ArrayList<CagEdge> edge_data) {
    this.edge_data = edge_data;
  }
  public String getTimeStep() {
    return timeStep;
  }
  public void setTimeStep(String timeStep) {
    this.timeStep = timeStep;
  }
}
