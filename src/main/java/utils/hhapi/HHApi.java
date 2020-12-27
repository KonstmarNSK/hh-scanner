package utils.hhapi;

import utils.Client;

import java.net.URI;
import java.net.http.HttpResponse;
import java.util.concurrent.Future;

public class HHApi {
    private final Client client;

    public HHApi(Client client) {
        this.client = client;
    }

    public Future<HttpResponse<String>> searchJobsByTextContent(String searchedContend){
        URI uri = UriBuilders.findJob().setText(searchedContend).build();
        return client.makeRequest(uri);
    }
}
