package org.mint.server.classes.wings;

import java.util.ArrayList;
import java.util.Date;

public class WorkflowMetadata {
  public Date lastUpdateTime;
  public String tellme;
  public String documentation;
  public ArrayList<String> contributors = new ArrayList<String>();
  public ArrayList<String> createdFrom = new ArrayList<String>();

  public WorkflowMetadata() {
  }

  /*
   * Metadata Properties
   */
  public void addContributor(String username) {
    if (username != null && !this.contributors.contains(username))
      this.contributors.add(username);
  }

  public ArrayList<String> getContributors() {
    return this.contributors;
  }

  public void setLastUpdateTime() {
    this.lastUpdateTime = new Date();
  }

  public void setLastUpdateTime(Date datetime) {
    this.lastUpdateTime = datetime;
  }
  
  public Date getLastUpdateTime() {
    return this.lastUpdateTime;
  }

  public void setDocumentation(String doc) {
    this.documentation = doc;
  }

  public String getDocumentation() {
    return this.documentation;
  }

  public void addCreationSource(String name) {
    if (!this.createdFrom.contains(name))
      this.createdFrom.add(name);
  }

  public ArrayList<String> getCreationSources() {
    return this.createdFrom;
  }

  public String getTellme() {
    return tellme;
  }

  public void setTellme(String tellme) {
    this.tellme = tellme;
  }
}