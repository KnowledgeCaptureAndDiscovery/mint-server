package org.mint.server.wings;

public class Link {
  String id;
  String variable;
  String fromPort;
  String fromNode;
  String toPort;
  String toNode;

  public Link() {}
  
  public Link(String id) {
    this.id = id;
  }
  
  public Link(String id, String fromNode, String fromPort, String toNode, String toPort, String variable) {
    this.id = id;
    this.fromNode = fromNode;
    this.fromPort = fromPort;
    this.toNode = toNode;
    this.toPort = toPort;
    this.variable = variable;
  }
  
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getVariable() {
    return variable;
  }

  public void setVariable(String variable) {
    this.variable = variable;
  }

  public String getFromPort() {
    return fromPort;
  }

  public void setFromPort(String fromPort) {
    this.fromPort = fromPort;
  }

  public String getFromNode() {
    return fromNode;
  }

  public void setFromNode(String fromNode) {
    this.fromNode = fromNode;
  }

  public String getToPort() {
    return toPort;
  }

  public void setToPort(String toPort) {
    this.toPort = toPort;
  }

  public String getToNode() {
    return toNode;
  }

  public void setToNode(String toNode) {
    this.toNode = toNode;
  }
}
