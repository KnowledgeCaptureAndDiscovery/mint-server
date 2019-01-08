package org.mint.server.classes.model;

import java.util.HashMap;

import org.mint.server.classes.URIEntity;

public class ModelVariable extends URIEntity {
  private static final long serialVersionUID = 1L;
  
  String description;
  String standard_name;
  
  HashMap<String, String> metadata;
  
  public ModelVariable() {
    this.metadata = new HashMap<String, String>();
  }
  
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public String getStandard_name() {
    return standard_name;
  }
  public void setStandard_name(String standard_name) {
    this.standard_name = standard_name;
  }
  public HashMap<String, String> getMetadata() {
    return metadata;
  }
  public void setMetadata(HashMap<String, String> metadata) {
    this.metadata = metadata;
  }
}
