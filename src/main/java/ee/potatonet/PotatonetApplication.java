package ee.potatonet;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;

import ee.potatonet.data.Post;
import ee.potatonet.data.User;
import ee.potatonet.data.repos.PostRepository;
import ee.potatonet.data.repos.UserRepository;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import nz.net.ultraq.thymeleaf.decorators.strategies.GroupingStrategy;

@SpringBootApplication
public class PotatonetApplication {

	public static void main(String[] args) {
		SpringApplication.run(PotatonetApplication.class, args);
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
		tomcat.addAdditionalTomcatConnectors(createSslConnector());
		return tomcat;
	}

	private Connector createSslConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
		try {
			File keystore = new ClassPathResource("eid/server.jks").getFile();
			File truststore = new ClassPathResource("eid/ca/ca.jks").getFile();
			connector.setScheme("https");
			connector.setSecure(true);
			connector.setPort(8446);
			protocol.setSSLEnabled(true);
			protocol.setKeystoreFile(keystore.getAbsolutePath());
			protocol.setKeystorePass("123456");
			protocol.setTruststoreFile(truststore.getAbsolutePath());
			protocol.setTruststorePass("123456");
			protocol.setClientAuth("true");
			return connector;
		}
		catch (IOException ex) {
			throw new IllegalStateException("can't access keystore: [" + "keystore"
					+ "] or truststore: [" + "keystore" + "]", ex);
		}
	}
	
	@Bean
	public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
	@Bean
	public DataSource dataSource() {
		EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.HSQL)
				.build();
		
		return db;
	}

	@Bean
	public TemplateEngine templateEngine() {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.addDialect(new LayoutDialect(new GroupingStrategy()));
		return engine;
	}

	@Bean
	public AvatarService avatarService(GravatarAvatarService gravatarAvatarService) {
		return gravatarAvatarService;
	}

	
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UserRepository userRepository;

	@PostConstruct
	public void mockSetUp() {
		User veiko = userRepository.save(new User("", "veiko.kaap@eesti.ee", "Veiko Kääp"));
		User tiit = userRepository.save(new User("", "tiit.oja@eesti.ee", "Tiit Ojasoo"));
		User simmo = userRepository.save(new User("", "simmo.saan@eesti.ee", "Simmo Saan"));

		createPost(veiko, "Tere, ma Veiko");
		createPost(tiit, "Tere, ma Tiit");
		createPost(simmo, "Tere, ma Simmo");

		veiko.addFriend(tiit);
		veiko.addFriend(simmo);
		tiit.addFriend(simmo);
		
		userRepository.save(veiko);
		userRepository.save(tiit);
		userRepository.save(simmo);
	}

	private void createPost(User user, String content) {
		Post entity = new Post(user, content);
		entity.setCreationDateTime(ZonedDateTime.now());
		postRepository.save(entity);
	}
}
