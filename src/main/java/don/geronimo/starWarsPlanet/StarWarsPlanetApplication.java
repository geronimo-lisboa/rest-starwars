package don.geronimo.starWarsPlanet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"don.geronimo.starWarsPlanet", "don.geronimo.starWarsPlanet.controller"})
public class StarWarsPlanetApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarWarsPlanetApplication.class, args);
	}
}
