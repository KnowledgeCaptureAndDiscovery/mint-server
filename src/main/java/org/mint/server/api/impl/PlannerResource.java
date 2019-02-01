package org.mint.server.api.impl;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.mint.server.classes.DataSpecification;
import org.mint.server.classes.graph.VariableGraph;
import org.mint.server.classes.question.ModelingQuestion;
import org.mint.server.planner.WorkflowSolution;
import org.mint.server.repository.impl.MINTRepositoryJSON;


@Path("users/{userid}/regions/{regionid}/questions/{questionid}/planner")
public class PlannerResource {
  
  @Context
  HttpServletRequest request;
  
  @PathParam("userid") String userid;
  @PathParam("regionid") String regionid;
  @PathParam("questionid") String questionid;
  
  @GET
  @Path("compose/{dsid}")
  @Produces("application/json")
  public ArrayList<WorkflowSolution> createWorkflowSolutions(@PathParam("dsid") String dsid) {
    MINTRepositoryJSON repo = MINTRepositoryJSON.get(userid, regionid);
    String quri = repo.getQuestionURI(questionid);
    ModelingQuestion question = repo.getModelingQuestionDetails(quri);
    // The graph name is the same as the region name
    VariableGraph graph = repo.getVariableGraph();
    String dsuri = repo.getDataSpecificationURI(questionid, dsid);
    DataSpecification ds = repo.getDataSpecificationDetails(questionid, dsuri);
    return repo.createWorkflowSolutions(question, graph, ds);
  }
}
