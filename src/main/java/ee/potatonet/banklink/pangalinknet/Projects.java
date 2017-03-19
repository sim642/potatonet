package ee.potatonet.banklink.pangalinknet;

import java.util.List;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
class Projects {

  private List<Project> list;

  public List<Project> getList() {
    return list;
  }

  public void setList(List<Project> list) {
    this.list = list;
  }
}
