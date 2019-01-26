package org.mint.server.classes;

import java.util.ArrayList;

public class Activity extends URIEntity {
  private static final long serialVersionUID = 2839348608043948509L;

  ArrayList<String> output;
  boolean required;

  public ArrayList<String> getOutput() {
    return output;
  }

  public void setOutput(ArrayList<String> output) {
    this.output = output;
  }
  
  public boolean isRequired() {
    return required;
  }
  public void setRequired(boolean required) {
    this.required = required;
  }
}
