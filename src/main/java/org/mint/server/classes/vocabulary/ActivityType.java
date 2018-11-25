package org.mint.server.classes.vocabulary;

import org.mint.server.classes.URIEntity;

public class ActivityType extends URIEntity {
  private static final long serialVersionUID = -2472754085297090582L;
  
  String link;
  boolean required;

  public String getLink() {
    return link;
  }
  public void setLink(String link) {
    this.link = link;
  }
  public boolean isRequired() {
    return required;
  }
  public void setRequired(boolean required) {
    this.required = required;
  }

}
