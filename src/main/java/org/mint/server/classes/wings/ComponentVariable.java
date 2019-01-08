package org.mint.server.classes.wings;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ComponentVariable extends IdEntity {
  boolean concrete;
  int type = 3;
  boolean autofill;
  boolean breakpoint;
  Binding binding;

  public ComponentVariable() {}
  
  public ComponentVariable(String id) {
    super(id);
  }
  
  @JsonGetter("isConcrete")
  public boolean isConcrete() {
    return concrete;
  }
  
  @JsonSetter("isConcrete")
  public void setConcrete(boolean concrete) {
    this.concrete = concrete;
  }
  
  public int getType() {
    return type;
  }
  public void setType(int type) {
    this.type = type;
  }
  public boolean isAutofill() {
    return autofill;
  }
  public void setAutofill(boolean autofill) {
    this.autofill = autofill;
  }
  public boolean isBreakpoint() {
    return breakpoint;
  }
  public void setBreakpoint(boolean breakpoint) {
    this.breakpoint = breakpoint;
  }
  public Binding getBinding() {
    return binding;
  }
  public void setBinding(Binding binding) {
    this.binding = binding;
  }
}
