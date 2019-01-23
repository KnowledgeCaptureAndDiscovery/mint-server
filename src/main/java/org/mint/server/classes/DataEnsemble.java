package org.mint.server.classes;

import java.util.ArrayList;

public class DataEnsemble {
  ArrayList<Dataset> datasets;
  ArrayList<String> variables;
  
  public ArrayList<Dataset> getDatasets() {
    return datasets;
  }
  public void setDatasets(ArrayList<Dataset> datasets) {
    this.datasets = datasets;
  }
  public ArrayList<String> getVariables() {
    return variables;
  }
  public void setVariables(ArrayList<String> variables) {
    this.variables = variables;
  }
}
