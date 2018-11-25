package org.mint.server.classes.graph;

import java.util.ArrayList;

import org.mint.server.classes.URIEntity;

public class VariableGraph extends URIEntity {
  private static final long serialVersionUID = 2981002663856220836L;
  
  ArrayList<Variable> variables;
  ArrayList<Relation> links;
  
  public VariableGraph() {
    super();
    variables = new ArrayList<Variable>();
    links = new ArrayList<Relation>();
  }

  public ArrayList<Variable> getVariables() {
    return variables;
  }

  public void setVariables(ArrayList<Variable> variables) {
    this.variables = variables;
  }

  public ArrayList<Relation> getLinks() {
    return links;
  }

  public void setLinks(ArrayList<Relation> links) {
    this.links = links;
  }

}
