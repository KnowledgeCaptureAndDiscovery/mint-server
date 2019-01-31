package org.mint.server.classes.wings;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ConstraintItem {
  String id; // For non literals
  String type; // For literals
  Object value; // For literals
  boolean isLiteral;
  
  public ConstraintItem() {}
  
  public ConstraintItem(String id) {
    this.id = id;
    this.isLiteral = false;
  }
  
  public ConstraintItem(Object value, String type) {
    this.type = type;
    this.value = value;
    this.isLiteral = true;
  }
  
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public Object getValue() {
    return value;
  }
  public void setValue(Object value) {
    this.value = value;
  }
  @JsonGetter("isLiteral")
  public boolean isLiteral() {
    return isLiteral;
  }
  @JsonSetter("isLiteral")
  public void setLiteral(boolean isLiteral) {
    this.isLiteral = isLiteral;
  }
}
