package org.mint.server.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.mint.server.classes.Dataset;
import org.mint.server.classes.Region;
import org.mint.server.classes.StandardName;
import org.mint.server.classes.URIEntity;
import org.mint.server.classes.graph.VariableGraph;
import org.mint.server.classes.model.Model;
import org.mint.server.classes.rawcag.RawCAG;
import org.mint.server.classes.vocabulary.EventType;
import org.mint.server.classes.vocabulary.InterventionType;
import org.mint.server.classes.vocabulary.QuestionTemplate;
import org.mint.server.classes.vocabulary.TaskType;
import org.mint.server.repository.MintRepository;
import org.mint.server.repository.impl.MintVocabularyJSON;
import org.mint.server.util.Config;

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
  public Region getRegion(@PathParam("regionid") String region_name) {
    MintVocabularyJSON vocab = MintVocabularyJSON.get();
    String regionid = vocab.getResourceURI(region_name, vocab.REGIONS);
    return vocab.getRegion(regionid);
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
  public VariableGraph getGraph(@PathParam("graphid") String gname) {
    MintVocabularyJSON vocab = MintVocabularyJSON.get();
    String graphid = vocab.getResourceURI(gname, vocab.GRAPHS);
    return MintVocabularyJSON.get().getGraph(graphid);
  }
  
  @GET
  @Path("rawcags")
  public ArrayList<URIEntity> listRawCAGS() {
    MintVocabularyJSON vocab = MintVocabularyJSON.get();
    ArrayList<URIEntity> cagIds = new ArrayList<URIEntity>();
    for(RawCAG cag : vocab.getRawCAGs()) {
      String cagid = vocab.getResourceURI(cag.getName(), vocab.RAWCAGS);
      cagIds.add(new URIEntity(cagid, cag.getName()));
    }
    return cagIds;
  }
  
  @GET
  @Path("rawcags/{cagname}")
  public RawCAG getRawCAG(@PathParam("cagname") String cagname) {
    return MintVocabularyJSON.get().getRawCAG(cagname);
  }
  
  @GET
  @Path("graphs/fromraw/{cagname}")
  public VariableGraph convertRawCagToVariableGraph(@PathParam("cagname") String cagname) {
    return MintVocabularyJSON.get().convertRawCagToVariableGraph(cagname);
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
    MintVocabularyJSON vocab = MintVocabularyJSON.get();
    String task_type_id = vocab.getResourceURI(task_type, vocab.TASKS);
    return vocab.getTaskType(task_type_id);
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
  public QuestionTemplate getQuestionTemplate(@PathParam("question_template") String template_name) {
    MintVocabularyJSON vocab = MintVocabularyJSON.get();
    String templateid = vocab.getResourceURI(template_name, vocab.QUESTIONS);
    return vocab.getQuestionTemplate(templateid);
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
  public EventType getEventTypes(@PathParam("event_type") String typename) {
    MintVocabularyJSON vocab = MintVocabularyJSON.get();
    String typeid = vocab.getResourceURI(typename, vocab.EVENTS);
    return vocab.getEventType(typeid);
  }
  
  @GET
  @Path("models")
  @Produces("application/json")
  public List<Model> getModels() {
    return MintVocabularyJSON.get().getModels();
  }
  
  @GET
  @Path("config")
  @Produces("application/json")
  public HashMap<String, Object> getConfig() {
    return Config.get().getPropertiesMap();
  }
  
  @GET
  @Path("standard_names")
  @Produces("application/json")
  public List<StandardName> getStandardNames() {
    return MintVocabularyJSON.get().getStandardNames();
  }
  
  @GET
  @Path("datasets")
  @Produces("application/json")
  public List<Dataset> getDatasets() {
    return MintVocabularyJSON.get().getDatasets();
  }
}