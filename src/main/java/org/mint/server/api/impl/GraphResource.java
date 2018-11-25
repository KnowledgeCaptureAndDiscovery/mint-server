package org.mint.server.api.impl;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;

import org.mint.server.classes.graph.VariableGraph;
import org.mint.server.repository.MintRepository;
import org.mint.server.repository.impl.MINTRepositoryJSON;

@Path("users/{userid}/graphs")
public class GraphResource {
  MintRepository repo;
  
  @Context
  HttpServletRequest request;
  
  @PathParam("userid") String userid;

  @GET
  @Path("{graphid}")
  public VariableGraph getGraph(@PathParam("graphid") String graphid) {
    return MINTRepositoryJSON.get(userid).getVariableGraph(graphid);
  }
}
