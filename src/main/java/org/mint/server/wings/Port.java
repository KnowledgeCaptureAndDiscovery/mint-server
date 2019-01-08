package org.mint.server.wings;

import org.mint.server.classes.URIEntity;

public class Port extends URIEntity {
  private static final long serialVersionUID = 6400017891901048021L;
  
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
