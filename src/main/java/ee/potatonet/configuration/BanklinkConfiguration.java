package ee.potatonet.configuration;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import com.springcryptoutils.core.key.PrivateKeyFactoryBean;
import com.springcryptoutils.core.keystore.KeyStoreFactoryBean;
import com.springcryptoutils.core.signature.Signer;
import com.springcryptoutils.core.signature.SignerImpl;

@Configuration
public class BanklinkConfiguration {

  @Autowired
  private ResourceLoader resourceLoader;

  @Bean
  public KeyStore keyStore() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, IOException {
    KeyStoreFactoryBean keyStoreFactory = new KeyStoreFactoryBean();
    keyStoreFactory.setLocation(resourceLoader.getResource("file:./banklink/banklink.jks"));
    keyStoreFactory.setPassword("ipizza");
    keyStoreFactory.afterPropertiesSet();
    return ((KeyStore) keyStoreFactory.getObject());
  }

  @Bean
  public PrivateKey privateKey() throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, CertificateException, IOException, NoSuchProviderException {
    PrivateKeyFactoryBean privateKeyFactory = new PrivateKeyFactoryBean();
    privateKeyFactory.setKeystore(keyStore());
    privateKeyFactory.setAlias("testpank");
    privateKeyFactory.setPassword("pizza");
    privateKeyFactory.afterPropertiesSet();
    return (PrivateKey) privateKeyFactory.getObject();
  }

  @Bean
  public Signer signer() throws CertificateException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, UnrecoverableEntryException, IOException {
    SignerImpl signer = new SignerImpl();
    signer.setAlgorithm("SHA1withRSA");
    signer.setPrivateKey(privateKey());
    return signer;
  }
}
