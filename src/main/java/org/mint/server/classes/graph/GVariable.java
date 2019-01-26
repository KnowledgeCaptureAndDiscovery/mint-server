package org.mint.server.classes.graph;

import java.util.ArrayList;

import org.mint.server.classes.URIEntity;

public class GVariable extends URIEntity {
  private static final long serialVersionUID = -1065248491858616282L;
  
  ArrayList<String> standard_names;
  GraphPosition position;
  String category;
  boolean added;
  
  //@JsonIgnore
  ArrayList<VariableProvenance> provenance;
  
  VariableProvider provider;
  boolean resolved;

  public GVariable() {
    this.provenance = new ArrayList<VariableProvenance>();
  }
  
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
  public ArrayList<VariableProvenance> getProvenance() {
    return provenance;
  }
  public void setProvenance(ArrayList<VariableProvenance> provenance) {
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
    this.setProvider(v.getProvider());
    this.setResolved(v.isResolved());
    this.setStandard_names(v.getStandard_names());
    this.setProvenance(new ArrayList<VariableProvenance>(v.getProvenance()));
  }
  
  public String getMatchingFileName(String modelid, ArrayList<String> vars, boolean isinput) {
    for(VariableProvenance prov : this.provenance) {
      if(prov.getModel().equals(modelid) && prov.isinput == isinput) {
        for(String varname : vars) {
          if(varname.equals(prov.getFileName())) {
            return prov.getFileName();
          }
        }
      }
    }
    return null;
  }
  
  public VariableProvenance getMatchingProvenanceItem(String modelid, String filename) {
    for(VariableProvenance prov : this.provenance) {
      if(prov.getModel().equals(modelid)) {
        if(prov.getFileName().equals(filename))
          return prov; 
      }
    }
    return null;
  }
}
