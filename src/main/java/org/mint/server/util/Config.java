package org.mint.server.util;

import java.io.File;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.plist.PropertyListConfiguration;


public class Config {
  static Config singleton = null;


  PropertyListConfiguration props = null;
  
  public static void load(HttpServletRequest request) {
    singleton = new Config(request);
  }
  
  public static Config get() {
    return singleton;
  }
  
  public Config(HttpServletRequest request) {
    this.props = loadServerConfiguration(request);
  }
  
  public PropertyListConfiguration getProperties() {
    return this.props;
  }
  
  private PropertyListConfiguration loadServerConfiguration(HttpServletRequest request) {
    String configFile = null;
    if(request != null) {
      ServletContext app = request.getSession().getServletContext();
      configFile = app.getInitParameter("config.file");
    }
    if (configFile == null) {
        String home = System.getProperty("user.home");
        if (home != null && !home.equals(""))
            configFile = home + File.separator + ".mint"
                    + File.separator + "server.properties";
        else
            configFile = "/etc/mint/server.properties";
    }
    // Create configFile if it doesn't exist (portal.properties)
    File cfile = new File(configFile);
    if (!cfile.exists()) {
        if (!cfile.getParentFile().exists() && !cfile.getParentFile().mkdirs()) {
            System.err.println("Cannot create config file directory : " + cfile.getParent());
            return null;
        }
        if (request != null)
            createDefaultServerConfig(request, configFile);
    }
    // Load properties from configFile
    PropertyListConfiguration props = new PropertyListConfiguration();
    try {
        props.load(configFile);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return props;
  }
  
  private void createDefaultServerConfig(HttpServletRequest request, String configFile) {
    String server = request.getScheme() + "://" + request.getServerName() + ":"
            + request.getServerPort() + request.getContextPath();
    String storageDir = null;
    String home = System.getProperty("user.home");
    if (home != null && !home.equals(""))
        storageDir = home + File.separator + ".mint" + File.separator + "storage";
    else
        storageDir = System.getProperty("java.io.tmpdir") +
                File.separator + "mint" + File.separator + "storage";
    if (!new File(storageDir).mkdirs())
        System.err.println("Cannot create storage directory: " + storageDir);

    PropertyListConfiguration config = new PropertyListConfiguration();
    config.addProperty("storage", storageDir);
    config.addProperty("server", server);
    
    try {
        config.save("file://" + configFile);
    } catch (Exception e) {
        e.printStackTrace();
    }
  }
  
  public HashMap<String, Object> getPropertiesMap() {
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("storage", this.props.getString("storage"));
    map.put("server", this.props.getString("server"));
    
    HashMap<String, String> wingsProps = new HashMap<String, String>();
    wingsProps.put("server", this.props.getString("wings.server"));
    if(this.props.getString("wings.internal_server") != null)
      wingsProps.put("internal_server", this.props.getString("wings.internal_server"));
    else
      wingsProps.put("internal_server", this.props.getString("wings.server"));
    wingsProps.put("domain", this.props.getString("wings.domain"));
    wingsProps.put("storage", this.props.getString("wings.storage"));
    wingsProps.put("dotpath", this.props.getString("wings.dotpath"));
    wingsProps.put("ontology_prefix", this.props.getString("wings.ontology_prefix"));
    map.put("wings", wingsProps);
    
    HashMap<String, String> catalogProps = new HashMap<String, String>();
    catalogProps.put("model", this.props.getString("catalogs.model"));
    catalogProps.put("data", this.props.getString("catalogs.data"));
    map.put("catalogs", catalogProps);
    
    HashMap<String, String> vizProps = new HashMap<String, String>();
    vizProps.put("server", this.props.getString("visualization.server"));
    map.put("visualization", vizProps);

    HashMap<String, String> transProps = new HashMap<String, String>();
    transProps.put("server", this.props.getString("transformation.server"));
    map.put("transformation", transProps);

    HashMap<String, String> gsnProps = new HashMap<String, String>();
    gsnProps.put("server", this.props.getString("gsn.server"));
    map.put("gsn", gsnProps);
    
    return map;
  }
}
