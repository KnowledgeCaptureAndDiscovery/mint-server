package org.mint.server.classes.vocabulary;

import java.util.ArrayList;

import org.mint.server.classes.URIEntity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class TaskType extends URIEntity {
  private static final long serialVersionUID = 8411248101206256854L;

  ArrayList<ActivityType> activity_types;
  ArrayList<String> dependencies;
  
  @JsonGetter("activity_types")
  public ArrayList<ActivityType> getActivityTypes() {
    return activity_types;
  }
  
  @JsonSetter("activity_types")
  public void setActivityTypes(ArrayList<ActivityType> activity_types) {
    this.activity_types = activity_types;
  }

  public ArrayList<String> getDependencies() {
    return dependencies;
  }

  public void setDependencies(ArrayList<String> dependencies) {
    this.dependencies = dependencies;
  }
}
