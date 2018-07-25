package reproducer;

import io.vertxbeans.rxjava.VertxBeans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(VertxBeans.class)
public class AppStarter {

    public static final int PORT = 8085;
    public static final String PATH_AND_QUERY = "/remote-server?jREJBBB5x2AaiSSDO0/OskoCztDZBAAAAAADV1A4";

    public static void main(final String... args) {
        new SpringApplication(AppStarter.class).run(args);
    }
}