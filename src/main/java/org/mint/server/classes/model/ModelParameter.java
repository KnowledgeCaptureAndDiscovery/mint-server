package org.mint.server.classes.model;

import org.mint.server.classes.URIEntity;

public class ModelParameter extends URIEntity {
  private static final long serialVersionUID = -6905519040199359021L;
  
  String label;
  String type;
  String defaultValue;
  String dataType;
  
  public ModelParameter() {}

  public ModelParameter(ModelParameter from) {
    this.setID(from.getID());
    this.setLabel(from.getLabel());
    this.setType(from.getType());
    this.setDataType(from.getDataType());
    this.setDefaultValue(from.getDefaultValue());
  }
  
  public ModelParameter(String id, String label, String type, String defaultValue, String dataType) {
    super(id);
    this.setLabel(label);
    this.type = type;
    this.defaultValue = defaultValue;
    this.dataType = dataType;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }
  
}
