package org.mint.server.classes.util;

public class KBConstants {
  private static String onturi = "https://w3id.org/mint/mo";
  private static String provuri = "http://www.w3.org/ns/prov-o";
  private static String provns = "http://www.w3.org/ns/prov#";
  private static String owlns = "http://www.w3.org/2002/07/owl#";
  private static String rdfns = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
  private static String rdfsns = "http://www.w3.org/2000/01/rdf-schema#";
  
  private static String dctermsns = "http://purl.org/dc/terms/";
  private static String dcns = "http://purl.org/dc/elements/1.1/";
  
  public static String ONTURI() {
    return "file:///Users/varun/Desktop/MintOntology/mo.owl";
    //return onturi;
  }

  public static void ONTURI(String uri) {
    onturi = uri;
  }
  
  public static String ONTNS() {
    return onturi + "#";
  }

  public static String PROVURI() {
    return provuri;
  }
  
  public static String PROVNS() {
    return provns;
  }
  
  public static String DCTERMSNS() {
    return dctermsns;
  }
  
  public static String DCNS() {
    return dcns;
  }
  
  public static String OWLNS() {
    return owlns;
  }
  
  public static String RDFNS() {
    return rdfns;
  }
  
  public static String RDFSNS() {
    return rdfsns;
  }

}

