package org.mint.server.api.impl;

import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.mint.server.classes.Region;
import org.mint.server.classes.StandardName;
import org.mint.server.classes.model.Model;
import org.mint.server.classes.util.StorageHandler;
import org.mint.server.classes.vocabulary.TaskType;
import org.mint.server.classes.vocabulary.WorkflowPointer;
import org.mint.server.repository.impl.MintVocabularyJSON;
import org.mint.server.util.Config;

@Path("common")
public class CommonResource {
  
  @Context
  HttpServletRequest request;
  
  @Context
  ServletContext context;
  
  @GET
  @Path("reload")
  public String reload() {
    MintVocabularyJSON.get().reload(true);
    return "OK";
  }
  
  @GET
  @Path("reload/config")
  public String reloadConfig() {
    MintVocabularyJSON.get().reload(false);
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
  @Path("geo/{regionid}")
  @Produces("application/json")
  public Response getGeoJson(@PathParam("regionid") String regionid) {
    MintVocabularyJSON vocab = MintVocabularyJSON.get();
    String fullpath = vocab.getBoundaryFile(regionid);
    return StorageHandler.streamFile(fullpath, context);
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
  @Path("workflows")
  @Produces("application/json")
  public List<WorkflowPointer> getWorkflowPointers() {
    return MintVocabularyJSON.get().getWorkflows();
  }

}