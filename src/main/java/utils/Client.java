package utils;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public class Client {
    private final HttpClient httpClient;

    public Client(Executor executor) {
        httpClient = HttpClient.newBuilder()
                .executor(executor)
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }


    public Future<HttpResponse<String>> makeRequest(URI targetUri){
        HttpRequest request = HttpRequest.newBuilder()
                .uri(targetUri)
                .timeout(Duration.ofMinutes(2))
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
}
