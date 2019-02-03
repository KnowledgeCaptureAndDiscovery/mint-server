package org.mint.server.classes.model;

import java.util.ArrayList;

import org.mint.server.classes.URIEntity;

public class ModelIO extends URIEntity {
  private static final long serialVersionUID = 5575376700347352778L;
  
  String type;
  String format;
  int dimensionality;
  ArrayList<ModelVariable> variables;

  public ModelIO(ModelIO from) {
    this.setID(from.getID());
    this.setLabel(from.getLabel());
    this.setType(from.getType());
    this.setDimensionality(from.getDimensionality());
    this.variables = new ArrayList<ModelVariable>();
    for(ModelVariable v : from.getVariables()) {
      this.variables.add(new ModelVariable(v));
    }
  }
  
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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public int getDimensionality() {
    return dimensionality;
  }

  public void setDimensionality(int dimensionality) {
    this.dimensionality = dimensionality;
  }
}
