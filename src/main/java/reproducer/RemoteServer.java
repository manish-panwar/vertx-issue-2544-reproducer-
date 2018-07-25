package reproducer;

import io.vertx.core.http.HttpServerOptions;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.http.HttpServer;
import io.vertx.rxjava.ext.web.Router;
import io.vertxbeans.rxjava.ContextRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;

import javax.annotation.PostConstruct;

import static java.util.concurrent.TimeUnit.MINUTES;
import static reproducer.AppStarter.PORT;

@Component
public class RemoteServer {

    @Autowired
    private Vertx vertx;
    @Autowired
    private ContextRunner contextRunner;

    @PostConstruct
    public void init() throws Exception {
        contextRunner.executeBlocking(1, () -> createHttpListener().buffer(2), 1, MINUTES);
    }

    private Observable<HttpServer> createHttpListener() {
        return vertx.createHttpServer(new HttpServerOptions().setPort(PORT))
                .requestHandler(createRoutes()::accept)
                .rxListen()
                .toObservable()
                .doOnCompleted(() -> System.out.println("Remote server started on port " + PORT + ". Please wait 5 seconds ..."))
                .doOnError(ex -> ex.printStackTrace());
    }

    private Router createRoutes() {
        Router router = Router.router(vertx);
        router.get("/remote-server").handler(context -> {
            context.response()
                    .setChunked(true)
                    .write(context.request().query())
                    .end();
        });
        return router;
    }
}