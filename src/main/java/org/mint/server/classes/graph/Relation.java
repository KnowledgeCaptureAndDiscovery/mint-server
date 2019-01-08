package org.mint.server.classes.graph;

public class Relation {
  String from;
  String to;
  boolean added;

  public String getFrom() {
    return from;
  }
  public void setFrom(String from) {
    this.from = from;
  }
  public String getTo() {
    return to;
  }
  public void setTo(String to) {
    this.to = to;
  }
  public boolean isAdded() {
    return added;
  }
  public void setAdded(boolean added) {
    this.added = added;
  }
}
