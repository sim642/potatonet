package ee.potatonet;

import ee.potatonet.data.User;

public interface AvatarService {
  String getAvatarUrl(User user, int size);
}
