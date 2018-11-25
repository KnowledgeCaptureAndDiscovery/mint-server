package org.mint.server.classes;

import java.util.Date;

public class Dataset extends URIEntity {
  private static final long serialVersionUID = -1630837293494837448L;

  String dataCatalogId;
  
  Region associatedWithRegion;
  
  Date fromDate;
  
  Date toDate;
  
  String dataType;

  public Dataset(String id, String name) {
    super(id, name);
  }
  
  public String getDataCatalogId() {
    return dataCatalogId;
  }

  public void setDataCatalogId(String dataCatalogId) {
    this.dataCatalogId = dataCatalogId;
  }

  public Region getAssociatedWithRegion() {
    return associatedWithRegion;
  }

  public void setAssociatedWithRegion(Region associatedWithRegion) {
    this.associatedWithRegion = associatedWithRegion;
  }

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

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }


}
