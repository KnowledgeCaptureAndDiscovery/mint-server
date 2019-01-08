package org.mint.server.wings;

import org.mint.server.classes.URIEntity;

public class Role extends URIEntity {
  private static final long serialVersionUID = -3234328055933007451L;
  
  String argid;
  int dimensionality;
  
  public Role() {}
  
  public Role(String id) {
    super(id);
  }

  public String getArgid() {
    return argid;
  }

  public void setArgid(String argid) {
    this.argid = argid;
  }

  public int getDimensionality() {
    return dimensionality;
  }

  public void setDimensionality(int dimensionality) {
    this.dimensionality = dimensionality;
  }
}
