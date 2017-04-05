package ee.potatonet.banklink.pangalinknet;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
class ProjectDTO {

  private boolean success;
  private Project data;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public Project getData() {
    return data;
  }

  public void setData(Project data) {
    this.data = data;
  }
}
