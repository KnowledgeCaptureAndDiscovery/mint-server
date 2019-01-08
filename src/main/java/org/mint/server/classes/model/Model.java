package org.mint.server.classes.model;

import java.util.ArrayList;

import org.mint.server.classes.URIEntity;

public class Model extends URIEntity {
  private static final long serialVersionUID = -2859278726136789000L;

  String version;
  ModelType type;
  
  ArrayList<ModelIO> inputs;
  ArrayList<ModelIO> outputs;
  
  public void copyFrom(Model model) {
    this.setID(model.getID());
    this.setLabel(model.getLabel());
    this.setVersion(model.getVersion());
    this.setType(model.getType());
    this.inputs = new ArrayList<ModelIO>(model.getInputs());
    this.outputs = new ArrayList<ModelIO>(model.getOutputs());
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public ModelType getType() {
    return type;
  }

  public void setType(ModelType type) {
    this.type = type;
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
