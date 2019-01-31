package org.mint.server.api.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.mint.server.util.Config;

import com.fasterxml.jackson.annotation.JsonProperty;


@Path("layout")
public class LayoutResource {
  
  @POST
  @Path("{type}")
  public String dotLayout(@PathParam("type") String layout_type, 
      @JsonProperty("dotstring") String dotstring) {
    String dotexe = Config.get().getProperties().getString("layout."+layout_type);
    if(dotexe != null) {
      try {
        // Run the dot executable
        Process dot = Runtime.getRuntime().exec(new String[]{dotexe, "-Tplain"});
        
        // Write dot-format graph to dot
        PrintWriter bout = new PrintWriter(dot.getOutputStream());
        bout.println(dotstring);
        bout.flush();
        
        // Read position annotated graph from dot
        BufferedReader bin = new BufferedReader(new InputStreamReader(dot.getInputStream()));
        StringBuffer layout = new StringBuffer();
        String line;
        while((line = bin.readLine()) != null) {
          layout.append(line);
        }
        return layout.toString();
      }
      catch(IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
