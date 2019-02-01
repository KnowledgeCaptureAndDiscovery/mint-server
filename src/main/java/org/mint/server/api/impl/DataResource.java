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

@Path("users/{userid}/regions/{regionid}/questions/{questionid}/data")
public class DataResource {
  
  @Context
  HttpServletRequest request;
  
  @PathParam("userid") String userid;
  @PathParam("regionid") String regionid;
  @PathParam("questionid") String questionid;
  
  @GET
  @Produces("application/json")
  public List<DataSpecification> listDataSpecifications() {
    return MINTRepositoryJSON.get(userid, regionid).listDataSpecifications(questionid);
  }
  
  @GET
  @Path("{dsid}")
  @Produces("application/json")
  public DataSpecification getDataSpecification(@PathParam("dsid") String dsname) {
    String dsid = MINTRepositoryJSON.get(userid, regionid).getDataSpecificationURI(questionid, dsname);
    return MINTRepositoryJSON.get(userid, regionid).getDataSpecificationDetails(questionid, dsid);
  }
  
  @POST
  @Produces("application/json")
  public String addDataSpecification(@JsonProperty("ds") DataSpecification ds) {
    MINTRepositoryJSON repo = MINTRepositoryJSON.get(userid, regionid);
    String dsid = repo.getRandomID("ds-");
    ds.setID(repo.getDataSpecificationURI(questionid, dsid));
    return repo.addDataSpecification(questionid, ds);
  }
  
  @DELETE
  @Path("{dsid}")
  @Produces("application/json")
  public void deleteDataSpecification(@PathParam("dsid") String dsname) {
    String dsid = MINTRepositoryJSON.get(userid, regionid).getDataSpecificationURI(questionid, dsname);    
    MINTRepositoryJSON.get(userid, regionid).deleteDataSpecification(questionid, dsid);
  }
  
  @PUT
  @Path("{dsid}")
  @Produces("application/json")
  public void updateDataSpecification(@JsonProperty("ds") DataSpecification ds) {
    MINTRepositoryJSON.get(userid, regionid).updateDataSpecification(questionid, ds);
  }
}
