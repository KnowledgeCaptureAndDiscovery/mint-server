package org.mint.server.classes.wings;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Workflow extends IdEntity {
  String version = "2.0";
  String onturl = "http://www.wings-workflows.org/ontology/workflow.owl";
  String wflowns = this.onturl + "#";
  
  public HashMap<String, Node> nodes;
  public HashMap<String, Link> links;
  public HashMap<String, Variable> variables;
  
  HashMap<String, Role> inputRoles;
  HashMap<String, Role> outputRoles;
  
  public HashMap<String, String> props;
  
  // Metadata metadata;
  // Rules rules
  // Properties props
  
  public Workflow() {}
  
  public Workflow(String id) {
    this.id = id;
    this.links = new HashMap<String, Link>();
    this.variables = new HashMap<String, Variable>();
    this.nodes = new HashMap<String, Node>();
    this.inputRoles = new HashMap<String, Role>();
    this.outputRoles = new HashMap<String, Role>();
    this.props = new HashMap<String, String>();
  }
  
  @JsonGetter("Links")
  public Map<String, Link> getLinks() {
    return this.links;
  }

  @JsonSetter("Links")
  public void setLinks(HashMap<String, Link> links) {
    this.links = links;
  }

  @JsonGetter("Variables")
  public Map<String, Variable> getVariables() {
    return variables;
  }

  @JsonSetter("Variables")
  public void setVariables(HashMap<String, Variable> variables) {
    this.variables = variables;
  }

  @JsonGetter("Nodes")
  public Map<String, Node> getNodes() {
    return nodes;
  }

  @JsonSetter("Nodes")
  public void setNodes(HashMap<String, Node> nodes) {
    this.nodes = nodes;
  }
  
  public void addNode(Node node) {
    this.nodes.put(node.getId(), node);
  }
  
  public void addLink(Link link) {
    this.links.put(link.getId(), link);
  }
  
  public void addVariable(Variable variable) {
    this.variables.put(variable.getId(), variable);
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
  
  public Node addNode(ComponentVariable cvar) {
    String cname = cvar.getLocalName();
    String nodeid = this.getNamespace() + cname + "_node";
    String nid = nodeid;
    int i = 1;
    while (this.getNode(nid) != null) {
      nid = nodeid + "_" + i;
      i++;
    }
    Node node = new Node(nid);
    node.setComponentVariable(cvar);
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
        fromN != null ? fromN.getId() : null, 
            fromPort != null ? fromPort.getId(): null,
                toN != null ? toN.getId(): null, 
                    toPort != null ? toPort.getId(): null, 
                        var != null ? var.getId() : null);
    
    if (toN != null && toN.findInputPort(toPort.getId()) == null) 
      toN.addInputPort(toPort);

    if (fromN != null && fromN.findInputPort(fromPort.getId()) == null)
      fromN.addOutputPort(fromPort);

    return l;
  }

  public HashMap<String, String> getProps() {
    return props;
  }

  public void setProps(HashMap<String, String> props) {
    this.props = props;
  }
}
