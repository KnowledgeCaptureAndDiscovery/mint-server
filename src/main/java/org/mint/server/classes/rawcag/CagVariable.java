package org.mint.server.classes.rawcag;

import java.util.ArrayList;

public class CagVariable {
  String name;
  String units;
  String dtype;
  ArrayList<String> arguments;
  ArrayList<String> indicators;
  ArrayList<ArrayList<Object>> alignment;
  
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getUnits() {
    return units;
  }
  public void setUnits(String units) {
    this.units = units;
  }
  public String getDtype() {
    return dtype;
  }
  public void setDtype(String dtype) {
    this.dtype = dtype;
  }
  public ArrayList<String> getArguments() {
    return arguments;
  }
  public void setArguments(ArrayList<String> arguments) {
    this.arguments = arguments;
  }
  public ArrayList<String> getIndicators() {
    return indicators;
  }
  public void setIndicators(ArrayList<String> indicators) {
    this.indicators = indicators;
  }
  public ArrayList<ArrayList<Object>> getAlignment() {
    return alignment;
  }
  public void setAlignment(ArrayList<ArrayList<Object>> alignment) {
    this.alignment = alignment;
  }
}
