package ee.potatonet.security;

public interface SslKeyStore {
  String getKeyStoreFile();
  String getKeyStorePassword();
  String getKeyAlias();
  String getKeyPassword();
}
