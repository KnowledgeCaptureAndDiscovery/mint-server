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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import org.mint.server.classes.question.ModelingQuestion;
import org.mint.server.repository.impl.MINTRepositoryJSON;

import com.fasterxml.jackson.annotation.JsonProperty;

@Path("users/{userid}/questions")
public class QuestionResource {  
  @Context
  HttpServletRequest request;
  
  @PathParam("userid") String userid;

  /**
   * Queries
   */
  @GET
  @Produces("application/json")
  public List<ModelingQuestion> listModelingQuestions(@QueryParam("region") String regionid) {
    return MINTRepositoryJSON.get(userid).listModelingQuestions(regionid);
  }
  
  @GET
  @Path("{questionid}")
  public ModelingQuestion getModelingQuestion(@PathParam("questionid") String qname) {
    String questionid = MINTRepositoryJSON.get(userid).getQuestionURI(qname);
    return MINTRepositoryJSON.get(userid).getModelingQuestionDetails(questionid);
  }
  
  @POST
  @Produces("application/json")
  public String addModelingQuestion(@JsonProperty("question") ModelingQuestion question) {
    MINTRepositoryJSON repo = MINTRepositoryJSON.get(userid);
    String questionid = repo.getRandomID("question-");
    question.setID(repo.getQuestionURI(questionid));
    return repo.addModelingQuestion(question);
  }
  
  @DELETE
  @Path("{questionid}")
  @Produces("application/json")
  public void deleteModelingQuestion(@PathParam("questionid") String qname) {
    String questionid = MINTRepositoryJSON.get(userid).getQuestionURI(qname);
    MINTRepositoryJSON.get(userid).deleteModelingQuestion(questionid);
  }
  
  @PUT
  @Path("{questionid}")
  @Produces("application/json")
  public void updateModelingQuestion(@JsonProperty("question") ModelingQuestion question) {
    MINTRepositoryJSON.get(userid).updateModelingQuestion(question);
  }
}
