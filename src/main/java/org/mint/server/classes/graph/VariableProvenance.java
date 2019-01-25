package org.mint.server.classes.graph;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class VariableProvenance {
  String model;
  String file_id;
  String file_type;
  String units;
  
  boolean isinput;
  
  public VariableProvenance(String model, String file_id, String file_type, String units, boolean isinput) {
    this.model = model;
    this.file_id = file_id;
    this.file_type = file_type;
    this.units = units;
    this.isinput = isinput;
  }
  
  public String getModel() {
    return model;
  }
  public void setModel(String model) {
    this.model = model;
  }
  public String getFile_id() {
    return file_id;
  }
  public void setFile_id(String file_id) {
    this.file_id = file_id;
  }
  public String getFile_type() {
    return file_type;
  }
  public void setFile_type(String file_type) {
    this.file_type = file_type;
  }
  public String getUnits() {
    return units;
  }
  public void setUnits(String units) {
    this.units = units;
  }
  public boolean isIsinput() {
    return isinput;
  }
  public void setIsinput(boolean isinput) {
    this.isinput = isinput;
  }
  
  @JsonIgnore
  public String getFileName() {
    return this.file_id.replaceAll(".*#", "");
  }
}
