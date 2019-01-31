package org.mint.server.classes.graph;

public class VariableProvider {
  public enum Type { MODEL, DATA, OUTPUT };
  
  String id;
  Type type;
  String category;
  
  public VariableProvider(String id, Type type, String category) {
    this.id = id;
    this.type = type;
    this.category = category;
  }
  public String getId() {
    return this.id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public Type getType() {
    return type;
  }
  public void setType(Type type) {
    this.type = type;
  }
  public String getCategory() {
    return category;
  }
  public void setCategory(String category) {
    this.category = category;
  }
}
