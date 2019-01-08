package org.mint.server.classes.model;

import java.util.ArrayList;

import org.mint.server.classes.URIEntity;

public class ModelType extends URIEntity {
  private static final long serialVersionUID = -5873341996219842864L;
  
  String description;
  String category;
  ArrayList<String> versions;
  
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getCategory() {
    return category;
  }
  public void setCategory(String category) {
    this.category = category;
  }
  public ArrayList<String> getVersions() {
    return versions;
  }
  public void setVersions(ArrayList<String> versions) {
    this.versions = versions;
  }
}
