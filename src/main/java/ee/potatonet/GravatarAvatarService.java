package ee.potatonet;

import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import ee.potatonet.data.User;

@Service
class GravatarAvatarService implements AvatarService {
  @Override
  public String getAvatarUrl(User user, int size) {
    return String.format("https://www.gravatar.com/avatar/%s?s=%d", getEmailHash(user.getEstMail()), size);
  }

  private static String getEmailHash(String email) {
    return DigestUtils.md5DigestAsHex(email.trim().toLowerCase().getBytes());
  }
}
