package org.mint.server.classes;

import java.util.ArrayList;

public class Dataset {
  String id;
  String name;
  ArrayList<DataResource> resources;
  ArrayList<String> variables;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ArrayList<String> getVariables() {
    return variables;
  }

  public void setVariables(ArrayList<String> variables) {
    this.variables = variables;
  }

  public ArrayList<DataResource> getResources() {
    return resources;
  }

  public void setResources(ArrayList<DataResource> resources) {
    this.resources = resources;
  }
  

}
