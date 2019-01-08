package org.mint.server.wings;

import java.util.ArrayList;
import java.util.List;

import org.mint.server.classes.URIEntity;

public class Node extends URIEntity {
  private static final long serialVersionUID = 3305495000000187168L;
  
  Variable component;
  List<Port> inputPorts;
  List<Port> outputPorts;
  SetCreationRule dataRule;
  SetCreationRule componentRule;
  Position position;

  public Node() {}
  
  public Node(String id) {
    super(id);
    inputPorts = new ArrayList<Port>();
    outputPorts = new ArrayList<Port>();
  }

  public Variable getComponent() {
    return component;
  }

  public void setComponent(Variable component) {
    this.component = component;
  }

  public List<Port> getInputPorts() {
    return inputPorts;
  }

  public void setInputPorts(List<Port> inputPorts) {
    this.inputPorts = inputPorts;
  }

  public List<Port> getOutputPorts() {
    return outputPorts;
  }

  public void setOutputPorts(List<Port> outputPorts) {
    this.outputPorts = outputPorts;
  }

  public SetCreationRule getDataRule() {
    return dataRule;
  }

  public void setDataRule(SetCreationRule dataRule) {
    this.dataRule = dataRule;
  }

  public SetCreationRule getComponentRule() {
    return componentRule;
  }

  public void setComponentRule(SetCreationRule componentRule) {
    this.componentRule = componentRule;
  }
  
  public void addInputPort(Port port) {
    this.inputPorts.add(port);
  }
  
  public void addOutputPort(Port port) {
    this.outputPorts.add(port);
  }  
  
  public Port findInputPort(String id) {
    for(Port p : inputPorts)
      if(p.getID().equals(id))
        return p;
    return null;    
  }
  
  public Port findOutputPort(String id) {
    for(Port p : outputPorts)
      if(p.getID().equals(id))
        return p;
    return null;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }
}
