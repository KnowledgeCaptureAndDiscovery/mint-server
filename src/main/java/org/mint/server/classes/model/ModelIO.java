package org.mint.server.classes.model;

import java.util.ArrayList;

import org.mint.server.classes.URIEntity;

public class ModelIO extends URIEntity {
  private static final long serialVersionUID = 5575376700347352778L;
  
  ArrayList<ModelVariable> variables;

  public ModelIO(String id) {
    super(id);
    this.variables = new ArrayList<ModelVariable>();
  }
  
  public ArrayList<ModelVariable> getVariables() {
    return variables;
  }

  public void setVariables(ArrayList<ModelVariable> variables) {
    this.variables = variables;
  }
  
  public void addVariable(ModelVariable variable) {
    this.variables.add(variable);
  }
}
