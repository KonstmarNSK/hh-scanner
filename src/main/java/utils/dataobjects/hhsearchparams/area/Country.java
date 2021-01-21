package utils.dataobjects.hhsearchparams.area;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import utils.Client;
import utils.json.JsonObjectResponseParser;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {
    public final String url;
    public final String name;
    public final Long id;

    // todo: soft reference (children may have their children, so the whole object graph may be big)
    private List<Area> areas;

    private Country() {
        this.url = null;
        this.name = null;
        this.id = -1L;
    }

    public CompletableFuture<List<Area>> fetchChildren(Client client) {
        if (areas != null) {
            var result = new CompletableFuture<List<Area>>();
            result.complete(areas);

            return result;
        }

        return doFetchChildren(client);
    }

    private CompletableFuture<List<Area>> doFetchChildren(Client client) {
        return client.makeRequest(URI.create(this.url))
                .thenApply(response ->
                        JsonObjectResponseParser.parseArray("areas", response.body(), Area.class)
                                .collect(Collectors.toList())
                );
    }
}
