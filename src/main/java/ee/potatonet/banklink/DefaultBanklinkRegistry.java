package ee.potatonet.banklink;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultBanklinkRegistry implements BanklinkRegistry {

  private final Map<String, Banklink> banklinks;

  public DefaultBanklinkRegistry() {
    banklinks = new HashMap<>();
  }

  @Override
  public void registerBanklink(String name, Banklink banklink) {
    banklinks.putIfAbsent(name, banklink);
  }

  @Override
  public Banklink getBanklink(String name) {
    return banklinks.get(name);
  }

  @Override
  public Collection<String> getBanklinkNames() {
    return banklinks.keySet();
  }
}
