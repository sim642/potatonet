package ee.potatonet.banklink;

import java.util.Collection;

public interface BanklinkRegistry {
  void registerBanklink(String name, Banklink banklink);

  Banklink getBanklink(String name);

  Collection<String> getBanklinkNames();
}
