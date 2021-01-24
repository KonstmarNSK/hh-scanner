package utils.dataobjects.hhsearchparams.area;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import utils.Client;
import utils.json.JsonObjectResponseParser;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Country extends Area{
    public final String url;

    // todo: soft reference (children may have their children, so the whole object graph may be big)
    private volatile List<CountryPart> countryParts;

    private Country() {
        super(null, -1L);
        this.url = null;
    }

    public CompletableFuture<List<CountryPart>> fetchChildren(Client client) {
        if (countryParts != null) {
            return CompletableFuture.supplyAsync(() -> countryParts, client.getThreadPool());
        }

        return doFetchChildren(client);
    }

    private CompletableFuture<List<CountryPart>> doFetchChildren(Client client) {
        return client.makeRequest(URI.create(this.url))
                .thenApply(response -> {
                            var result = JsonObjectResponseParser.parseArray("areas",
                                    response.body(),
                                    CountryPart.class)
                                    .collect(Collectors.toUnmodifiableList());

                            countryParts = result;

                            return result;
                        }
                );
    }


}
