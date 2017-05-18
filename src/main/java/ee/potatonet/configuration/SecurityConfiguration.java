package ee.potatonet.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Security;
import java.util.Map;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfiguration {

  static {
    Security.addProvider(new BouncyCastleProvider());
    removeCryptographyRestrictions();
  }

  // http://stackoverflow.com/a/22492582
  private static void removeCryptographyRestrictions() {
    if (!isRestrictedCryptography()) {
//      logger.fine("Cryptography restrictions removal not needed");
      return;
    }
    try {
      /*
       * Do the following, but with reflection to bypass access checks:
       *
       * JceSecurity.isRestricted = false;
       * JceSecurity.defaultPolicy.perms.clear();
       * JceSecurity.defaultPolicy.add(CryptoAllPermission.INSTANCE);
       */
      final Class<?> jceSecurity = Class.forName("javax.crypto.JceSecurity");
      final Class<?> cryptoPermissions = Class.forName("javax.crypto.CryptoPermissions");
      final Class<?> cryptoAllPermission = Class.forName("javax.crypto.CryptoAllPermission");

      final Field isRestrictedField = jceSecurity.getDeclaredField("isRestricted");
      isRestrictedField.setAccessible(true);
      final Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(isRestrictedField, isRestrictedField.getModifiers() & ~Modifier.FINAL);
      isRestrictedField.set(null, false);

      final Field defaultPolicyField = jceSecurity.getDeclaredField("defaultPolicy");
      defaultPolicyField.setAccessible(true);
      final PermissionCollection defaultPolicy = (PermissionCollection) defaultPolicyField.get(null);

      final Field perms = cryptoPermissions.getDeclaredField("perms");
      perms.setAccessible(true);
      ((Map<?, ?>) perms.get(defaultPolicy)).clear();

      final Field instance = cryptoAllPermission.getDeclaredField("INSTANCE");
      instance.setAccessible(true);
      defaultPolicy.add((Permission) instance.get(null));

//      logger.fine("Successfully removed cryptography restrictions");
    } catch (final Exception e) {
//      logger.log(Level.WARNING, "Failed to remove cryptography restrictions", e);
    }
  }

  private static boolean isRestrictedCryptography() {
    // This simply matches the Oracle JRE, but not OpenJDK.
    return "Java(TM) SE Runtime Environment".equals(System.getProperty("java.runtime.name"));
  }
}
