package org.mint.server.classes.model;

import java.util.ArrayList;

import org.mint.server.classes.URIEntity;

public class Model extends URIEntity {
  private static final long serialVersionUID = -2859278726136789000L;

  String version;
  ModelType type;
  
  ArrayList<ModelIO> inputs;
  ArrayList<ModelIO> outputs;
  ArrayList<ModelParameter> parameters;
  
  public void copyFrom(Model model) {
    this.setID(model.getID());
    this.setLabel(model.getLabel());
    this.setVersion(model.getVersion());
    this.setType(model.getType());
    
    this.inputs = new ArrayList<ModelIO>();
    this.outputs = new ArrayList<ModelIO>();
    this.parameters = new ArrayList<ModelParameter>();
    
    if(model.getInputs() != null)
      for(ModelIO io : model.getInputs())
        this.inputs.add(new ModelIO(io));
    if(model.getOutputs() != null)
      for(ModelIO io : model.getOutputs())
        this.outputs.add(new ModelIO(io));
    if(model.getParameters() != null)
      for(ModelParameter param : model.getParameters())
        this.parameters.add(new ModelParameter(param));
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

  public ArrayList<ModelParameter> getParameters() {
    return parameters;
  }

  public void setParameters(ArrayList<ModelParameter> parameters) {
    this.parameters = parameters;
  }
}
