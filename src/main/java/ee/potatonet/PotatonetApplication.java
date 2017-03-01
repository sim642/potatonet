package ee.potatonet;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.util.Collection;
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
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ITemplateResolver;

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

			@Override
			protected void customizeConnector(Connector connector) {
				super.customizeConnector(connector);
				connector.setParseBodyMethods("POST,PUT,DELETE");
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
	public SpringTemplateEngine springTemplateEngine(Collection<ITemplateResolver> templateResolvers, ServletContext servletContext) {
		SpringTemplateEngine engine = new SpringTemplateEngine();
		templateResolvers.forEach(engine::addTemplateResolver);

		engine.addDialect(new LayoutDialect(new GroupingStrategy()));
		engine.setLinkBuilder(new TemplateRenderService.LinkBuilder(servletContext));
		return engine;
	}

	@Bean
	public AvatarService avatarService(GravatarAvatarService gravatarAvatarService) {
		return gravatarAvatarService;
	}
}
