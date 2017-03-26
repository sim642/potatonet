package ee.potatonet.banklink;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractBanklinkRequest {

  private String service;
  private String version = "008";
  private String encoding = "UTF-8";
  private String lang;

  protected Banklink banklink;

  public AbstractBanklinkRequest(String service, Banklink banklink) {
    this.service = service;
    this.banklink = banklink;
  }

  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getEncoding() {
    return encoding;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public Banklink getBanklink() {
    return banklink;
  }

  public void setBanklink(Banklink banklink) {
    this.banklink = banklink;
  }

  protected abstract LinkedHashMap<String, String> getParams();

  public Map<String, String> getCompleteParams() {
    LinkedHashMap<String, String> completeParams = new LinkedHashMap<>();
    completeParams.put("VK_SERVICE", service);
    completeParams.put("VK_VERSION", version);
    completeParams.putAll(getParams());

    completeParams.put("VK_MAC", BanklinkUtils.signParams(banklink, completeParams));
    completeParams.put("VK_ENCODING", encoding);
    completeParams.put("VK_LANG", lang);

    return completeParams;
  }
}
