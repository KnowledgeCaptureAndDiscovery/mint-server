package org.mint.server.planner;

import org.mint.server.classes.graph.VariableProvider;

public class WorkflowLink {
  String from;
  String to;
  String variable;
  VariableProvider.Type type;
  
  public WorkflowLink(String from, String to, String variable, VariableProvider.Type type) {
    this.from = from;
    this.to = to;
    this.variable = variable;
    this.type = type;
  }
  
  public String getFrom() {
    return from;
  }
  public void setFrom(String from) {
    this.from = from;
  }
  public String getTo() {
    return to;
  }
  public void setTo(String to) {
    this.to = to;
  }
  public String getVariable() {
    return variable;
  }
  public void setVariable(String variable) {
    this.variable = variable;
  }
  public VariableProvider.Type getType() {
    return type;
  }
  public void setType(VariableProvider.Type type) {
    this.type = type;
  }
  
  
}
