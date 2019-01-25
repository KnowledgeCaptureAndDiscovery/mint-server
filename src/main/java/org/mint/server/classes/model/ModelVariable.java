package org.mint.server.classes.model;

import org.mint.server.classes.URIEntity;

public class ModelVariable extends URIEntity {
  private static final long serialVersionUID = 1L;
  
  String units;
  String standard_name;

  public String getStandard_name() {
    return standard_name;
  }
  public void setStandard_name(String standard_name) {
    this.standard_name = standard_name;
  }
  public String getUnits() {
    return units;
  }
  public void setUnits(String units) {
    this.units = units;
  }
}
