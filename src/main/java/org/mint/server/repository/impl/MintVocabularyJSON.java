package org.mint.server.repository.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.commons.configuration.plist.PropertyListConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.mint.server.classes.Region;
import org.mint.server.classes.graph.VariableGraph;
import org.mint.server.classes.model.Model;
import org.mint.server.classes.vocabulary.EventType;
import org.mint.server.classes.vocabulary.InterventionType;
import org.mint.server.classes.vocabulary.QuestionTemplate;
import org.mint.server.classes.vocabulary.TaskType;
import org.mint.server.classes.vocabulary.WorkflowPointer;
import org.mint.server.repository.MintVocabulary;
import org.mint.server.util.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class MintVocabularyJSON implements MintVocabulary {
  String server, storage;

  String COMMON_DIR = "common";
  String GRAPHS_DIR = "graphs";
  String EVENTS_FILE = "event_types.json";
  String REGIONS_FILE = "regions.json";
  String TASKS_FILE = "task_types.json";
  String QUESTIONS_FILE = "question_templates.json";
  String INTERVENTIONS_FILE = "intervention_types.json";
  String WORKFLOWS_FILE = "workflows.json";  
  String MODELS_FILE = "models.json";
  
  LinkedHashMap<String, Region> regions;
  LinkedHashMap<String, Region> allRegions;
  LinkedHashMap<String, VariableGraph> graphs;
  LinkedHashMap<String, EventType> eventTypes;
  LinkedHashMap<String, InterventionType> interventionTypes;
  LinkedHashMap<String, QuestionTemplate> questionTemplates;
  LinkedHashMap<String, TaskType> taskTypes;
  LinkedHashMap<String, WorkflowPointer> workflows;
  LinkedHashMap<String, Model> models;
  
  ObjectMapper mapper;

  static MintVocabularyJSON singleton = null;

  public static MintVocabularyJSON get() {
    if(singleton == null)
      singleton = new MintVocabularyJSON();
    return singleton;
  }
  
  public MintVocabularyJSON() {
    regions = new LinkedHashMap<String, Region>();
    allRegions = new LinkedHashMap<String, Region>();
    graphs = new LinkedHashMap<String, VariableGraph>();
    eventTypes = new LinkedHashMap<String, EventType>();
    interventionTypes = new LinkedHashMap<String, InterventionType>();
    questionTemplates = new LinkedHashMap<String, QuestionTemplate>();
    taskTypes = new LinkedHashMap<String, TaskType>();
    workflows = new LinkedHashMap<String, WorkflowPointer>();
    models = new LinkedHashMap<String, Model>();
    
    setConfiguration();
    load();
  }
  
  public void reload() {
    this.load();
  }
  
  /* Helper Functions */
  
  private void setConfiguration() {
    PropertyListConfiguration props = Config.get().getProperties();
    this.server = props.getString("server");
    this.storage = props.getString("storage");
    this.mapper = new ObjectMapper();
    
    File dirf = new File(this.storage);
    if(!dirf.exists() && !dirf.mkdirs()) {
      System.err.println("Cannot create storage directory : "+dirf.getAbsolutePath());
    }
  }
  
  private String getFullPath(String path) {
    return this.storage + File.separator + COMMON_DIR + File.separator + path;
  }
  
  private void load() {
    try {
      CollectionType regionListType = mapper.getTypeFactory()
          .constructCollectionType(ArrayList.class, Region.class);      
      ArrayList<Region> regions = this.mapper.readValue(
          new FileInputStream(this.getFullPath(REGIONS_FILE)), regionListType);
      this.setRegions(regions);
      
      CollectionType eventListType = mapper.getTypeFactory()
          .constructCollectionType(ArrayList.class, EventType.class);   
      ArrayList<EventType> eventTypes = this.mapper.readValue(
          new FileInputStream(this.getFullPath(EVENTS_FILE)), eventListType);
      this.setEventTypes(eventTypes);

      CollectionType interventionListType = mapper.getTypeFactory()
          .constructCollectionType(ArrayList.class, InterventionType.class);   
      ArrayList<InterventionType> interventionTypes = this.mapper.readValue(
          new FileInputStream(this.getFullPath(INTERVENTIONS_FILE)), interventionListType);
      this.setInterventionTypes(interventionTypes);
      
      CollectionType questionListType = mapper.getTypeFactory()
          .constructCollectionType(ArrayList.class, QuestionTemplate.class);   
      ArrayList<QuestionTemplate> questionTemplates = this.mapper.readValue(
          new FileInputStream(this.getFullPath(QUESTIONS_FILE)), questionListType);
      this.setQuestionTemplates(questionTemplates);
      
      CollectionType taskListType = mapper.getTypeFactory()
          .constructCollectionType(ArrayList.class, TaskType.class);   
      ArrayList<TaskType> taskTypes = this.mapper.readValue(
          new FileInputStream(this.getFullPath(TASKS_FILE)), taskListType);
      this.setTaskTypes(taskTypes);
      
      /*CollectionType workflowListType = mapper.getTypeFactory()
          .constructCollectionType(ArrayList.class, TaskType.class);   
      ArrayList<WorkflowPointer> workflows = this.mapper.readValue(
          new FileInputStream(this.getFullPath(WORKFLOWS_FILE)), workflowListType);
      this.setWorkflows(workflows);*/

      this.graphs = new LinkedHashMap<String, VariableGraph>();         
      File graphdir = new File(this.getFullPath(GRAPHS_DIR));
      for(File f : FileUtils.listFiles(graphdir, TrueFileFilter.TRUE, TrueFileFilter.TRUE)) {
        VariableGraph graph = this.mapper.readValue(
            new FileInputStream(f.getAbsolutePath()), VariableGraph.class);
        this.graphs.put(graph.getID(), graph);
      }
      
      this.models = new LinkedHashMap<String, Model>();
      CollectionType modelListType = mapper.getTypeFactory()
          .constructCollectionType(ArrayList.class, Model.class);   
      ArrayList<Model> modelList = this.mapper.readValue(
          new FileInputStream(this.getFullPath(MODELS_FILE)), modelListType);
      this.setModels(modelList);
      
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<Region> getRegions() {
    return new ArrayList<Region>(this.regions.values());
  }

  public Region getRegion(String id) {
    return this.allRegions.get(id);
  }
  
  public void setRegions(ArrayList<Region> regions) {
    for(Region region : regions)
      this.regions.put(region.getID(), region);
    
    this.setAllRegions(regions, null);
  }

  private void setAllRegions(ArrayList<Region> regions, String parentid) {
    for(Region region : regions) {
      region.setParentRegion(parentid);
      this.allRegions.put(region.getID(), region);
      if(region.getSubRegions() != null)
        this.setAllRegions(region.getSubRegions(), region.getID());
    }
  }

  public ArrayList<EventType> getEventTypes() {
    return new ArrayList<EventType>(this.eventTypes.values());
  }
  
  public EventType getEventType(String id) {
    return this.eventTypes.get(id);
  }

  public void setEventTypes(ArrayList<EventType> eventTypes) {
    for(EventType eventType : eventTypes)
      this.eventTypes.put(eventType.getID(), eventType);
  }

  public ArrayList<InterventionType> getInterventionTypes() {
    return new ArrayList<InterventionType>(this.interventionTypes.values());
  }

  public void setInterventionTypes(ArrayList<InterventionType> interventionTypes) {
    for(InterventionType itype : interventionTypes)
      this.interventionTypes.put(itype.getID(), itype);
  }

  public ArrayList<QuestionTemplate> getQuestionTemplates() {
    return new ArrayList<QuestionTemplate>(questionTemplates.values());
  }

  public void setQuestionTemplates(ArrayList<QuestionTemplate> questionTemplates) {
    for(QuestionTemplate template : questionTemplates)
      this.questionTemplates.put(template.getID(), template);
  }

  public ArrayList<TaskType> getTaskTypes() {
    return new ArrayList<TaskType>(taskTypes.values());
  }

  public void setTaskTypes(ArrayList<TaskType> taskTypes) {
    for(TaskType taskType : taskTypes)
      this.taskTypes.put(taskType.getID(), taskType);
  }
  
  public Model getModel(String id) {
    return this.models.get(id);
  }
  
  public void setModels(ArrayList<Model> models) {
    for(Model model : models) {
      this.models.put(model.getID(), model);
    }
  }

  public ArrayList<Model> getModels() {
    return new ArrayList<Model>(models.values());
  }
  
  public ArrayList<WorkflowPointer> getWorkflows() {
    return new ArrayList<WorkflowPointer>(workflows.values());
  }

  public void setWorkflows(ArrayList<WorkflowPointer> workflows) {
    for(WorkflowPointer workflow : workflows)
      this.workflows.put(workflow.getID(), workflow);
  }

  public ArrayList<VariableGraph> getGraphs() {
    return new ArrayList<VariableGraph>(graphs.values());
  }

  public void setGraphs(ArrayList<VariableGraph> graphs) {
    for(VariableGraph graph : graphs)
      this.graphs.put(graph.getID(), graph);
  }

  @Override
  public QuestionTemplate getQuestionTemplate(String id) {
    return this.questionTemplates.get(id);
  }

  @Override
  public InterventionType getInterventionType(String id) {
    return this.interventionTypes.get(id);
  }

  @Override
  public TaskType getTaskType(String id) {
    return this.taskTypes.get(id);
  }

  @Override
  public WorkflowPointer getWorkflow(String id) {
    return this.workflows.get(id);
  }

  @Override
  public VariableGraph getGraph(String id) {
    return this.graphs.get(id);
  }

}
