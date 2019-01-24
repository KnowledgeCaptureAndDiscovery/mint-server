package org.mint.server.api.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.mint.server.classes.DataSpecification;
import org.mint.server.repository.impl.MINTRepositoryJSON;

import com.fasterxml.jackson.annotation.JsonProperty;

@Path("users/{userid}/questions/{questionid}/data")
public class DataResource {
  
  @Context
  HttpServletRequest request;
  
  @PathParam("userid") String userid;
  @PathParam("questionid") String questionid;
  
  @GET
  @Produces("application/json")
  public List<DataSpecification> listDataSpecifications() {
    return MINTRepositoryJSON.get(userid).listDataSpecifications(questionid);
  }
  
  @GET
  @Path("{dsid}")
  @Produces("application/json")
  public DataSpecification getDataSpecification(@PathParam("dsid") String dsname) {
    String dsid = MINTRepositoryJSON.get(userid).getDataSpecificationURI(questionid, dsname);
    return MINTRepositoryJSON.get(userid).getDataSpecificationDetails(questionid, dsid);
  }
  
  @POST
  @Produces("application/json")
  public String addDataSpecification(@JsonProperty("ds") DataSpecification ds) {
    MINTRepositoryJSON repo = MINTRepositoryJSON.get(userid);
    String dsid = repo.getRandomID("ds-");
    ds.setID(repo.getDataSpecificationURI(questionid, dsid));
    return repo.addDataSpecification(questionid, ds);
  }
  
  @DELETE
  @Path("{dsid}")
  @Produces("application/json")
  public void deleteDataSpecification(@PathParam("dsid") String dsname) {
    String dsid = MINTRepositoryJSON.get(userid).getDataSpecificationURI(questionid, dsname);    
    MINTRepositoryJSON.get(userid).deleteDataSpecification(questionid, dsid);
  }
  
  @PUT
  @Path("{dsid}")
  @Produces("application/json")
  public void updateDataSpecification(@JsonProperty("ds") DataSpecification ds) {
    MINTRepositoryJSON.get(userid).updateDataSpecification(questionid, ds);
  }
}
