package org.mint.server.repository;

import java.util.ArrayList;

import org.mint.server.classes.Region;
import org.mint.server.classes.model.Model;
import org.mint.server.classes.vocabulary.TaskType;
import org.mint.server.classes.vocabulary.WorkflowPointer;

public interface MintVocabulary {
  ArrayList<Region> getRegions();
  
  Region getRegion(String id);
  
  ArrayList<TaskType> getTaskTypes();
  
  TaskType getTaskType(String id);
  
  ArrayList<WorkflowPointer> getWorkflows();
  
  WorkflowPointer getWorkflow(String id);
  
  ArrayList<Model> getModels();
  
  Model getModel(String id);
}
