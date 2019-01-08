package org.mint.server.classes;

import java.util.ArrayList;

public class DataEnsemble {
  ArrayList<String> datasets;
  ArrayList<String> variables;
  
  public ArrayList<String> getDatasets() {
    return datasets;
  }
  public void setDatasets(ArrayList<String> datasets) {
    this.datasets = datasets;
  }
  public ArrayList<String> getVariables() {
    return variables;
  }
  public void setVariables(ArrayList<String> variables) {
    this.variables = variables;
  }
}
