package org.mint.server.classes.graph;

import java.util.ArrayList;

import org.mint.server.classes.URIEntity;

public class GVariable extends URIEntity {
  private static final long serialVersionUID = -1065248491858616282L;
  
  ArrayList<String> standard_names;
  GraphPosition position;
  String category;
  boolean added;

  public GVariable() {}
  
  public GVariable(GVariable gvar) {
    this.copyFrom(gvar);
  }
  public ArrayList<String> getStandard_names() {
    return standard_names;
  }
  public void setStandard_names(ArrayList<String> standard_names) {
    this.standard_names = standard_names;
  }
  public GraphPosition getPosition() {
    return position;
  }
  public void setPosition(GraphPosition position) {
    this.position = position;
  }
  public String getCategory() {
    return category;
  }
  public void setCategory(String category) {
    this.category = category;
  }
  public boolean isAdded() {
    return added;
  }
  public void setAdded(boolean added) {
    this.added = added;
  }

  public void copyFrom(GVariable v) {
    this.setID(v.getID());
    this.setLabel(v.getLabel());
    this.setPosition(v.getPosition());
    this.setCategory(v.getCategory());
    this.setStandard_names(v.getStandard_names());
  }

}
