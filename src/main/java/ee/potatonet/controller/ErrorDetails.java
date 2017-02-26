package ee.potatonet.controller;

import java.util.Map;

public class ErrorDetails {

  public Integer status;
  public String error;
  public String message;
  public String timeStamp;
  public String trace;

  public ErrorDetails(int status, Map<String, Object> errorAttributes) {
    this.status = status;
    this.error = (String) errorAttributes.get("error");
    this.message = (String) errorAttributes.get("message");
    this.timeStamp = errorAttributes.get("timestamp").toString();
    this.trace = (String) errorAttributes.get("trace");
  }

}