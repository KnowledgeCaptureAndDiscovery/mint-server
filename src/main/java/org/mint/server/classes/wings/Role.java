package org.mint.server.classes.wings;

public class Role extends IdEntity{
  int type;
  String roleid;
  int dimensionality;
  
  public Role() {}
  
  public Role(String id, int type) {
    super(id);
    this.type = type;
  }
  
  public int getType() {
    return type;
  }
  public void setType(int type) {
    this.type = type;
  }
  public String getRoleid() {
    return roleid;
  }
  public void setRoleid(String roleid) {
    this.roleid = roleid;
  }
  public int getDimensionality() {
    return dimensionality;
  }
  public void setDimensionality(int dimensionality) {
    this.dimensionality = dimensionality;
  }
}
