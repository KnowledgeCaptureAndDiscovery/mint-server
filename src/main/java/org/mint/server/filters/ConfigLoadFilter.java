package org.mint.server.filters;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;

import org.mint.server.util.Config;


@PreMatching
public class ConfigLoadFilter implements ContainerRequestFilter {
  @Context 
  HttpServletRequest request;
  
  @Override
  public void filter(ContainerRequestContext context) throws IOException {
    Config.load(request);
  }

}