package org.mint.server.classes.vocabulary;

import org.mint.server.classes.URIEntity;

public class EventType extends URIEntity {
  private static final long serialVersionUID = 1309820595563944958L;

  String graph;
  
  public String getGraph() {
    return graph;
  }
  public void setGraph(String graph) {
    this.graph = graph;
  }

}
