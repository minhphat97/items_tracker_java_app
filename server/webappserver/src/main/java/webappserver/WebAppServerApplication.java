/**
 * A class is used to start the SpringBoot server.
 *
 * @author MinhPhatTran
 */
package ca.cmpt213.a4.webappserver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Windows 10 is used for this project
@SpringBootApplication
public class WebAppServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(WebAppServerApplication.class, args);
	}
}// WebAppServerApplication.java
