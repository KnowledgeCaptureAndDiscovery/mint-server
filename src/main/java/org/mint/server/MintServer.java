package org.mint.server;

import javax.annotation.PreDestroy;

import org.glassfish.jersey.server.ResourceConfig;
import org.mint.server.api.impl.CommonResource;
import org.mint.server.api.impl.GraphResource;
import org.mint.server.api.impl.QuestionResource;
import org.mint.server.api.impl.TaskResource;
import org.mint.server.filters.AcceptHeaderFilter;
import org.mint.server.filters.CORSResponseFilter;
import org.mint.server.filters.ConfigLoadFilter;

class MintServer extends ResourceConfig {

  public MintServer() {
    // Headers
    register(ConfigLoadFilter.class);
    register(AcceptHeaderFilter.class);
    register(CORSResponseFilter.class);
    
    // Main Resources
    register(CommonResource.class);    
    register(QuestionResource.class);
    register(GraphResource.class);
    register(TaskResource.class);
  }
  
  @PreDestroy
  public void onDestroy() {
    // Cleanup tasks
  }

}
