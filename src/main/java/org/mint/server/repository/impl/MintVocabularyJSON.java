package org.mint.server.repository.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.configuration.plist.PropertyListConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.mint.server.classes.Region;
import org.mint.server.classes.StandardName;
import org.mint.server.classes.graph.GVariable;
import org.mint.server.classes.graph.Relation;
import org.mint.server.classes.graph.VariableGraph;
import org.mint.server.classes.model.Model;
import org.mint.server.classes.model.ModelIO;
import org.mint.server.classes.model.ModelType;
import org.mint.server.classes.model.ModelVariable;
import org.mint.server.classes.rawcag.CagEdge;
import org.mint.server.classes.rawcag.CagVariable;
import org.mint.server.classes.rawcag.RawCAG;
import org.mint.server.classes.vocabulary.TaskType;
import org.mint.server.classes.vocabulary.WorkflowPointer;
import org.mint.server.repository.MintVocabulary;
import org.mint.server.util.Config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class MintVocabularyJSON implements MintVocabulary {
  String server, storage;


  public String COMMON = "common";
  public String GRAPHS = "graphs";
  public String RAWCAGS = "rawCags";
  public String EVENTS = "event_types";
  public String REGIONS = "regions";
  public String TASKS = "task_types";
  public String QUESTIONS = "question_templates";
  public String INTERVENTIONS = "intervention_types";
  public String WORKFLOWS = "workflows";  
  public String MODELS = "models";
  public String DATASETS = "datasets";
  public String STDNAMES = "standard_names";
  
  String COMMON_DIR = COMMON;
  String GRAPHS_DIR = GRAPHS;
  String RAWCAGS_DIR = RAWCAGS;
  String EVENTS_FILE = EVENTS + ".json";
  String REGIONS_FILE = REGIONS + ".json";
  String TASKS_FILE = TASKS + ".json";
  String QUESTIONS_FILE = QUESTIONS + ".json";
  String INTERVENTIONS_FILE = INTERVENTIONS + ".json";
  String WORKFLOWS_FILE = WORKFLOWS + ".json";  
  String MODELS_FILE = MODELS + ".json";
  String DATASETS_FILE = DATASETS + ".json";
  String STDNAMES_FILE = STDNAMES + ".json";
  
  LinkedHashMap<String, Region> regions;
  LinkedHashMap<String, Region> allRegions;
  LinkedHashMap<String, VariableGraph> graphs;
  LinkedHashMap<String, TaskType> taskTypes;
  LinkedHashMap<String, StandardName> standardNames;
  LinkedHashMap<String, WorkflowPointer> workflows;
  LinkedHashMap<String, Model> models;
  LinkedHashMap<String, RawCAG> cags;
  
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
    taskTypes = new LinkedHashMap<String, TaskType>();
    standardNames = new LinkedHashMap<String, StandardName>();
    workflows = new LinkedHashMap<String, WorkflowPointer>();
    models = new LinkedHashMap<String, Model>();
    cags = new LinkedHashMap<String, RawCAG>();
    
    setConfiguration();
    load();
  }
  
  public void reload() {
    regions.clear();
    allRegions.clear();
    graphs.clear();
    taskTypes.clear();
    standardNames.clear();
    workflows.clear();
    models.clear();
    cags.clear();
    
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
  
  public String getResourceURI(String name, String dirname) {
    return this.server + "/" + COMMON_DIR + "/" + dirname + "/" + name;
  }
  
  private void load() {
    try {
      // Load vocabulary
      
      // Get all regions
      CollectionType regionListType = mapper.getTypeFactory()
          .constructCollectionType(ArrayList.class, Region.class);      
      ArrayList<Region> regions = this.mapper.readValue(
          new FileInputStream(this.getFullPath(REGIONS_FILE)), regionListType);
      this.setRegions(regions);
      
      // Get all task types
      CollectionType taskListType = mapper.getTypeFactory()
          .constructCollectionType(ArrayList.class, TaskType.class);   
      ArrayList<TaskType> taskTypes = this.mapper.readValue(
          new FileInputStream(this.getFullPath(TASKS_FILE)), taskListType);
      this.setTaskTypes(taskTypes);
      
      // Get mapping of standard names to canonical names
      CollectionType stdnamesType = mapper.getTypeFactory()
          .constructCollectionType(ArrayList.class, StandardName.class);   
      ArrayList<StandardName> standardNames = this.mapper.readValue(
          new FileInputStream(this.getFullPath(STDNAMES_FILE)), stdnamesType);
      this.setStandardNames(standardNames);
      

      // Get workflow pointers (and 
      CollectionType pointerListType = mapper.getTypeFactory()
          .constructCollectionType(ArrayList.class, WorkflowPointer.class);   
      ArrayList<WorkflowPointer> pointers = this.mapper.readValue(
          new FileInputStream(this.getFullPath(WORKFLOWS_FILE)), pointerListType);
      for(WorkflowPointer pointer : pointers) {
        this.setWorkflowComponents(pointer);
      }
      this.setWorkflows(pointers);

      
      // Load registered graphs
      this.graphs = new LinkedHashMap<String, VariableGraph>();         
      File graphdir = new File(this.getFullPath(GRAPHS_DIR));
      for(File f : FileUtils.listFiles(graphdir, TrueFileFilter.TRUE, TrueFileFilter.TRUE)) {
        VariableGraph graph = this.mapper.readValue(
            new FileInputStream(f.getAbsolutePath()), VariableGraph.class);
        this.graphs.put(graph.getID(), graph);
      }
      
      
      // Load models from Model Catalog
      this.models = new LinkedHashMap<String, Model>();
      // Set models from Model Catalog
      ArrayList<Model> modelList = this.fetchModelsFromCatalog();
      this.setModels(modelList);
      
      
      // Load registered raw cags
      this.cags = new LinkedHashMap<String, RawCAG>();         
      File cagsdir = new File(this.getFullPath(RAWCAGS_DIR));
      for(File f : FileUtils.listFiles(cagsdir, TrueFileFilter.TRUE, TrueFileFilter.TRUE)) {
        RawCAG cag = this.mapper.readValue(
            new FileInputStream(f.getAbsolutePath()), RawCAG.class);
        this.cags.put(cag.getName(), cag);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  
  private void setWorkflowComponents(WorkflowPointer pointer) {
    ArrayList<String> components = new ArrayList<String>();
    String uri = Config.get().getProperties().getString("wings.server");
    String wflowuri = uri + "/export/users/" + 
        pointer.getUserid() + "/" +
        pointer.getDomain() + "/workflows/" +
        pointer.getWorkflow() + ".owl";
    pointer.setID(wflowuri + "#" + pointer.getWorkflow());

    try {
      String json = IOUtils.toString(new URI(wflowuri+"?format=json"), StandardCharsets.UTF_8);
      Pattern comp = Pattern.compile("hasComponentBinding.+#(.+?)\"");
      for(String line : json.split("\\n")) {
        Matcher m = comp.matcher(line);
        if(m.find()) {
          components.add(m.group(1));
        }
      }
    } catch (IOException | URISyntaxException e) {
      e.printStackTrace();
    }
    pointer.setComponents(components);
  }
  
  @SuppressWarnings("unchecked")
  private ArrayList<HashMap<String, String>> queryModelCatalog(String query, HashMap<String, String> params) {
    ArrayList<HashMap<String, String>> keyValueList = new ArrayList<HashMap<String, String>>();
    String uri = Config.get().getProperties().getString("catalogs.model");
    String iouri = uri + "/" + query;
    try {
      if(params != null) {
        iouri += "?";
        for(String key : params.keySet()) {
          iouri += key + "=" + URLEncoder.encode(params.get(key), "UTF-8");
        }
      }
      String json = IOUtils.toString(new URI(iouri), StandardCharsets.UTF_8);
      HashMap<String, Object> map = 
          this.mapper.readValue(json, new TypeReference<HashMap<String, Object>>(){});
      HashMap<String, Object> results = (HashMap<String, Object>)map.get("results");
      ArrayList<HashMap<String, HashMap<String, String>>> bindings = 
          (ArrayList<HashMap<String, HashMap<String, String>>>)results.get("bindings");
      
      for(HashMap<String, HashMap<String, String>> binding: bindings) {
        HashMap<String, String> keyValues = new HashMap<String, String>();
        for(String key: binding.keySet()) {
          keyValues.put(key, binding.get(key).get("value"));
        }
        keyValueList.add(keyValues);
      }
    } catch (IOException | URISyntaxException e) {
      e.printStackTrace();
    }
    return keyValueList;
  }
  
  private ArrayList<Model> fetchModelsFromCatalog() {
    HashMap<String, ModelType> typeMap = new HashMap<String, ModelType>();
    ArrayList<HashMap<String, String>> kvlist = this.queryModelCatalog("getModels", null);
    for(HashMap<String, String> kv : kvlist) {
      String typeid = kv.get("model");
      String label = kv.get("label");
      String desc = kv.get("desc");
      String category = kv.get("categories");
      ModelType type = new ModelType();
      type.setID(typeid);
      type.setLabel(label);
      type.setCategory(category);
      type.setDescription(desc);
      typeMap.put(typeid, type);
    }

    ArrayList<Model> modelList = new ArrayList<Model>();
    kvlist = this.queryModelCatalog("getModelConfigurations", null);
    for(HashMap<String, String> kv : kvlist) {
      String modelid = kv.get("model_config");
      String label = kv.get("label");
      String typeid = kv.get("model");
      ModelType type = typeMap.get(typeid);
      
      Model m = new Model();
      m.setID(modelid);
      m.setLabel(label);
      m.setType(type);
      
      m = this.fetchModelIO(m);
      modelList.add(m);
    }
    return modelList;
  }
  
  private Model fetchModelIO(Model model) {
    HashMap<String, ModelIO> inputs = new HashMap<String, ModelIO>();
    HashMap<String, ModelIO> outputs = new HashMap<String, ModelIO>();
    
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("config", model.getID());
    ArrayList<HashMap<String, String>> kvlist = 
        this.queryModelCatalog("getConfigI_OVariablesAndStandardNames", params);    

    for(HashMap<String, String> kv: kvlist) {
      String ioprop = kv.get("prop");
      String ioid = kv.get("io");
      String iotype = kv.get("type");
      String vid = kv.get("vp");
      String units = kv.get("units");
      String stdname = kv.get("st");
      /*
      if(stdname == null) {
        stdname = vid.substring(vid.indexOf("/")+1);
        stdname = vid.substring(vid.indexOf("#")+1);
      }
      */
      
      boolean isinput = ioprop.endsWith("hasInput");
      ModelIO io = isinput ? inputs.get(ioid) : outputs.get(ioid);
      if(io == null)
        io = new ModelIO(ioid);
      
      io.setType(iotype);
      
      ModelVariable mv = new ModelVariable();
      mv.setID(vid);
      mv.setUnits(units);
      mv.setStandard_name(stdname);
      io.addVariable(mv);
      if(isinput)
        inputs.put(ioid, io);
      else
        outputs.put(ioid, io);
    }
    
    model.setInputs(new ArrayList<ModelIO>(inputs.values()));
    model.setOutputs(new ArrayList<ModelIO>(outputs.values()));
    return model;
  }
  
  public VariableGraph convertRawCagToVariableGraph(String cagname) {
    RawCAG cag = this.getRawCAG(cagname);
    String graphid = this.server + "/common/graphs/" + cag.getName();
    String graphfile = this.storage + "/common/graphs/" + cag.getName() + ".json";
    
    VariableGraph graph = this.getGraph(graphid);
    if(graph != null)
      return graph;
    
    graph = new VariableGraph();
    graph.setID(graphid);
    graph.setLabel(cag.getName());
    
    HashMap<String, GVariable> variables = new HashMap<String, GVariable>();
    for(CagVariable v : cag.getVariables()) {
      GVariable gv = new GVariable();
      gv.setLabel(v.getName());
      gv.setID(graphid + "#" + v.getName());
      if(v.getAlignment() != null) {
        ArrayList<String> standard_names = new ArrayList<String>();
        for(ArrayList<Object> items : v.getAlignment()) {
          String stdname = items.get(0).toString();
          standard_names.add(stdname);
        }
        gv.setStandard_names(standard_names);
      }
      variables.put(v.getName(), gv);
    }
    
    ArrayList<Relation> links = new ArrayList<Relation>();
    for(CagEdge edge : cag.getEdge_data()) {
      GVariable from = variables.get(edge.getSource());
      GVariable to = variables.get(edge.getTarget());
      if(from != null && to != null) {
        Relation link = new Relation();
        link.setFrom(from.getID());
        link.setTo(to.getID());
        links.add(link);
      }
    }
    graph.setVariables(new ArrayList<GVariable>(variables.values()));
    graph.setLinks(links);
    try {
      this.mapper.writerWithDefaultPrettyPrinter().writeValue(
          new FileOutputStream(graphfile), graph);
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.graphs.put(graph.getID(), graph);
    return graph;
  }

  private String getGraphFile(String graphid) {
    String graphdir = this.getFullPath(GRAPHS_DIR);
    File dir = new File(graphdir);
    if(!dir.exists()) {
      dir.mkdir();
    }
    return graphdir + File.separator + graphid + ".json";
  }
  
  private void writeGraph(VariableGraph graph) {
    String gname = graph.getID().substring(graph.getID().lastIndexOf("/"));
    try {
      this.mapper.writerWithDefaultPrettyPrinter().writeValue(
          new FileOutputStream(this.getGraphFile(gname)), graph);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public String addVariableGraph(VariableGraph graph) {
    this.writeGraph(graph);
    this.graphs.put(graph.getID(), graph);
    return graph.getID();
  }
  
  public void updateVariableGraph(VariableGraph graph) {
    this.graphs.put(graph.getID(), graph);
    this.writeGraph(graph);
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

  public ArrayList<TaskType> getTaskTypes() {
    return new ArrayList<TaskType>(taskTypes.values());
  }

  public void setTaskTypes(ArrayList<TaskType> taskTypes) {
    for(TaskType taskType : taskTypes)
      this.taskTypes.put(taskType.getID(), taskType);
  }
  
  public ArrayList<StandardName> getStandardNames() {
    return new ArrayList<StandardName>(standardNames.values());
  }

  public void setStandardNames(ArrayList<StandardName> standardNames) {
    for(StandardName standardName : standardNames) {
      this.standardNames.put(standardName.getId(), standardName);
    }
  }
  
  public boolean areStandardNamesCompatible(String id1, String id2) {
    String cname1 = this.getCanonicalName(id1);
    String cname2 = this.getCanonicalName(id2);
    return cname1.equals(cname2);
  }
  
  public String getCanonicalName(String stdname_id) {
    String cname = stdname_id;
    StandardName stdname = this.standardNames.get(stdname_id);
    if(stdname != null && stdname.getCanonical_name() != null)
      cname = stdname.getCanonical_name();
    return cname;
  }
  
  public String getCanonicalName(ArrayList<String> stdname_ids) {
    String cname = null;
    for(String stdname_id : stdname_ids) {
      cname = this.getCanonicalName(stdname_id);
      if(!cname.equals(stdname_id))
        return cname;
    }
    return cname;
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

  public ArrayList<RawCAG> getRawCAGs() {
    return new ArrayList<RawCAG>(this.cags.values());
  }

  public void setRawCAGs(ArrayList<RawCAG> cags) {
    for(RawCAG cag : cags)
      this.cags.put(cag.getName(), cag);
  }
  
  @Override
  public RawCAG getRawCAG(String id) {
    return this.cags.get(id);
  }

}
