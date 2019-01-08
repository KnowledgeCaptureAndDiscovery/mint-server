package org.mint.server.classes.wings;

public class Port extends IdEntity {
  Role role;
  
  public Port() {}
  
  public Port(String id) {
    super(id);
  }
  
  public Role getRole() {
    return role;
  }
  public void setRole(Role role) {
    this.role = role;
  }
}
