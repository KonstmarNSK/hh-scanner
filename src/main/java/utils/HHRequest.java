package utils;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class HHRequest<TResult> {
    private final URI uri;
    private final Function<HttpResponse<InputStream>, TResult> responseHandler;

    public HHRequest(URI uri, Function<HttpResponse<InputStream>, TResult> responseHandler) {
        this.uri = uri;
        this.responseHandler = responseHandler;
    }

    public CompletableFuture<TResult> doRequest(Client client) {
        return client.makeRequest(uri).thenApplyAsync(responseHandler);
    }
}
