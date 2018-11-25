package org.mint.server.classes.graph;

import java.util.ArrayList;

import org.mint.server.classes.URIEntity;

public class Variable extends URIEntity {
  private static final long serialVersionUID = -1065248491858616282L;
  
  String canonical_name;
  ArrayList<String> standard_names;
  GraphPosition position;

  public String getCanonical_name() {
    return canonical_name;
  }
  public void setCanonical_name(String canonical_name) {
    this.canonical_name = canonical_name;
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
  
}
