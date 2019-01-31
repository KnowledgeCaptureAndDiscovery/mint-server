package org.mint.server.classes.wings;

public class Constraint {
  ConstraintItem subject;
  ConstraintItem predicate;
  ConstraintItem object;
  
  public Constraint(ConstraintItem subject, ConstraintItem predicate, ConstraintItem object) {
    this.subject = subject;
    this.predicate = predicate;
    this.object = object;
  }
  
  public ConstraintItem getSubject() {
    return subject;
  }
  public void setSubject(ConstraintItem subject) {
    this.subject = subject;
  }
  public ConstraintItem getPredicate() {
    return predicate;
  }
  public void setPredicate(ConstraintItem predicate) {
    this.predicate = predicate;
  }
  public ConstraintItem getObject() {
    return object;
  }
  public void setObject(ConstraintItem object) {
    this.object = object;
  }
}
