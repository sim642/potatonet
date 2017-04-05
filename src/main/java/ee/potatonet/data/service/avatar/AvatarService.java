package ee.potatonet.data.service.avatar;

import ee.potatonet.data.model.User;

public interface AvatarService {
  String getAvatarUrl(User user, int size);
}
