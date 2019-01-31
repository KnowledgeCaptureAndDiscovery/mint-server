package org.mint.server.planner;

import java.util.ArrayList;

import org.mint.server.classes.URIEntity;
import org.mint.server.classes.graph.VariableGraph;
import org.mint.server.classes.model.Model;
import org.mint.server.classes.wings.Workflow;

public class WorkflowSolution extends URIEntity {
  private static final long serialVersionUID = 4890370333549158851L;

  ArrayList<Model> models;
  ArrayList<SolutionVariable> variables; 
  ArrayList<WorkflowLink> links;
  Workflow modelGraph;
  Workflow wingsWorkflow;
  VariableGraph graph;
  
  public ArrayList<Model> getModels() {
    return models;
  }
  public void setModels(ArrayList<Model> models) {
    this.models = models;
  }
  public ArrayList<SolutionVariable> getVariables() {
    return variables;
  }
  public void setVariables(ArrayList<SolutionVariable> variables) {
    this.variables = variables;
  }
  public ArrayList<WorkflowLink> getLinks() {
    return links;
  }
  public void setLinks(ArrayList<WorkflowLink> links) {
    this.links = links;
  }
  public Workflow getModelGraph() {
    return modelGraph;
  }
  public void setModelGraph(Workflow modelGraph) {
    this.modelGraph = modelGraph;
  }
  public Workflow getWingsWorkflow() {
    return wingsWorkflow;
  }
  public void setWingsWorkflow(Workflow wingsWorkflow) {
    this.wingsWorkflow = wingsWorkflow;
  }
  public VariableGraph getGraph() {
    return graph;
  }
  public void setGraph(VariableGraph graph) {
    this.graph = graph;
  }
  
}
