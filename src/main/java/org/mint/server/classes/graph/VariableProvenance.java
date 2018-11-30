package org.mint.server.classes.graph;

import java.util.HashMap;

public class VariableProvenance {
  String model;
  String file_id;
  
  HashMap<String, String> metadata;
  
  boolean isinput;
  
  public VariableProvenance(String model, String file_id, HashMap<String, String> metadata, boolean isinput) {
    this.model = model;
    this.file_id = file_id;
    this.metadata = metadata;
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
  public HashMap<String, String> getMetadata() {
    return metadata;
  }
  public void setMetadata(HashMap<String, String> metadata) {
    this.metadata = metadata;
  }
  public boolean isIsinput() {
    return isinput;
  }
  public void setIsinput(boolean isinput) {
    this.isinput = isinput;
  }
}
