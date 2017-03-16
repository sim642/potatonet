package ee.potatonet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PotatonetApplication {

	public static void main(String[] args) {
		System.setProperty("timestamp", String.valueOf(System.currentTimeMillis()));

		SpringApplication.run(PotatonetApplication.class, args);
	}
}
