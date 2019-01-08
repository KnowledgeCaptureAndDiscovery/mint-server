package org.mint.server.classes.wings;

import java.util.ArrayList;
import java.util.HashMap;

public class Node extends IdEntity {
  ComponentVariable componentVariable;
  String comment;
  HashMap<String, Port> inputPorts;
  HashMap<String, Port> outputPorts;
  ArrayList<String> machineIds;
  boolean inactive;
  // SetCreationRule prule;
  // SetCreationRule crule;
  String category;
  
  public Node(String id) {
    super(id);
    this.inputPorts = new HashMap<String, Port>();
    this.outputPorts = new HashMap<String, Port>();
  }
  
  public ComponentVariable getComponentVariable() {
    return componentVariable;
  }
  public void setComponentVariable(ComponentVariable componentVariable) {
    this.componentVariable = componentVariable;
  }
  public String getComment() {
    return comment;
  }
  public void setComment(String comment) {
    this.comment = comment;
  }
  public HashMap<String, Port> getInputPorts() {
    return inputPorts;
  }
  public void setInputPorts(HashMap<String, Port> inputPorts) {
    this.inputPorts = inputPorts;
  }
  public HashMap<String, Port> getOutputPorts() {
    return outputPorts;
  }
  public void setOutputPorts(HashMap<String, Port> outputPorts) {
    this.outputPorts = outputPorts;
  }
  public ArrayList<String> getMachineIds() {
    return machineIds;
  }
  public void setMachineIds(ArrayList<String> machineIds) {
    this.machineIds = machineIds;
  }
  public boolean isInactive() {
    return inactive;
  }
  public void setInactive(boolean inactive) {
    this.inactive = inactive;
  }
  
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public void addInputPort(Port port) {
    this.inputPorts.put(port.getId(), port);
  }
  
  public void addOutputPort(Port port) {
    this.outputPorts.put(port.getId(), port);
  }  
  
  public Port findInputPort(String id) {
    for(String pid : inputPorts.keySet())
      if(pid.equals(id))
        return inputPorts.get(pid);
    return null;    
  }
  
  public Port findOutputPort(String id) {
    for(String pid : outputPorts.keySet())
      if(pid.equals(id))
        return inputPorts.get(pid);
    return null;
  }
}
