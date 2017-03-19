package ee.potatonet.banklink.properties;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;

import ee.potatonet.banklink.BanklinkRegistrar;
import ee.potatonet.banklink.BanklinkRegistry;

public class PropertiesBanklinkRegistrar implements BanklinkRegistrar {

  private final PropertiesBanklinkProperties properties;

  public PropertiesBanklinkRegistrar(PropertiesBanklinkProperties properties) {
    this.properties = properties;
  }

  @Override
  public void registerBanklinks(BanklinkRegistry registry) {
    try {
      KeyStore keyStore = KeyStore.getInstance("JKS");
      keyStore.load(properties.getKeyStore().getInputStream(), properties.getKeyStorePassword().toCharArray());

      Map<String, PropertiesBanklink> banklinks = properties.getBanklinks();
      banklinks.forEach((name, banklink) -> {
        if (banklink.getKeyAlias() == null)
          banklink.setKeyAlias(name);

        try {
          PrivateKey privateKey = (PrivateKey) keyStore.getKey(banklink.getKeyAlias(), banklink.getKeyPassword().toCharArray());
          banklink.setPrivateKey(privateKey);
        }
        catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
          throw new RuntimeException(e);
        }
      });

      banklinks.forEach(registry::registerBanklink);
    }
    catch (CertificateException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
      throw new RuntimeException(e);
    }
  }
}
