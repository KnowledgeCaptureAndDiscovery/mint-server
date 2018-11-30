package org.mint.server.api.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.mint.server.classes.workflow.TempWorkflowDetails;
import org.mint.server.repository.impl.MINTRepositoryJSON;

import com.fasterxml.jackson.annotation.JsonProperty;

@Path("users/{userid}/questions/{questionid}/workflows")
public class WorkflowResource {

  @Context
  HttpServletRequest request;
  
  @PathParam("userid") String userid;
  @PathParam("questionid") String questionid;
  
  @POST
  @Produces("application/json")
  public String addWorkflow(@JsonProperty("wflow") TempWorkflowDetails wflow) {
    return MINTRepositoryJSON.get(userid).saveWorkflow(questionid, wflow);
  }
  
  @GET
  @Produces("application/json")
  public TempWorkflowDetails getWorkflow() {
    return MINTRepositoryJSON.get(userid).getWorkflow(questionid);
  }
}
