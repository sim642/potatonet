package ee.potatonet.banklink.pangalinknet;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
class ProjectsDTO {

  private boolean success;
  private Projects data;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public Projects getData() {
    return data;
  }

  public void setData(Projects data) {
    this.data = data;
  }
}
