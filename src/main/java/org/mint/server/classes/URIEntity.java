package org.mint.server.classes;

import java.io.Serializable;
import java.net.URI;

public class URIEntity implements Serializable, Comparable<URIEntity> {
  private static final long serialVersionUID = -1243434L;
  
  private URI id;
  private String label;
  private String localName;

  public URIEntity() {}
  
  public URIEntity(String id) {
    this.setID(id);
  }
  
  public URIEntity(String id, String label) {
    this(id);
    this.label = label;
  }

  public String getID() {
    if (id != null)
      return id.toString();
    else
      return null;
  }

  public void setID(String id) {
    try {
      this.id = new URI(id).normalize();
      this.localName = this.extractNameFromId();
      if(this.label == null)
        this.label = this.localName;
    } catch (Exception e) {
      System.err.println(id + " Not a URI. Only URIs allowed for IDs");
    }
  }

  public String getLocalName() {
    return localName;
  }


  public String extractNameFromId() {
    if (id != null) {
      if(id.getFragment() != null)
        return id.getFragment();
      else {
        return id.getPath().substring(id.getPath().lastIndexOf("/")+1);
      }
    }
    else
      return null;
  }
  
  public String getLabel() {
    return this.label;
  }

  public void setLabel(String label) {
    this.label = label;
    if(label == null)
      this.label = this.localName;
  }
  
  /*
  public String getURL() {
    return this.getNamespace().replaceAll("#$", "");
  }
  
  public String getNamespace() {
    if (id != null)
      return id.getScheme() + ":" + id.getSchemeSpecificPart() + "#";
    else
      return null;
  }
  */

  @Override
  public String toString() {
    return getLabel();
  }

  @Override
  public int hashCode() {
    return this.getID().hashCode();
  }

  @Override
  public int compareTo(URIEntity o) {
    if(o != null)
      return this.getID().compareTo(o.getID());
    return 1;
  }

}
