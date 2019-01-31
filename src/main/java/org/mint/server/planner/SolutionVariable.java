package org.mint.server.planner;

import java.util.ArrayList;

import org.mint.server.classes.graph.GVariable;
import org.mint.server.classes.graph.VariableProvenance;
import org.mint.server.classes.graph.VariableProvider;

public class SolutionVariable {
  GVariable variable; // Pointer to the graph variable
  String type; // IO type
  
  ArrayList<VariableProvenance> provenance;
  VariableProvider provider;
  boolean resolved;
  
  SolutionVariable() {
    this.provenance = new ArrayList<VariableProvenance>();
  }
  
  SolutionVariable(GVariable variable) {
    this();
    this.variable = variable;
  }
  
  SolutionVariable(SolutionVariable v) {
    this.copyFrom(v);
  }

  public GVariable getVariable() {
    return variable;
  }
  public void setVariable(GVariable variable) {
    this.variable = variable;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
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
  
  public void copyFrom(SolutionVariable v) {
    this.variable = new GVariable(v.getVariable());
    this.setProvider(v.getProvider());
    this.setResolved(v.isResolved());
    this.setProvenance(new ArrayList<VariableProvenance>(v.getProvenance()));
  }
  
  public String getMatchingFileName(String modelid, ArrayList<String> vars, boolean isinput) {
    for(VariableProvenance prov : this.provenance) {
      if(prov.getModel().equals(modelid) && prov.isIsinput() == isinput) {
        for(String varname : vars) {
          if(varname.equals(prov.getFile_id())) {
            return prov.getFile_id();
          }
        }
      }
    }
    return null;
  }
  
  public VariableProvenance getMatchingProvenanceItem(String modelid, String filename) {
    for(VariableProvenance prov : this.provenance) {
      if(prov.getModel().equals(modelid)) {
        if(prov.getFile_id().equals(filename))
          return prov; 
      }
    }
    return null;
  }
}
