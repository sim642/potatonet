package ee.potatonet;

import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PotatonetApplication {

	public static void main(String[] args) {
		System.setProperty("timestamp", String.valueOf(System.currentTimeMillis()));

		Security.addProvider(new BouncyCastleProvider());

		SpringApplication.run(PotatonetApplication.class, args);
	}
}
