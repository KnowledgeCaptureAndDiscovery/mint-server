package org.mint.server.classes;

import java.util.ArrayList;

public class DataSpecification extends URIEntity {
  private static final long serialVersionUID = 1772106688715676040L;

  ArrayList<DataEnsemble> ensemble;

  public ArrayList<DataEnsemble> getEnsemble() {
    return ensemble;
  }

  public void setEnsemble(ArrayList<DataEnsemble> ensemble) {
    this.ensemble = ensemble;
  }

}
