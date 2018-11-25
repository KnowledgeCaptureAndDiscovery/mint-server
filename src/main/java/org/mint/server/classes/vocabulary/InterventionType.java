package org.mint.server.classes.vocabulary;

import org.mint.server.classes.URIEntity;

public class InterventionType extends URIEntity {
  private static final long serialVersionUID = 125336454248734634L;
  
  String subgraph;
 
  public String getSubgraph() {
    return subgraph;
  }
  public void setSubgraph(String subgraph) {
    this.subgraph = subgraph;
  }
  
}
