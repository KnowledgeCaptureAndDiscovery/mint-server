package org.mint.server.classes.question.template;

import java.util.ArrayList;

public class TemplateItem {
  public enum Type { TEXT, EVENT, INTERVENTION, REGION, TIMEPERIOD };
  
  public String value;
  public ArrayList<String> options;
  
  public TemplateItem() {
    this.options = new ArrayList<String>();
  }
  
  public TemplateItem(String value) {
    this();
    this.value = value;
  }
  
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public ArrayList<String> getOptions() {
    return options;
  }

  public void setOptions(ArrayList<String> options) {
    this.options = options;
  }
}
