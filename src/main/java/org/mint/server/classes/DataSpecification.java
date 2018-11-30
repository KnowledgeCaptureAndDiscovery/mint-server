package org.mint.server.classes;

import java.util.ArrayList;
import java.util.HashMap;

public class DataSpecification extends URIEntity {
  private static final long serialVersionUID = 1772106688715676040L;

  HashMap<String, ArrayList<String>> variableDatasetMap;

  public HashMap<String, ArrayList<String>> getVariableDatasetMap() {
    return variableDatasetMap;
  }

  public void setVariableDatasetMap(HashMap<String, ArrayList<String>> variableDatasetMap) {
    this.variableDatasetMap = variableDatasetMap;
  }
}
