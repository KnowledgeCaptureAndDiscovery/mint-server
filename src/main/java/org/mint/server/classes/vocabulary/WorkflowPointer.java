package org.mint.server.classes.vocabulary;

import java.util.ArrayList;

import org.mint.server.classes.URIEntity;

public class WorkflowPointer extends URIEntity {
  private static final long serialVersionUID = -8065390873029346462L;
  
  enum WorkflowType { DATA_COMPARISON, DATA_GENERATION, DATA_EXPLORATION, MODEL_CALIBRATION, MODELING };
  
  WorkflowType type;
  String userid;
  String domain;
  String workflow;
  ArrayList<String> components;
  
  public WorkflowType getType() {
    return type;
  }
  public void setType(WorkflowType type) {
    this.type = type;
  }
  public String getDomain() {
    return domain;
  }
  public void setDomain(String domain) {
    this.domain = domain;
  }
  public String getWorkflow() {
    return workflow;
  }
  public void setWorkflow(String workflow) {
    this.workflow = workflow;
  }
  public String getUserid() {
    return userid;
  }
  public void setUserid(String userid) {
    this.userid = userid;
  }
  public ArrayList<String> getComponents() {
    return components;
  }
  public void setComponents(ArrayList<String> components) {
    this.components = components;
  }

}
