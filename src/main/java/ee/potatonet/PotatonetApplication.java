package ee.potatonet;

import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PotatonetApplication {

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	public static void main(String[] args) {
		System.setProperty("timestamp", String.valueOf(System.currentTimeMillis()));

		SpringApplication.run(PotatonetApplication.class, args);
	}
}
