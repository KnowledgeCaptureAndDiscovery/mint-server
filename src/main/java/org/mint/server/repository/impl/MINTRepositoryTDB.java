package org.mint.server.repository.impl;

/*
import java.io.File;
import java.util.HashMap;

import org.apache.commons.configuration.plist.PropertyListConfiguration;
import org.mint.server.classes.util.KBConstants;
import org.mint.server.util.Config;

import edu.isi.kcap.ontapi.KBAPI;
import edu.isi.kcap.ontapi.KBObject;
import edu.isi.kcap.ontapi.OntFactory;
import edu.isi.kcap.ontapi.OntSpec;
import edu.isi.kcap.ontapi.jena.transactions.TransactionsJena;
*/

public class MINTRepositoryTDB {
  /*
  extends TransactionsJena {
  KBAPI ontkb;
  
  String tdbdir;
  String dbdir;
  String owlns, rdfns, rdfsns;
  String onturi,liburi;
  String ontns;
  
  String server;
  
  String uniongraph;
  
  HashMap<String, KBObject> items;
  
  static MINTRepositoryTDB singleton = null;

  public static MINTRepositoryTDB get() {
    if(singleton == null)
      singleton = new MINTRepositoryTDB();
    return singleton;
  }
  
  public MINTRepositoryTDB() {
    setConfiguration();
    initializeKB();
  }
  
  public String LIBURI() {
    if(liburi == null)
      liburi = server.replaceAll("\\/$", "") + "/software/";
    return liburi;
  }
  
  public String USERURI() {
    return server.replaceAll("\\/$", "") + "/users/"; 
  }
  
  public String USERNS() {
    return USERURI();
  }
  
  public String LIBNS() {
    return LIBURI();
  }
  
  public void initializeKB() {
    this.ontologyFactory = new OntFactory(OntFactory.JENA, tdbdir);
    try {
      this.ontkb = ontologyFactory.getKB(onturi, OntSpec.PELLET, false, true);

      this.start_read();
      this.initializeVocabularyFromKB();
      this.end();
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public void reloadKBCaches() {
    if(this.ontkb != null)
      this.ontkb.delete();
    
    this.initializeKB();
  }
  
  public KBObject getItem(String id) {
    if(this.items == null)
      return null;
    
    return this.items.get(id);
  }
  
  private void initializeVocabularyFromKB() {
    this.items = new HashMap<String, KBObject>();
    for(KBObject item: this.ontkb.getAllClasses()) {
      if(!item.isAnonymous()) {
        this.items.put(item.getID(), item);
      }
    }
    for(KBObject item: this.ontkb.getAllDatatypeProperties()) {
      this.items.put(item.getID(), item);
    }
    for(KBObject item: this.ontkb.getAllObjectProperties()) {
      this.items.put(item.getID(), item);
    }
  }
  
  private void setConfiguration() {
    PropertyListConfiguration props = Config.get().getProperties();
    this.server = props.getString("server");
    onturi = KBConstants.ONTURI();
    liburi = this.LIBURI();
    
    ontns = KBConstants.ONTNS();
    
    tdbdir = props.getString("storage.tdb");
    File tdbdirf = new File(tdbdir);
    if(!tdbdirf.exists() && !tdbdirf.mkdirs()) {
      System.err.println("Cannot create tdb directory : "+tdbdirf.getAbsolutePath());
    }

    uniongraph = "urn:x-arq:UnionGraph";
    
    owlns = KBConstants.OWLNS();
    rdfns = KBConstants.RDFNS();
    rdfsns = KBConstants.RDFSNS();
  }*/
}
