package reproducer;

import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.core.http.HttpClient;
import io.vertx.rxjava.core.http.HttpClientRequest;
import io.vertx.rxjava.ext.web.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static reproducer.AppStarter.PATH_AND_QUERY;
import static reproducer.AppStarter.PORT;

@Component
public class Client {

    @Autowired
    private Vertx vertx;

    @PostConstruct
    public void init() {
        vertx.setPeriodic(5000, aLong -> {
            callRemoteServerViaHttpClient();
            callRemoteServerViaWebClient();
        });
    }

    private void callRemoteServerViaHttpClient() {
        HttpClient httpClient = vertx.createHttpClient(getOptions());
        HttpClientRequest clientRequest = httpClient
                .request(HttpMethod.GET, PATH_AND_QUERY)
                .setTimeout(5000);

        clientRequest.toObservable().subscribe(httpClientResponse -> {
            Buffer buffer = Buffer.buffer();
            httpClientResponse.handler(buffer::appendBuffer);
            httpClientResponse.endHandler(aVoid -> System.out.println("Query received by remote server when HttpClient is used :: " + buffer.toString()));
        });
        clientRequest.end();
    }

    private void callRemoteServerViaWebClient() {
        WebClient webClient = WebClient.create(vertx, new WebClientOptions(getOptions()));
        webClient
                .request(HttpMethod.GET, PATH_AND_QUERY)
                .timeout(5000)
                .rxSend()
                .subscribe(httpResponse -> {
                    System.out.println("Query received by remote server when WebClient  is used :: " + httpResponse.bodyAsString());
                });
    }

    private HttpClientOptions getOptions() {
        return new HttpClientOptions()
                .setDefaultPort(PORT)
                .setDefaultHost("localhost");
    }
}