package org.mint.server.classes.wings;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class IdEntity {
  String id;

  public IdEntity() {}
  
  public IdEntity(String id) {
    this.id = id;
  }
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
  
  
  @JsonIgnore
  public String getNamespace() {
    return this.id.replaceAll("#.*", "#");
  }

  @JsonIgnore
  public void setNamespace(String namespace) {
    String id = this.id.replaceAll(".*#", namespace);
    this.id = id;
  }
  
  @JsonIgnore
  public String getLocalName() {
    return this.id.replaceAll(".*#", "");
  }
}
