package org.mint.server.classes.rawcag;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class CagEdge {
  String source;
  String target;
  
  Object InfluenceStatements;
  Object CPT;
  Object polyfit;
  
  public String getSource() {
    return source;
  }
  public void setSource(String source) {
    this.source = source;
  }
  public String getTarget() {
    return target;
  }
  public void setTarget(String target) {
    this.target = target;
  }
  
  @JsonGetter("InfluenceStatements")
  public Object getInfluenceStatements() {
    return InfluenceStatements;
  }
  @JsonSetter("InfluenceStatements")
  public void setInfluenceStatements(Object influenceStatements) {
    InfluenceStatements = influenceStatements;
  }
  
  @JsonGetter("CPT")
  public Object getCPT() {
    return CPT;
  }
  @JsonSetter("CPT")
  public void setCPT(Object cPT) {
    CPT = cPT;
  }
  
  public Object getPolyfit() {
    return polyfit;
  }
  public void setPolyfit(Object polyfit) {
    this.polyfit = polyfit;
  }
}
