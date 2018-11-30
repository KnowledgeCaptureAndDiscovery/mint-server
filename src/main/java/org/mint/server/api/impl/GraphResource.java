package org.mint.server.api.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import org.mint.server.classes.graph.VariableGraph;
import org.mint.server.repository.MintRepository;
import org.mint.server.repository.impl.MINTRepositoryJSON;

import com.fasterxml.jackson.annotation.JsonProperty;

@Path("users/{userid}/graphs")
public class GraphResource {
  MintRepository repo;
  
  @Context
  HttpServletRequest request;
  
  @PathParam("userid") String userid;
  
  @POST
  public String addGraph(@JsonProperty("graph") VariableGraph graph) {
    return MINTRepositoryJSON.get(userid).addVariableGraph(graph);
  }
  
  @PUT
  @Path("{graphid}")
  public void updateGraph(@JsonProperty("graph") VariableGraph graph) {
    MINTRepositoryJSON.get(userid).updateVariableGraph(graph);
  }
  
  @GET
  @Path("{graphid}")
  public VariableGraph getGraph(@PathParam("graphid") String graphid) {
    return MINTRepositoryJSON.get(userid).getVariableGraph(graphid);
  }
  
  @DELETE
  @Path("{graphid}")
  public void deleteGraph(@PathParam("graphid") String graphid) {
    MINTRepositoryJSON.get(userid).deleteVariableGraph(graphid);
  }
}
