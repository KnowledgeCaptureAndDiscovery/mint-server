package org.mint.server.repository.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.apache.commons.configuration.plist.PropertyListConfiguration;
import org.mint.server.classes.DataSpecification;
import org.mint.server.classes.EnsembleSpecification;
import org.mint.server.classes.InterventionSpecification;
import org.mint.server.classes.Region;
import org.mint.server.classes.Task;
import org.mint.server.classes.graph.VariableGraph;
import org.mint.server.classes.question.ModelingQuestion;
import org.mint.server.classes.workflow.ModelGraph;
import org.mint.server.classes.workflow.WorkflowTemplate;
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
  
  public String getQuestionURI(String questionid) {
    return this.server + "/users/" + this.userid + "/questions/" + questionid;
  }

  public String getTaskURI(String questionid, String taskid) {
    return this.server + "/users/" + this.userid + "/tasks/" + questionid + "/" + taskid;
  }
  
  private String getQuestionsFile() {
    return this.storage + File.separator + this.QUESTIONS_FILE;
  }

  private String getTasksFile(String questionid) {
    return this.storage + File.separator + "tasks" + File.separator + questionid + File.separator + "tasks.json";
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
  
  @Override
  public ArrayList<ModelingQuestion> listModelingQuestions(String regionid) {
    ArrayList<ModelingQuestion> filteredList = new ArrayList<ModelingQuestion>();
    for(ModelingQuestion question : this.listAllModelingQuestions())
      if(question.getRegion().equals(regionid))
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
  public String addModelingQuestion(ModelingQuestion question) {
    ArrayList<ModelingQuestion> questions = this.listAllModelingQuestions();
    questions.add(question);
    this.writeModelingQuestions(questions);
    return question.getID();
  }

  @Override
  public void updateModelingQuestion(ModelingQuestion newquestion) {
    ArrayList<ModelingQuestion> questions = this.listAllModelingQuestions();
    ArrayList<ModelingQuestion> newquestions = new ArrayList<ModelingQuestion>();

    for(ModelingQuestion question : questions) {
      if(!question.getID().equals(newquestion.getID()))
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
  }

  /* End of Modeling Questions */
  
  
  /* Start of Variable Graph */
  
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
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void updateVariableGraph(VariableGraph graph) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void deleteVariableGraph(String id) {
    // TODO Auto-generated method stub
    
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
    task.setStatus(Task.Status.ONGOING);
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

  @Override
  public ArrayList<DataSpecification> listDataSpecifications(String taskid) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DataSpecification getDataSpecificationDetails(String id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String addDataSpecification(DataSpecification data_specification) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void updateDataSpecification(DataSpecification data_specification) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void deleteDataSpecification(String id) {
    // TODO Auto-generated method stub
    
  }
  
  /* End of Data Specifications */

  
  /* Start of Workflow Composition */
  
  @Override
  public ArrayList<ModelGraph> composeModelGraphs(VariableGraph graph,
      DataSpecification data_specification) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ArrayList<WorkflowTemplate> createWorkflows(VariableGraph graph, ModelGraph model_graph,
      DataSpecification data_specification) {
    // TODO Auto-generated method stub
    return null;
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
  
  
}
