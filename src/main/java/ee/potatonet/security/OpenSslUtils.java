package ee.potatonet.security;

import java.io.IOException;
import java.io.Reader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.openssl.PEMReader;

public final class OpenSslUtils {
  private OpenSslUtils() {

  }

  public static Certificate readCertificate(Reader reader) throws IOException {
    return readCertificates(reader).get(0);
  }

  public static List<Certificate> readCertificates(Reader reader) throws IOException {
    try (PEMReader pemReader = new PEMReader(reader)) {
      List<Certificate> certificates = new ArrayList<>();
      Object object;
      while ((object = pemReader.readObject()) != null)
        certificates.add(((Certificate) object));
      return certificates;
    }
  }

  public static PrivateKey readPrivateKey(Reader reader) throws IOException {
    return readPrivateKey(reader, null);
  }

  public static PrivateKey readPrivateKey(Reader reader, char[] password) throws IOException {
    try (PEMReader pemReader = new PEMReader(reader, () -> password)) {
      Object object = pemReader.readObject();
      if (object instanceof KeyPair)
        return ((KeyPair) object).getPrivate();
      else
        return ((PrivateKey) object);
    }
  }
}
