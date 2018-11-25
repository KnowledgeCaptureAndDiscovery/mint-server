package org.mint.server.classes.question.template;

import java.util.Date;

public class TemplateTimePeriod {

  public Date fromDate;
  public Date toDate;

  public Date getFromDate() {
    return fromDate;
  }

  public void setFromDate(Date fromDate) {
    this.fromDate = fromDate;
  }

  public Date getToDate() {
    return toDate;
  }

  public void setToDate(Date toDate) {
    this.toDate = toDate;
  }

}
