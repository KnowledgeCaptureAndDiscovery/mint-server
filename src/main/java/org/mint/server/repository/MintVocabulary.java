package org.mint.server.repository;

import java.util.ArrayList;

import org.mint.server.classes.Region;
import org.mint.server.classes.graph.VariableGraph;
import org.mint.server.classes.model.Model;
import org.mint.server.classes.rawcag.RawCAG;
import org.mint.server.classes.vocabulary.EventType;
import org.mint.server.classes.vocabulary.InterventionType;
import org.mint.server.classes.vocabulary.QuestionTemplate;
import org.mint.server.classes.vocabulary.TaskType;
import org.mint.server.classes.vocabulary.WorkflowPointer;

public interface MintVocabulary {
  ArrayList<QuestionTemplate> getQuestionTemplates();
  
  QuestionTemplate getQuestionTemplate(String id);
  
  ArrayList<EventType> getEventTypes();
  
  EventType getEventType(String id);
  
  ArrayList<InterventionType> getInterventionTypes();
  
  InterventionType getInterventionType(String id);
  
  ArrayList<Region> getRegions();
  
  Region getRegion(String id);
  
  ArrayList<TaskType> getTaskTypes();
  
  TaskType getTaskType(String id);
  
  ArrayList<WorkflowPointer> getWorkflows();
  
  WorkflowPointer getWorkflow(String id);
  
  ArrayList<VariableGraph> getGraphs();
  
  VariableGraph getGraph(String id);

  ArrayList<RawCAG> getRawCAGs();
  
  RawCAG getRawCAG(String id);
  
  ArrayList<Model> getModels();
  
  Model getModel(String id);
}
