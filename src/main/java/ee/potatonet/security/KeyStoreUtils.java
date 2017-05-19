package ee.potatonet.security;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.List;

public final class KeyStoreUtils {
  private KeyStoreUtils() {

  }

  public static Path newTemporaryPrivateKeyStore(char[] password, PrivateKey privateKey, List<Certificate> fullChain) throws IOException {
    try {
      KeyStore keyStore = KeyStore.getInstance("jks");
      keyStore.load(null, password);

      Certificate[] rawFullChain = fullChain.toArray(new Certificate[0]);
      keyStore.setKeyEntry("privatekey", privateKey, password, rawFullChain);

      Path path = Files.createTempFile(null, ".jks");
      try (OutputStream os = Files.newOutputStream(path)) {
        keyStore.store(os, password);
      }

      return path;
    }
    catch (KeyStoreException | CertificateException | NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}
