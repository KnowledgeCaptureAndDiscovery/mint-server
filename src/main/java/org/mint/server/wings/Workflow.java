package org.mint.server.wings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mint.server.classes.URIEntity;

public class Workflow extends URIEntity {
  private static final long serialVersionUID = 950579762421166168L;
  
  Map<String, Link> links;
  Map<String, Variable> variables;
  Map<String, Node> nodes;
  List<Constraint> constraints;
  
  Map<String, Role> inputRoles;
  Map<String, Role> outputRoles;

  public Workflow() {}
  
  public Workflow(String id) {
    super(id);
    links = new HashMap<String, Link>();
    variables = new HashMap<String, Variable>();
    nodes = new HashMap<String, Node>();
    inputRoles = new HashMap<String, Role>();
    outputRoles = new HashMap<String, Role>();
    constraints = new ArrayList<Constraint>();
  }

  public String getNamespace() {
    return this.getID().replaceAll("#.*", "#");
  }

  public void setNamespace(String namespace) {
    String id = this.getID().replaceAll(".*#", namespace);
    this.setID(id);
  }

  public Map<String, Link> getLinks() {
    return links;
  }

  public void setLinks(Map<String, Link> links) {
    this.links = links;
  }

  public Map<String, Variable> getVariables() {
    return variables;
  }

  public void setVariables(Map<String, Variable> variables) {
    this.variables = variables;
  }

  public Map<String, Node> getNodes() {
    return nodes;
  }

  public void setNodes(Map<String, Node> nodes) {
    this.nodes = nodes;
  }

  public List<Constraint> getConstraints() {
    return constraints;
  }

  public void setConstraints(List<Constraint> constraints) {
    this.constraints = constraints;
  }
  
  public void addNode(Node node) {
    this.nodes.put(node.getID(), node);
  }
  
  public void addLink(Link link) {
    this.links.put(link.getId(), link);
  }
  
  public void addVariable(Variable variable) {
    this.variables.put(variable.getID(), variable);
  }
  
  public void addConstraint(Constraint constraint) {
    this.constraints.add(constraint);
  }
  
  public Map<String, Role> getInputRoles() {
    return inputRoles;
  }

  public Map<String, Role> getOutputRoles() {
    return outputRoles;
  }

  public void addInputRole(String varid, Role role) {
    this.inputRoles.put(varid, role);
  }
  
  public void addOutputRole(String varid, Role role) {
    this.outputRoles.put(varid, role);
  }
  
  public Node getNode(String id) {
    return this.nodes.get(id);
  }

  public Variable getVariable(String id) {
    return this.variables.get(id);
  }
  
  public Link getLink(String id) {
    return this.links.get(id);
  }
  
  /*
   * Helpful functions
   */
  
  public Node addNode(Variable cvar) {
    String cname = cvar.getLocalName();
    String nodeid = this.getNamespace() + cname + "_node";
    String nid = nodeid;
    int i = 1;
    while (this.getNode(nid) != null) {
      nid = nodeid + "_" + i;
      i++;
    }
    Node node = new Node(nid);
    node.setComponent(cvar);
    this.addNode(node);
    return node;
  }
  
  private String createLinkId(Port fromPort, Port toPort, Variable var) {
    String lid = this.getNamespace();
    lid += var.getLocalName() + "_";
    
    if (fromPort != null)
      lid += fromPort.getLocalName() + "_to";
    else
      lid += "to";

    if (toPort != null)
      lid += "_" + toPort.getLocalName();
    else
      lid += "_output";

    return lid;
  }
  
  public Link addLink(String lid, Node fromN, Node toN, Port fromPort, Port toPort, Variable var) {
    if(lid == null) {
      String olid = this.createLinkId(fromPort, toPort, var);
      int i = 1;
      lid = olid;
      while (getLink(lid) != null) {
        lid = olid + "_" + String.format("%04d", i);
        i++;
      }
    }

    Link l = new Link(lid, 
        fromN != null ? fromN.getID() : null, 
            fromPort != null ? fromPort.getID(): null,
                toN != null ? toN.getID(): null, 
                    toPort != null ? toPort.getID(): null, 
                        var != null ? var.getID() : null);
    
    if (toN != null && toN.findInputPort(toPort.getID()) == null) 
      toN.addInputPort(toPort);

    if (fromN != null && fromN.findInputPort(fromPort.getID()) == null)
      fromN.addOutputPort(fromPort);

    return l;
  }
}
