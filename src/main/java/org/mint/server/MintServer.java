package org.mint.server;

import javax.annotation.PreDestroy;

import org.glassfish.jersey.server.ResourceConfig;
import org.mint.server.api.impl.CommonResource;
import org.mint.server.api.impl.DataResource;
import org.mint.server.api.impl.GraphResource;
import org.mint.server.api.impl.LayoutResource;
import org.mint.server.api.impl.PlannerResource;
import org.mint.server.api.impl.QuestionResource;
import org.mint.server.api.impl.TaskResource;
import org.mint.server.api.impl.WorkflowResource;
import org.mint.server.filters.AcceptHeaderFilter;
import org.mint.server.filters.CORSResponseFilter;
import org.mint.server.filters.ConfigLoadFilter;
import org.mint.server.filters.GZIPReaderInterceptor;
import org.mint.server.filters.GZIPWriterInterceptor;

class MintServer extends ResourceConfig {

  public MintServer() {
    // Headers
    register(ConfigLoadFilter.class);
    register(AcceptHeaderFilter.class);
    register(CORSResponseFilter.class);
    
    register(GZIPWriterInterceptor.class);
    register(GZIPReaderInterceptor.class);
    
    // Main Resources
    register(CommonResource.class);    
    register(QuestionResource.class);
    register(GraphResource.class);
    register(TaskResource.class);
    register(DataResource.class);
    register(PlannerResource.class);
    register(WorkflowResource.class);
    register(LayoutResource.class);
  }
  
  @PreDestroy
  public void onDestroy() {
    // Cleanup tasks
  }

}
