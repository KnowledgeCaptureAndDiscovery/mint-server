package org.mint.server.classes.wings;

import java.util.ArrayList;

public class WorkflowWrapper {
  ArrayList<Object> constraints;
  Workflow template;
  
  public ArrayList<Object> getConstraints() {
    return constraints;
  }
  public void setConstraints(ArrayList<Object> constraints) {
    this.constraints = constraints;
  }
  public Workflow getTemplate() {
    return template;
  }
  public void setTemplate(Workflow template) {
    this.template = template;
  }
}
