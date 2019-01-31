package org.mint.server.classes.wings;

import java.util.ArrayList;

public class WorkflowWrapper {
  ArrayList<Constraint> constraints;
  Workflow template;
  
  public ArrayList<Constraint> getConstraints() {
    return constraints;
  }
  public void setConstraints(ArrayList<Constraint> constraints) {
    this.constraints = constraints;
  }
  public Workflow getTemplate() {
    return template;
  }
  public void setTemplate(Workflow template) {
    this.template = template;
  }
}
