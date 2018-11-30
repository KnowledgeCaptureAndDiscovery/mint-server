package org.mint.server.classes.graph;

import java.util.ArrayList;
import java.util.HashMap;

import org.mint.server.classes.URIEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Variable extends URIEntity {
  private static final long serialVersionUID = -1065248491858616282L;
  
  String canonical_name;
  ArrayList<String> standard_names;
  GraphPosition position;
  String category;
  
  @JsonIgnore
  HashMap<String, VariableProvenance> provenance;
  
  VariableProvider provider;
  boolean resolved;

  public Variable() {
    this.provenance = new HashMap<String, VariableProvenance>();
  }
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
  public String getCategory() {
    return category;
  }
  public void setCategory(String category) {
    this.category = category;
  }
  public HashMap<String, VariableProvenance> getProvenance() {
    return provenance;
  }
  public void setProvenance(HashMap<String, VariableProvenance> provenance) {
    this.provenance = provenance;
  }
  public VariableProvider getProvider() {
    return provider;
  }
  public void setProvider(VariableProvider provider) {
    this.provider = provider;
  }
  public boolean isResolved() {
    return resolved;
  }
  public void setResolved(boolean resolved) {
    this.resolved = resolved;
  }
  public void copyFrom(Variable v) {
    this.setID(v.getID());
    this.setLabel(v.getLabel());
    this.setPosition(v.getPosition());
    this.setStandard_names(v.getStandard_names());
    this.setCanonical_name(v.getCanonical_name());
  }
  
}
