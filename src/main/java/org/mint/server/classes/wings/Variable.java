package org.mint.server.classes.wings;

import java.util.ArrayList;
import java.util.HashMap;

public class Variable extends IdEntity {
  String name;
  int type;
  String comment;
  boolean autofill;
  boolean breakpoint;
  String category;
  HashMap<String, ArrayList<String>> extra;
  
  public Variable() {}
  
  public Variable(String id, int type) {
    super(id);
    this.type = type;
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getType() {
    return type;
  }
  public void setType(int type) {
    this.type = type;
  }
  public String getComment() {
    return comment;
  }
  public void setComment(String comment) {
    this.comment = comment;
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

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public HashMap<String, ArrayList<String>> getExtra() {
    return extra;
  }

  public void setExtra(HashMap<String, ArrayList<String>> extra) {
    this.extra = extra;
  }
  
  public String toString() {
    return this.id;
  }
}
