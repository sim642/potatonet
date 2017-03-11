package ee.potatonet.data;

import javax.persistence.Embeddable;

@Embeddable
public class Coordinates {

  private Float latitude;

  private Float longitude;

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
}
