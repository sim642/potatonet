package ee.potatonet.banklink;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

import ee.potatonet.banklink.pangalinknet.PangalinknetBanklinkProperties;
import ee.potatonet.banklink.properties.PropertiesBanklinkProperties;

@Component
@ConfigurationProperties(prefix = "banklink")
public class BanklinkProperties {

  @NestedConfigurationProperty
  private PropertiesBanklinkProperties properties;

  @NestedConfigurationProperty
  private PangalinknetBanklinkProperties pangalinknet;

  public PropertiesBanklinkProperties getProperties() {
    return properties;
  }

  public void setProperties(PropertiesBanklinkProperties properties) {
    this.properties = properties;
  }

  public PangalinknetBanklinkProperties getPangalinknet() {
    return pangalinknet;
  }

  public void setPangalinknet(PangalinknetBanklinkProperties pangalinknet) {
    this.pangalinknet = pangalinknet;
  }
}
