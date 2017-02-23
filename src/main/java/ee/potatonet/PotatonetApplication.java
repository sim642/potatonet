package ee.potatonet;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;

import ee.potatonet.data.Post;
import ee.potatonet.data.PostService;
import ee.potatonet.data.User;
import ee.potatonet.data.UserService;
import ee.potatonet.data.repos.PostRepository;
import ee.potatonet.eid.EIDCodeDetails;
import ee.potatonet.eid.EIDDetails;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import nz.net.ultraq.thymeleaf.decorators.strategies.GroupingStrategy;

@SpringBootApplication
public class PotatonetApplication {

	public static void main(String[] args) {
		System.setProperty("timestamp", String.valueOf(System.currentTimeMillis()));
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
	private PostService postService;
	@Autowired
	private UserService userService;


	@PostConstruct
	public void mockSetUp() {
		User veiko = createUser("38001230000", "Veiko", "Kääp", "veiko.kaap@eesti.ee", "veiko");
		User tiit = createUser("37001230000", "Tiit", "Oja", "tiit.oja@eesti.ee", "tiit");
		User simmo = createUser("36001230000", "Simmo", "Saan", "simmo.saan@eesti.ee", "simmo");

		User wannaBeFriend = createUser("49510201111", "Wannabe", "Friend", "wannabe@eesti.ee", "wannabe");
		veiko.getIncomingFriendRequests().add(wannaBeFriend);
		simmo.getIncomingFriendRequests().add(wannaBeFriend);
		tiit.getIncomingFriendRequests().add(wannaBeFriend);
		
		createPost(veiko, "Tere, ma Veiko");
		createPost(tiit, "Tere, ma Tiit");
		createPost(simmo, "Tere, ma Simmo");

		veiko.addFriend(tiit);
		veiko.addFriend(simmo);
		tiit.addFriend(simmo);
		
		userService.save(veiko);
		userService.save(tiit);
		userService.save(simmo);
	}

	private User createUser(String idCode, String givenName, String surname, String email, String password) {
		User user = new User(new EIDDetails(new EIDCodeDetails(idCode), givenName, surname, email));
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setPassword(passwordEncoder.encode(password));
		return userService.save(user);
	}

	private void createPost(User user, String content) {
		Post post = new Post();
		post.setContent(content);
		postService.savePostToUser(post, user);
	}
}
