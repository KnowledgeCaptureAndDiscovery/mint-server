package org.mint.server.api.impl;


import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import com.fasterxml.jackson.annotation.JsonProperty;

@Path("")
@DeclareRoles({"user", "admin", "importer"})
public class UserResource {
  @Context
  HttpServletResponse response;
  
  @Context
  HttpServletRequest request;
  
  @Context
  SecurityContext securityContext;
  
  /**
   * Authentication
   */
  /*
  @POST
  @Path("login")
  @Produces("application/json")
  @Consumes("application/json")
  @Override
  public UserSession login(
      @JsonProperty("credentials") UserCredentials credentials) {
    return UserDatabase.get().login(credentials);
  }
  
  @POST
  @Path("validate")
  @Produces("application/json")
  @Consumes("application/json")
  @Override
  public UserSession validateSession(
      @JsonProperty("session") UserSession session) {
    return UserDatabase.get().validateSession(session);
  }
  
  @POST
  @Path("logout")
  @Consumes("application/json")
  @Override
  public void logout(UserSession session) {
    UserDatabase.get().logout(session);
  }
  */
  
  /**
   * Query users
   */
  
}
