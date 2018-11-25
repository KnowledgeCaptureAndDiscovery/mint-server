package org.mint.server.classes.question.template;

public class TemplateEvent {
  String type;
  String variableGraphId;
  
  public String getType() {
    return type;
  }
  
  public void setType(String type) {
    this.type = type;
  }

  public String getVariableGraphId() {
    return variableGraphId;
  }

  public void setVariableGraphId(String variableGraphId) {
    this.variableGraphId = variableGraphId;
  }
  
}
