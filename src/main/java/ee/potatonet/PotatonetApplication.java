package ee.potatonet;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;

import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.Ssl;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
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
import ee.potatonet.eid.EIDCodeDetails;
import ee.potatonet.eid.EIDDetails;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import nz.net.ultraq.thymeleaf.decorators.strategies.GroupingStrategy;

@SpringBootApplication
public class PotatonetApplication {

	public static void main(String[] args) {
		SpringApplication.run(PotatonetApplication.class, args);
	}

	@Autowired
	private ServerProperties serverProperties;

	@Autowired
	private AppProperties appProperties;

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};
		tomcat.addAdditionalTomcatConnectors(createSslConnector());
		tomcat.addAdditionalTomcatConnectors(createHTTPConnector());
		return tomcat;
	}

	// Redirects requests to HTTP port 8080 to HTTPS port
	private Connector createHTTPConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");
		connector.setPort(8080);
		connector.setRedirectPort(appProperties.getDomainPort());
		return connector;
	}

	private Connector createSslConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
		connector.setScheme("https");
		connector.setSecure(true);
		connector.setPort(8446);

		protocol.setSSLEnabled(true);
		Ssl ssl = serverProperties.getSsl();
		protocol.setKeystoreFile(ssl.getKeyStore());
		protocol.setKeystorePass(ssl.getKeyStorePassword());
		protocol.setKeyPass(ssl.getKeyPassword());

		protocol.setTruststoreFile(appProperties.getTrustStore());
		protocol.setTruststorePass(appProperties.getTrustStorePassword());
		protocol.setClientAuth("true");

		return connector;
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
		User veiko = userRepository.save(new User(new EIDDetails(new EIDCodeDetails("38001230000"), "Veiko", "Kääp", "veiko.kaap@eesti.ee")));
		User tiit = userRepository.save(new User(new EIDDetails(new EIDCodeDetails("37001230000"), "Tiit", "Oja", "tiit.oja@eesti.ee")));
		User simmo = userRepository.save(new User(new EIDDetails(new EIDCodeDetails("36001230000"), "Simmo", "Saan", "simmo.saan@eesti.ee")));

		User wannaBeFriend = userRepository.save(new User(new EIDDetails(new EIDCodeDetails("49510201111"), "Wannabe", "Friend", "wannabe@eesti.ee")));
		veiko.getIncomingFriendRequests().add(wannaBeFriend);
		simmo.getIncomingFriendRequests().add(wannaBeFriend);
		tiit.getIncomingFriendRequests().add(wannaBeFriend);
		
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
