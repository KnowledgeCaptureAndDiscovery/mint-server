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

@Path("users/{userid}/regions/{regionid}/cag")
public class GraphResource {
  MintRepository repo;
  
  @Context
  HttpServletRequest request;
  
  @PathParam("userid") String userid;
  @PathParam("regionid") String regionid;
  
  @POST
  public String addGraph(@JsonProperty("graph") VariableGraph graph) {
    return MINTRepositoryJSON.get(userid, regionid).addVariableGraph(graph);
  }
  
  @PUT
  public void updateGraph(@JsonProperty("graph") VariableGraph graph) {
    MINTRepositoryJSON.get(userid, regionid).updateVariableGraph(graph);
  }
  
  @GET
  public VariableGraph getGraph() {
    return MINTRepositoryJSON.get(userid, regionid).getVariableGraph();
  }
  
  @DELETE
  public void deleteGraph() {
    MINTRepositoryJSON.get(userid, regionid).deleteVariableGraph();
  }
}
