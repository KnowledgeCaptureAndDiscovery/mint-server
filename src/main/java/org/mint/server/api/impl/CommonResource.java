package org.mint.server.api.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.mint.server.classes.Region;
import org.mint.server.classes.URIEntity;
import org.mint.server.classes.graph.VariableGraph;
import org.mint.server.classes.vocabulary.EventType;
import org.mint.server.classes.vocabulary.InterventionType;
import org.mint.server.classes.vocabulary.QuestionTemplate;
import org.mint.server.classes.vocabulary.TaskType;
import org.mint.server.repository.MintRepository;
import org.mint.server.repository.impl.MintVocabularyJSON;

@Path("common")
public class CommonResource {
  MintRepository repo;
  
  @Context
  HttpServletRequest request;
  
  @GET
  @Path("reload")
  public String reload() {
    MintVocabularyJSON.get().reload();
    return "OK";
  }
  
  @GET
  @Path("regions")
  @Produces("application/json")
  public List<Region> listRegions() {
    return MintVocabularyJSON.get().getRegions();
  }
  
  @GET
  @Path("regions/{regionid}")
  public Region getRegion(@PathParam("regionid") String regionid) {
    regionid = request.getRequestURL().toString(); // Need the full url
    return MintVocabularyJSON.get().getRegion(regionid);
  }

  @GET
  @Path("graphs")
  public ArrayList<URIEntity> listGraphs() {
    ArrayList<URIEntity> graphIds = new ArrayList<URIEntity>();
    for(VariableGraph graph : MintVocabularyJSON.get().getGraphs()) {
      graphIds.add(new URIEntity(graph.getID(), graph.getLabel()));
    }
    return graphIds;
  }
  
  @GET
  @Path("graphs/{graphid}")
  public VariableGraph getGraph(@PathParam("graphid") String graphid) {
    graphid = request.getRequestURL().toString();
    return MintVocabularyJSON.get().getGraph(graphid);
  }
  
  @GET
  @Path("task_types")
  @Produces("application/json")
  public List<TaskType> listTaskTypes() {
    return MintVocabularyJSON.get().getTaskTypes();
  }
  
  @GET
  @Path("task_types/{task_type}")
  @Produces("application/json")
  public TaskType getTaskType(@PathParam("task_type") String task_type) {
    task_type = request.getRequestURL().toString();
    return MintVocabularyJSON.get().getTaskType(task_type);
  }  
  
  @GET
  @Path("question_templates")
  @Produces("application/json")
  public List<QuestionTemplate> listQuestionTemplates() {
    return MintVocabularyJSON.get().getQuestionTemplates();
  }
  
  @GET
  @Path("question_templates/{templateid}")
  @Produces("application/json")
  public QuestionTemplate getQuestionTemplate(@PathParam("question_template") String templateid) {
    templateid = request.getRequestURL().toString();
    return MintVocabularyJSON.get().getQuestionTemplate(templateid);
  }

  @GET
  @Path("event_types")
  @Produces("application/json")
  public List<EventType> listEventTypes() {
    return MintVocabularyJSON.get().getEventTypes();
  }
  
  @GET
  @Path("intervention_types")
  @Produces("application/json")
  public List<InterventionType> listInterventionTypes() {
    return MintVocabularyJSON.get().getInterventionTypes();
  }
  
  @GET
  @Path("event_types/{event_type}")
  @Produces("application/json")
  public EventType getEventTypes(@PathParam("event_type") String typeid) {
    typeid = request.getRequestURL().toString();
    return MintVocabularyJSON.get().getEventType(typeid);
  }
}