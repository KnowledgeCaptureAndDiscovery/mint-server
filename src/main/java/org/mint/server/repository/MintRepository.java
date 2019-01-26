package org.mint.server.repository;

import java.util.ArrayList;

import org.mint.server.classes.DataSpecification;
import org.mint.server.classes.EnsembleSpecification;
import org.mint.server.classes.InterventionSpecification;
import org.mint.server.classes.Region;
import org.mint.server.classes.Task;
import org.mint.server.classes.graph.VariableGraph;
import org.mint.server.classes.question.ModelingQuestion;
import org.mint.server.planner.WorkflowSolution;

public interface MintRepository {
  // Vocabulary
  public MintVocabulary getVocabulary();
  
  // Region
  public ArrayList<Region> listRegions();
  public Region getRegionDetails(String id);
  public String addRegion(Region region);
  public void updateRegion(Region region);
  public void deleteRegion(String id);
  
  // ModelingQuestion
  public ArrayList<ModelingQuestion> listAllModelingQuestions();
  public ArrayList<ModelingQuestion> listModelingQuestions(String regionid);
  public ModelingQuestion getModelingQuestionDetails(String questionid);
  public void setModelingQuestionGraph(String questionid, String graphid);
  public String addModelingQuestion(ModelingQuestion question);
  public void updateModelingQuestion(ModelingQuestion question);
  public void deleteModelingQuestion(String questionid);
  
  // VariableGraph
  public VariableGraph getVariableGraph(String questionid);
  public String addVariableGraph(VariableGraph graph);
  public void updateVariableGraph(VariableGraph graph);
  public void deleteVariableGraph(String graphid);
  
  // Task
  public ArrayList<Task> listTasks(String questionid);
  public Task getTaskDetails(String questionid, String taskid);
  public String addTask(String questionid, Task task);
  public void updateTask(String questionid, Task task);
  public void deleteTask(String questionid, String taskid);
  
  // DataSpecification
  public ArrayList<DataSpecification> listDataSpecifications(String questionid);
  public DataSpecification getDataSpecificationDetails(String questionid, String dsid);
  public String addDataSpecification(String questionid, DataSpecification data_specification);
  public void updateDataSpecification(String questionid, DataSpecification data_specification);
  public void deleteDataSpecification(String questionid, String dsid);
  
  // Workflow Composition / Planning
  public ArrayList<WorkflowSolution> createWorkflowSolutions(
      ArrayList<String> drivingVariables,
      ArrayList<String> responseVariables,
      ArrayList<String> models,
      VariableGraph cag,
      DataSpecification ds);
  
  // InterventionSpecification (? Can this be represented by data specification above ? )
  public ArrayList<InterventionSpecification> listInterventionSpecifications(String taskid);
  public InterventionSpecification getInterventionSpecificationDetails(String id);
  public String addInterventionSpecification(InterventionSpecification intervention_specification);
  public void updateInterventionSpecification(InterventionSpecification intervention_specification);
  public void deleteInterventionSpecification(String id);
  
  // EnsembleSpecification (? Can this be represented by data specification above ? )
  public ArrayList<EnsembleSpecification> listEnsembleSpecifications(String taskid);
  public EnsembleSpecification getEnsembleSpecificationDetails(String id);
  public String addEnsembleSpecification(EnsembleSpecification ensemble_specification);
  public void updateEnsembleSpecification(EnsembleSpecification ensemble_specification);
  public void deleteEnsembleSpecification(String id);
  
  // Run Workflows
  public String runComparisonWorkflow(String taskid, String templateid, DataSpecification data_specification);
  public String runCalibrationWorkflow(String taskid, String templateid, DataSpecification data_specification);
  public String runEnsembleWorkflows(String taskid, String templateid, 
      DataSpecification data_specification, 
      InterventionSpecification intervention_specification,
      EnsembleSpecification ensemble_specification);
  
  // Inspect Workflow Runs
  public DataSpecification getRunOutputs(String runid);
  public DataSpecification getComparisonRunOutput(String runid);
  public DataSpecification getCalibrationRunOutput(String runid);
  
}
