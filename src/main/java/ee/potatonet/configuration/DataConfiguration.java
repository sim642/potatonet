package ee.potatonet.configuration;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import ee.potatonet.data.service.avatar.AvatarService;
import ee.potatonet.data.service.avatar.GravatarAvatarService;

@Configuration
public class DataConfiguration {

  @Bean
  public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
    return new JdbcTemplate(dataSource);
  }

  @Bean
  public AvatarService avatarService(GravatarAvatarService gravatarAvatarService) {
    return gravatarAvatarService;
  }
}
