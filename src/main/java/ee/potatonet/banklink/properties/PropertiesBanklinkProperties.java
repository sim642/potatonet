package ee.potatonet.banklink.properties;

import java.util.Map;
import org.springframework.core.io.Resource;

public class PropertiesBanklinkProperties {

  private Map<String, PropertiesBanklink> banklinks;

  private Resource keyStore;
  private String keyStorePassword;

  public Map<String, PropertiesBanklink> getBanklinks() {
    return banklinks;
  }

  public void setBanklinks(Map<String, PropertiesBanklink> banklinks) {
    this.banklinks = banklinks;
  }

  public Resource getKeyStore() {
    return keyStore;
  }

  public void setKeyStore(Resource keyStore) {
    this.keyStore = keyStore;
  }

  public String getKeyStorePassword() {
    return keyStorePassword;
  }

  public void setKeyStorePassword(String keyStorePassword) {
    this.keyStorePassword = keyStorePassword;
  }
}
