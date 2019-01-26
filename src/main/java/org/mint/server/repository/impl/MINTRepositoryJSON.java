package org.mint.server.repository.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.configuration.plist.PropertyListConfiguration;
import org.apache.commons.io.FileUtils;
import org.mint.server.classes.DataSpecification;
import org.mint.server.classes.EnsembleSpecification;
import org.mint.server.classes.InterventionSpecification;
import org.mint.server.classes.Region;
import org.mint.server.classes.Task;
import org.mint.server.classes.graph.Relation;
import org.mint.server.classes.graph.GVariable;
import org.mint.server.classes.graph.VariableGraph;
import org.mint.server.classes.question.ModelingQuestion;
import org.mint.server.classes.vocabulary.TaskType;
import org.mint.server.classes.workflow.TempWorkflowDetails;
import org.mint.server.planner.MintPlanner;
import org.mint.server.planner.WorkflowSolution;
import org.mint.server.repository.MintRepository;
import org.mint.server.repository.MintVocabulary;
import org.mint.server.util.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

public class MINTRepositoryJSON implements MintRepository {
  String server, storage, userid;
  
  String REGIONS_FILE = "regions.json";
  String REGIONS_DIR = "regions";
  String QUESTIONS_FILE = "questions.json";
  String QUESTIONS_DIR = "questions";
  String VARIABLE_GRAPH_FILE = "variable_graph.json";
  
  ObjectMapper mapper;
  MintVocabulary vocabulary = null;

  static HashMap<String, MINTRepositoryJSON> repocache = 
      new HashMap<String, MINTRepositoryJSON>();

  public static MINTRepositoryJSON get(String userid) {
    if(!repocache.containsKey(userid))
      repocache.put(userid, new MINTRepositoryJSON(userid));
    return repocache.get(userid);
  }
  
  public MINTRepositoryJSON(String userid) {
    setConfiguration(userid);
    this.vocabulary = MintVocabularyJSON.get();
  }
  
  /* Helper Functions */
  
  private void setConfiguration(String userid) {
    this.userid = userid;
    PropertyListConfiguration props = Config.get().getProperties();
    this.server = props.getString("server");
    this.storage = props.getString("storage") + File.separator + "users" + File.separator + userid;
    this.mapper = new ObjectMapper();
    
    File dirf = new File(this.storage);
    if(!dirf.exists() && !dirf.mkdirs()) {
      System.err.println("Cannot create storage directory : "+dirf.getAbsolutePath());
    }
  }
  
  public String getRandomID(String prefix) {
    return prefix + UUID.randomUUID().toString();
  }
  
  /* Get rest api uris */
  public String getQuestionURI(String questionid) {
    return this.server + "/users/" + this.userid + "/questions/" + questionid;
  }

  public String getTaskURI(String questionid, String taskid) {
    return this.server + "/users/" + this.userid + "/questions/" + questionid + "/tasks/" + taskid;
  }
  
  public String getDataSpecificationURI(String questionid, String dsid) {
    return this.server + "/users/" + this.userid + "/questions/" + questionid + "/data/" + dsid;
  }
  
  public String getGraphURI(String graphid) {
    return this.server + "/users/" + this.userid + "/graphs/" + graphid;
  }
  /* End of Get rest api uris */
  
  
  /* Get paths to files */
  private String getQuestionDir(String questionid) {
    return this.storage + File.separator + this.QUESTIONS_DIR + File.separator + questionid;    
  }

  private String getQuestionsFile() {
    String qfile = this.storage + File.separator + this.QUESTIONS_FILE;
    String qdir = this.storage + File.separator + this.QUESTIONS_DIR;
    File f = new File(qfile);
    File dir = new File(qdir);
    if(!f.exists()) {
      try {
        Files.write(f.toPath(), "[]".getBytes());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    if(!dir.exists()) {
      dir.mkdir();
    }
    return qfile;
  }
  
  private String getGraphFile(String graphid) {
    String graphdir = this.storage + File.separator + "graphs";
    File dir = new File(graphdir);
    if(!dir.exists()) {
      dir.mkdir();
    }
    return graphdir + File.separator + graphid + ".json";
  }
  
  private String getTasksFile(String questionid) {
    return this.getQuestionDir(questionid) + File.separator + "tasks.json";
  }
  
  private String getDataSpecificationsFile(String questionid) {
    return this.getQuestionDir(questionid) + File.separator  + "data_specifications.json";
  }

  private String getHistoryFile(String questionid) {
    return this.getQuestionDir(questionid) + File.separator + "history.json";
  }
  
  private String getFavoritesFile(String questionid) {
    return this.getQuestionDir(questionid) + File.separator + "favorites.json";
  }
  
  private String getWorkflowsFile(String questionid) {
    return this.getQuestionDir(questionid) + File.separator + "workflows.json";
  }
  
  /* End of get paths to files */
  
  private void createDirectory(String path) {
    File f = new File(path);
    f.mkdir();
  }
  
  private void removeDirectory(String path) {
    File f = new File(path);
    try {
      FileUtils.deleteDirectory(f);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  /* End of Helper Functions */
  
  
  /* Start of Regions */
  
  @Override
  public ArrayList<Region> listRegions() { 
    return this.vocabulary.getRegions();
  }

  @Override
  public Region getRegionDetails(String id) {
    for(Region region: this.vocabulary.getRegions()) {
      if(region.getID().equals(id))
        return region;
    }
    return null;
  }

  @Override
  public String addRegion(Region region) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void updateRegion(Region region) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void deleteRegion(String id) {
    // TODO Auto-generated method stub
    
  }
  
  /* End of Regions */
  

  /* Start of Modeling Questions */
  private void writeModelingQuestions(ArrayList<ModelingQuestion> questions) {
    try {
      this.mapper.writerWithDefaultPrettyPrinter().writeValue(
          new FileOutputStream(this.getQuestionsFile()), questions);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Override
  public ArrayList<ModelingQuestion> listAllModelingQuestions() {
    CollectionType questionList = mapper.getTypeFactory()
        .constructCollectionType(ArrayList.class, ModelingQuestion.class);    
    try {
      return this.mapper.readValue(new FileInputStream(this.getQuestionsFile()), questionList);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  private ArrayList<String> getAllRegionIds(Region region) {
    ArrayList<String> regionids = new ArrayList<String>();
    regionids.add(region.getID());
    if(region.getSubRegions() != null)
      for(Region subRegion : region.getSubRegions()) 
        regionids.addAll(this.getAllRegionIds(subRegion));

    return regionids;
  }
  
  @Override
  public ArrayList<ModelingQuestion> listModelingQuestions(String regionid) {
    if(regionid == null) 
      return this.listAllModelingQuestions();
    
    ArrayList<ModelingQuestion> filteredList = new ArrayList<ModelingQuestion>();
    Region region = this.getRegionDetails(regionid);
    if(region == null)
      return null;
    ArrayList<String> regionids = getAllRegionIds(region);
    for(ModelingQuestion question : this.listAllModelingQuestions())
      if(regionids.contains(question.getRegion()))
        filteredList.add(question);
    return filteredList;
  }

  @Override
  public ModelingQuestion getModelingQuestionDetails(String questionid) {
    for(ModelingQuestion question: this.listAllModelingQuestions()) {
      if(question.getID().equals(questionid))
        return question;
    }
    return null;
  }
  
  @Override
  public void setModelingQuestionGraph(String questionid, String graphid) {
    ModelingQuestion question = this.getModelingQuestionDetails(questionid);
    question.setGraph(graphid);
    this.updateModelingQuestion(question);
  }

  @Override
  public String addModelingQuestion(ModelingQuestion question) {
    // Add question to list and write to file
    ArrayList<ModelingQuestion> questions = this.listAllModelingQuestions();
    questions.add(question);
    this.writeModelingQuestions(questions);
    
    // Create tasks directory for question and initialize files
    String qname = question.getID().substring(question.getID().lastIndexOf("/")+1);
    String qdir = this.getQuestionDir(qname);
    this.createDirectory(qdir);
    try {
      Files.write(new File(this.getTasksFile(qname)).toPath(), "[]".getBytes());
      Files.write(new File(this.getHistoryFile(qname)).toPath(), "[]".getBytes());
      Files.write(new File(this.getFavoritesFile(qname)).toPath(), "[]".getBytes());
      Files.write(new File(this.getDataSpecificationsFile(qname)).toPath(), "[]".getBytes());
      Files.write(new File(this.getWorkflowsFile(qname)).toPath(), "{}".getBytes());
      
      // Create default tasks for the question
      this.createDefaultTasks(question.getID());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return question.getID();
  }
  
  private void createDefaultTasks(String questionid) {
    for(TaskType taskType : MintVocabularyJSON.get().getTaskTypes()) {
      Task task = new Task(taskType);
      String taskid = this.getRandomID("task-"); 
      String qname = questionid.substring(questionid.lastIndexOf("/"));
      task.setID(this.getTaskURI(qname, taskid));
      task.setQuestion(questionid);
      this.addTask(qname, task);
    }
  }

  @Override
  public void updateModelingQuestion(ModelingQuestion newquestion) {
    ArrayList<ModelingQuestion> questions = this.listAllModelingQuestions();
    ArrayList<ModelingQuestion> newquestions = new ArrayList<ModelingQuestion>();

    for(ModelingQuestion question : questions) {
      if(question.getID().equals(newquestion.getID()))
        newquestions.add(newquestion);
      else
        newquestions.add(question);
    }
    this.writeModelingQuestions(newquestions);
  }

  @Override
  public void deleteModelingQuestion(String questionid) {
    ArrayList<ModelingQuestion> questions = this.listAllModelingQuestions();
    ArrayList<ModelingQuestion> newquestions = new ArrayList<ModelingQuestion>();

    for(ModelingQuestion question : questions) {
      if(!question.getID().equals(questionid))
        newquestions.add(question);
    }
    this.writeModelingQuestions(newquestions);
    String qname = questionid.substring(questionid.lastIndexOf("/"));
    this.removeDirectory(this.getQuestionDir(qname));
  }

  /* End of Modeling Questions */
  
  
  /* Start of Variable Graph */
  
  private void writeGraph(VariableGraph graph) {
    String gname = graph.getID().substring(graph.getID().lastIndexOf("/"));
    try {
      this.mapper.writerWithDefaultPrettyPrinter().writeValue(
          new FileOutputStream(this.getGraphFile(gname)), graph);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Override
  public VariableGraph getVariableGraph(String graphid) {
    String graphfile = this.storage + File.separator + "graphs" + File.separator + graphid + ".json";
    try {
      return this.mapper.readValue(new FileInputStream(graphfile), VariableGraph.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public String addVariableGraph(VariableGraph graph) {
    // Rewriting existing id of graph
    String origid = graph.getID();
    String randomid = this.getRandomID("graph-");
    String newid = this.getGraphURI(randomid);
    graph.setID(newid);
    for(GVariable v : graph.getVariables()) {
      v.setID(v.getID().replace(origid, newid));
    }
    for(Relation l : graph.getLinks()) {
      l.setFrom(l.getFrom().replace(origid, newid));
      l.setTo(l.getTo().replace(origid, newid));
    }
    // Write the graph
    this.writeGraph(graph);
    return graph.getID();
  }

  @Override
  public void updateVariableGraph(VariableGraph graph) {
    this.writeGraph(graph);
  }

  @Override
  public void deleteVariableGraph(String graphid) {
    String fpath = this.getGraphFile(graphid);
    new File(fpath).delete();
  }
  
  /* End of Variable Graph */
  
  
  /* Start of Tasks */
  
  private void writeTasks(ArrayList<Task> tasks, String questionid) {
    try {
      this.mapper.writerWithDefaultPrettyPrinter().writeValue(
          new FileOutputStream(this.getTasksFile(questionid)), tasks);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public ArrayList<Task> listTasks(String questionid) {
    CollectionType taskList = mapper.getTypeFactory()
        .constructCollectionType(ArrayList.class, Task.class);    
    try {
      return this.mapper.readValue(new FileInputStream(this.getTasksFile(questionid)), taskList);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public Task getTaskDetails(String questionid, String taskid) {
    for(Task task: this.listTasks(questionid)) {
      if(task.getID().equals(taskid))
        return task;
    }
    return null;
  }

  @Override
  public String addTask(String questionid, Task task) {
    ArrayList<Task> tasks = this.listTasks(questionid);
    task.setStatus(Task.Status.NOT_STARTED);
    tasks.add(task);
    this.writeTasks(tasks, questionid);
    return task.getID();
  }

  @Override
  public void updateTask(String questionid, Task newtask) {
    ArrayList<Task> tasks = this.listTasks(questionid);
    ArrayList<Task> newtasks = new ArrayList<Task>();
    for(Task task : tasks) {
      if(task.getID().equals(newtask.getID()))
        newtasks.add(newtask);
      else
        newtasks.add(task);
    }
    this.writeTasks(newtasks, questionid);
  }

  @Override
  public void deleteTask(String questionid, String taskid) {
    ArrayList<Task> tasks = this.listTasks(questionid);
    ArrayList<Task> newtasks = new ArrayList<Task>();
    for(Task task : tasks) {
      if(!task.getID().equals(taskid))
        newtasks.add(task);
    }
    this.writeTasks(newtasks, questionid);
  }
  
  /* End of Tasks */
  
  
  /* Start of Data Specifications */

  private void writeDataSpecifications(ArrayList<DataSpecification> dss, String questionid) {
    try {
      this.mapper.writerWithDefaultPrettyPrinter().writeValue(
          new FileOutputStream(this.getDataSpecificationsFile(questionid)), dss);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  @Override
  public ArrayList<DataSpecification> listDataSpecifications(String questionid) {
    CollectionType dsList = mapper.getTypeFactory()
        .constructCollectionType(ArrayList.class, DataSpecification.class);    
    try {
      return this.mapper.readValue(new FileInputStream(this.getDataSpecificationsFile(questionid)), dsList);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public DataSpecification getDataSpecificationDetails(String questionid, String dsid) {
    for(DataSpecification ds: this.listDataSpecifications(questionid)) {
      if(ds.getID().equals(dsid))
        return ds;
    }
    return null;
  }

  @Override
  public String addDataSpecification(String questionid, DataSpecification ds) {
    ArrayList<DataSpecification> dss = this.listDataSpecifications(questionid);
    dss.add(ds);
    this.writeDataSpecifications(dss, questionid);
    return ds.getID();
  }

  @Override
  public void updateDataSpecification(String questionid, DataSpecification newds) {
    ArrayList<DataSpecification> dss = this.listDataSpecifications(questionid);
    ArrayList<DataSpecification> newdss = new ArrayList<DataSpecification>();
    for(DataSpecification ds : dss) {
      if(ds.getID().equals(newds.getID()))
        newdss.add(newds);
      else
        newdss.add(newds);
    }
    this.writeDataSpecifications(newdss, questionid);
  }

  @Override
  public void deleteDataSpecification(String questionid, String dsid) {
    ArrayList<DataSpecification> dss = this.listDataSpecifications(questionid);
    ArrayList<DataSpecification> newdss = new ArrayList<DataSpecification>();
    for(DataSpecification ds : dss) {
      if(!ds.getID().equals(dsid))
        newdss.add(ds);
    }
    this.writeDataSpecifications(newdss, questionid);
  }
  
  /* End of Data Specifications */

  
  /* Start of Workflow Composition */
  
  @Override
  public ArrayList<WorkflowSolution> createWorkflowSolutions(
      ArrayList<String> drivingVariables,
      ArrayList<String> responseVariables,
      ArrayList<String> models,
      VariableGraph cag,
      DataSpecification ds) {
    return new MintPlanner().createWorkflowSolutions(drivingVariables, 
        responseVariables, models, cag, ds, this.vocabulary.getModels());
  }
  
  /* End of Workflow Compositions */
  

  @Override
  public ArrayList<InterventionSpecification> listInterventionSpecifications(String taskid) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public InterventionSpecification getInterventionSpecificationDetails(String id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String addInterventionSpecification(InterventionSpecification intervention_specification) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void updateInterventionSpecification(InterventionSpecification intervention_specification) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void deleteInterventionSpecification(String id) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public ArrayList<EnsembleSpecification> listEnsembleSpecifications(String taskid) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EnsembleSpecification getEnsembleSpecificationDetails(String id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String addEnsembleSpecification(EnsembleSpecification ensemble_specification) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void updateEnsembleSpecification(EnsembleSpecification ensemble_specification) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void deleteEnsembleSpecification(String id) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public String runComparisonWorkflow(String taskid, String templateid, DataSpecification data_specification) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String runCalibrationWorkflow(String taskid, String templateid, DataSpecification data_specification) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String runEnsembleWorkflows(String taskid, String templateid, DataSpecification data_specification,
      InterventionSpecification intervention_specification, EnsembleSpecification ensemble_specification) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DataSpecification getRunOutputs(String runid) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DataSpecification getComparisonRunOutput(String runid) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DataSpecification getCalibrationRunOutput(String runid) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public MintVocabulary getVocabulary() {
    // TODO Auto-generated method stub
    return this.vocabulary;
  }
  
  /* Temporary APIs */
  public String saveWorkflow(String questionid, TempWorkflowDetails wflow) {
    try {
      this.mapper.writerWithDefaultPrettyPrinter().writeValue(
          new FileOutputStream(this.getWorkflowsFile(questionid)), wflow);
      return "OK";
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public TempWorkflowDetails getWorkflow(String questionid) {    
    try {
      return this.mapper.readValue(new FileInputStream(this.getWorkflowsFile(questionid)), 
          TempWorkflowDetails.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
