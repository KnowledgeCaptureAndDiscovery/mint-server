package org.mint.server.classes.model;

import java.util.ArrayList;

import org.mint.server.classes.URIEntity;

public class ModelIO extends URIEntity {
  private static final long serialVersionUID = 5575376700347352778L;
  
  ArrayList<ModelVariable> variables;

  public ArrayList<ModelVariable> getVariables() {
    return variables;
  }

  public void setVariables(ArrayList<ModelVariable> variables) {
    this.variables = variables;
  }
}
