package org.mint.server.classes.wings;

public class Link extends IdEntity {
  IdEntity fromNode;
  IdEntity toNode;
  IdEntity fromPort;
  IdEntity toPort;
  IdEntity variable;
  
  public Link() {}
  
  public Link(String id) {
    super(id);
  }
  
  public Link(String id, String fromNode, String fromPort, String toNode, String toPort, String variable) {
    this.id = id;
    this.fromNode = fromNode != null ? new IdEntity(fromNode) : null;
    this.fromPort = fromPort != null ? new IdEntity(fromPort) : null;
    this.toNode = toNode != null ? new IdEntity(toNode) : null;
    this.toPort = toPort != null ? new IdEntity(toPort) : null;
    this.variable = variable != null ? new IdEntity(variable) : null;
  }
  
  public IdEntity getFromNode() {
    return fromNode;
  }
  public void setFromNode(IdEntity fromNode) {
    this.fromNode = fromNode;
  }
  public IdEntity getToNode() {
    return toNode;
  }
  public void setToNode(IdEntity toNode) {
    this.toNode = toNode;
  }
  public IdEntity getFromPort() {
    return fromPort;
  }
  public void setFromPort(IdEntity fromPort) {
    this.fromPort = fromPort;
  }
  public IdEntity getToPort() {
    return toPort;
  }
  public void setToPort(IdEntity toPort) {
    this.toPort = toPort;
  }
  public IdEntity getVariable() {
    return variable;
  }
  public void setVariable(IdEntity variable) {
    this.variable = variable;
  }
  
}
