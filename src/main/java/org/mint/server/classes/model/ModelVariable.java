package org.mint.server.classes.model;

import org.mint.server.classes.URIEntity;

public class ModelVariable extends URIEntity {
  private static final long serialVersionUID = 1L;
  
  String units;
  String standard_name;
  int relevance = 999;

  public ModelVariable() {}
  
  public ModelVariable(ModelVariable from) {
    this.setID(from.getID());
    this.setLabel(from.getLabel());
    this.units = from.getUnits();
    this.standard_name = from.getStandard_name();
    this.relevance = from.getRelevance();
  }
  
  public String getStandard_name() {
    return standard_name;
  }
  public void setStandard_name(String standard_name) {
    this.standard_name = standard_name;
  }
  public String getUnits() {
    return units;
  }
  public void setUnits(String units) {
    this.units = units;
  }

  public int getRelevance() {
    return relevance;
  }

  public void setRelevance(int relevance) {
    this.relevance = relevance;
  }
}
