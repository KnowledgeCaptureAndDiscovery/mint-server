package org.mint.server.classes.wings;

public class Binding extends IdEntity {
  String type;
  Object value;
  
  public Binding() {}
  
  public Binding(String id) {
    super(id);
    this.type = "uri";
  }
  
  public Binding(Object value, boolean isLiteral) {
    this.value = value;
    this.type = "literal";
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
}
