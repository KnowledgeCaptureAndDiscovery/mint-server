package org.mint.server.classes.graph;

import org.mint.server.classes.URIEntity;

public class VariableProvider extends URIEntity {
  private static final long serialVersionUID = 7084591893072377975L;
  public enum Type { MODEL, DATA, OUTPUT };
  
  Type type;
  String category;
  
  public VariableProvider(String id, Type type, String category) {
    super(id);
    this.type = type;
    this.category = category;
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
