package org.mint.server.wings;

import java.util.ArrayList;

public class WorkflowMetadata {

  String lastUpdateTime;
  String documentation;
  ArrayList<String> contributors;
  ArrayList<String> createdFrom;
  
  public String getLastUpdateTime() {
    return lastUpdateTime;
  }
  public void setLastUpdateTime(String lastUpdateTime) {
    this.lastUpdateTime = lastUpdateTime;
  }
  public String getDocumentation() {
    return documentation;
  }
  public void setDocumentation(String documentation) {
    this.documentation = documentation;
  }
  public ArrayList<String> getContributors() {
    return contributors;
  }
  public void setContributors(ArrayList<String> contributors) {
    this.contributors = contributors;
  }
  public ArrayList<String> getCreatedFrom() {
    return createdFrom;
  }
  public void setCreatedFrom(ArrayList<String> createdFrom) {
    this.createdFrom = createdFrom;
  }
  
}
