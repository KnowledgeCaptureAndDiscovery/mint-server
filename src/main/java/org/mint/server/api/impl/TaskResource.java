package org.mint.server.api.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.mint.server.classes.Task;
import org.mint.server.repository.impl.MINTRepositoryJSON;

import com.fasterxml.jackson.annotation.JsonProperty;

@Path("users/{userid}/questions/{questionid}/tasks")
public class TaskResource {

  @Context
  HttpServletRequest request;
  
  @PathParam("userid") String userid;
  @PathParam("questionid") String questionid;
  
  @GET
  @Produces("application/json")
  public List<Task> listTasks() {
    return MINTRepositoryJSON.get(userid).listTasks(questionid);
  }
  
  @GET
  @Path("{taskid}")
  @Produces("application/json")
  public Task getTask(@PathParam("taskid") String taskid) {
    taskid = request.getRequestURL().toString();
    return MINTRepositoryJSON.get(userid).getTaskDetails(questionid, taskid);
  }
  
  @POST
  @Produces("application/json")
  public String addTask(@JsonProperty("task") Task task) {
    MINTRepositoryJSON repo = MINTRepositoryJSON.get(userid);
    String taskid = repo.getRandomID("task-");
    task.setID(repo.getTaskURI(questionid, taskid));
    return repo.addTask(questionid, task);
  }
  
  @DELETE
  @Path("{taskid}")
  @Produces("application/json")
  public void deleteTask(@PathParam("taskid") String taskid) {
    taskid = request.getRequestURL().toString();
    MINTRepositoryJSON.get(userid).deleteTask(questionid, taskid);
  }
  
  @PUT
  @Path("{taskid}")
  @Produces("application/json")
  public void updateTask(@JsonProperty("task") Task task) {
    MINTRepositoryJSON.get(userid).updateTask(questionid, task);
  }
}
