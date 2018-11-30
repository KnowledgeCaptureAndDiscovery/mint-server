package org.mint.server.classes.model;

import java.util.ArrayList;

import org.mint.server.classes.URIEntity;

public class Model extends URIEntity {
  private static final long serialVersionUID = -2859278726136789000L;

  String category;
  ArrayList<ModelIO> inputs;
  ArrayList<ModelIO> outputs;
  
  public void copyFrom(Model model) {
    this.setID(model.getID());
    this.setLabel(model.getLabel());
    this.category = model.getCategory();
    this.inputs = new ArrayList<ModelIO>(model.getInputs());
    this.outputs = new ArrayList<ModelIO>(model.getOutputs());
  }
  
  public String getCategory() {
    return category;
  }
  public void setCategory(String category) {
    this.category = category;
  }
  public ArrayList<ModelIO> getInputs() {
    return inputs;
  }
  public void setInputs(ArrayList<ModelIO> inputs) {
    this.inputs = inputs;
  }
  public ArrayList<ModelIO> getOutputs() {
    return outputs;
  }
  public void setOutputs(ArrayList<ModelIO> outputs) {
    this.outputs = outputs;
  }
}
