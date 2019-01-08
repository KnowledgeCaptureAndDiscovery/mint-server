package org.mint.server.wings;

import org.mint.server.classes.URIEntity;

public class Variable extends URIEntity {
  private static final long serialVersionUID = 2130248932529680344L;
  
  Binding binding;
  Position position;
  
  public Variable() {}
  
  public Variable(String id) {
    super(id);
  }

  public Binding getBinding() {
    return binding;
  }

  public void setBinding(Binding binding) {
    this.binding = binding;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }
}
