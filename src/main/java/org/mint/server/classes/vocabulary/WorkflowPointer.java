package org.mint.server.classes.vocabulary;

import org.mint.server.classes.URIEntity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class WorkflowPointer extends URIEntity {
  private static final long serialVersionUID = -8065390873029346462L;
  
  enum Type { DATA_COMPARISON, DATA_GENERATION, MODEL_CALIBRATION };
  
  Type type;
  String input_variable;
  String output_variable;
  
  public Type getType() {
    return type;
  }
  public void setType(Type type) {
    this.type = type;
  }
  @JsonGetter("input_variable")
  public String getInputVariable() {
    return input_variable;
  }
  @JsonSetter("input_variable")
  public void setInputVariable(String input_variable) {
    this.input_variable = input_variable;
  }
  @JsonGetter("output_variable")
  public String getOutputVariable() {
    return output_variable;
  }
  @JsonSetter("output_variable")
  public void setOutputVariable(String output_variable) {
    this.output_variable = output_variable;
  }

}
