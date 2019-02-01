package org.mint.server.repository.impl;

import java.io.File;
import java.io.FileInputStream;
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
import org.apache.commons.io.IOUtils;
import org.mint.server.classes.Region;
import org.mint.server.classes.StandardName;
import org.mint.server.classes.model.Model;
import org.mint.server.classes.model.ModelIO;
import org.mint.server.classes.model.ModelParameter;
import org.mint.server.classes.model.ModelType;
import org.mint.server.classes.model.ModelVariable;
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
  LinkedHashMap<String, TaskType> taskTypes;
  LinkedHashMap<String, StandardName> standardNames;
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
    taskTypes = new LinkedHashMap<String, TaskType>();
    standardNames = new LinkedHashMap<String, StandardName>();
    workflows = new LinkedHashMap<String, WorkflowPointer>();
    models = new LinkedHashMap<String, Model>();
    
    setConfiguration();
    load();
  }
  
  public void reload() {
    regions.clear();
    allRegions.clear();
    taskTypes.clear();
    standardNames.clear();
    workflows.clear();
    models.clear();
    
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
      
      // Load models from Model Catalog
      this.models = new LinkedHashMap<String, Model>();
      // Set models from Model Catalog
      ArrayList<Model> modelList = this.fetchModelsFromCatalog();
      this.setModels(modelList);

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
      String iolabel = kv.get("iolabel");
      String iotype = kv.get("type");
      String ioformat = kv.get("format");
      String vid = kv.get("vp");
      String units = kv.get("units");
      String stdname = kv.get("st");
      String dimensionality = kv.get("dim");
      
      boolean isinput = ioprop.endsWith("hasInput");
      ModelIO io = isinput ? inputs.get(ioid) : outputs.get(ioid);
      if(io == null)
        io = new ModelIO(ioid);
      
      io.setType(iotype);
      io.setFormat(ioformat);
      io.setDimensionality(0);
      if(iolabel != null) {
        io.setLabel(iolabel);
      }
      if(dimensionality != null) {
        int dim = Integer.parseInt(dimensionality);
        io.setDimensionality(dim);
      }
      
      if(vid != null) {        
        ModelVariable mv = new ModelVariable();
        mv.setID(vid);
        mv.setUnits(units);
        mv.setStandard_name(stdname);
        io.addVariable(mv);
      }
      if(isinput)
        inputs.put(ioid, io);
      else
        outputs.put(ioid, io);
    }
    
    model.setInputs(new ArrayList<ModelIO>(inputs.values()));
    model.setOutputs(new ArrayList<ModelIO>(outputs.values()));
    model = this.fetchModelParamters(model);
    return model;
  }
  
  private Model fetchModelParamters(Model model) {
    HashMap<String, String>queryParams = new HashMap<String, String>();
    queryParams.put("config", model.getID());
    ArrayList<HashMap<String, String>> kvlist = 
        this.queryModelCatalog("getConfigIParameters", queryParams);

    ArrayList<ModelParameter> parameters = new ArrayList<ModelParameter>(); 
    for(HashMap<String, String> kv: kvlist) {
      String pid = kv.get("p");
      String plabel = kv.get("paramlabel");
      String defaultValue = kv.get("defaultvalue");
      String dataType = kv.get("pdatatype");
      String type = kv.get("ptype");
      
      ModelParameter param = new ModelParameter(pid, plabel, type, defaultValue, dataType);
      parameters.add(param);
    }
    model.setParameters(parameters);
    return model;
  }
  

  @SuppressWarnings("unused")
  private String getGraphFile(String graphid) {
    String graphdir = this.getFullPath("graphs");
    File dir = new File(graphdir);
    if(!dir.exists()) {
      dir.mkdir();
    }
    return graphdir + File.separator + graphid + ".json";
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
      if(stdname_id == null)
        continue;
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


  @Override
  public TaskType getTaskType(String id) {
    return this.taskTypes.get(id);
  }

  @Override
  public WorkflowPointer getWorkflow(String id) {
    return this.workflows.get(id);
  }
  
}
