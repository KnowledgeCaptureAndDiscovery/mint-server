package org.mint.server.wings;

import java.util.ArrayList;
import java.util.List;

public class Binding {
  String id;
  Value value;
  List<Binding> children;

  public Binding() {}
  
  public Binding(String id) {
    this.id = id;
    this.children = new ArrayList<Binding>();
  }
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Value getValue() {
    return value;
  }

  public void setValue(Value value) {
    this.value = value;
  }

  public List<Binding> getChildren() {
    return children;
  }

  public void setChildren(List<Binding> children) {
    this.children = children;
  }
  
  public void addChild(Binding child) {
    this.children.add(child);
  }
}
