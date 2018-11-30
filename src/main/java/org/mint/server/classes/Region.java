package org.mint.server.classes;

import java.util.ArrayList;

public class Region extends URIEntity {
  private static final long serialVersionUID = -4220269038352458436L;

  String boundaryVector;
  
  ArrayList<Region> subRegions;
  String parentRegion;
  String placeholder;
  String image;
  String description;
  Float latitude;
  Float longitude;
  Integer zoom;
  
  public Region() {
    super();
  }
  
  public Region(String id, String name) {
    super(id, name);
    subRegions = new ArrayList<Region>();
  }
  
  @Override
  public void setID(String id) {
    super.setID(id);
  }
  
  public String getBoundaryVector() {
    return boundaryVector;
  }
  
  public void setBoundaryVector(String boundaryVector) {
    this.boundaryVector = boundaryVector;
  }
  
  public ArrayList<Region> getSubRegions() {
    return subRegions;
  }
  
  public void setSubRegions(ArrayList<Region> subRegions) {
    this.subRegions = subRegions;
  }
  
  public void addRegion(Region subRegion) {
    this.subRegions.add(subRegion);
  }
  
  public void removeRegion(Region subRegion) {
    this.subRegions.remove(subRegion);
  }

  public String getParentRegion() {
    return parentRegion;
  }

  public void setParentRegion(String parentRegion) {
    this.parentRegion = parentRegion;
  }

  public String getPlaceholder() {
    return placeholder;
  }

  public void setPlaceholder(String placeholder) {
    this.placeholder = placeholder;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Float getLatitude() {
    return latitude;
  }

  public void setLatitude(Float latitude) {
    this.latitude = latitude;
  }

  public Float getLongitude() {
    return longitude;
  }

  public void setLongitude(Float longitude) {
    this.longitude = longitude;
  }

  public Integer getZoom() {
    return zoom;
  }

  public void setZoom(Integer zoom) {
    this.zoom = zoom;
  }
}
