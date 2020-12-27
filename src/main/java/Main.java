import utils.Client;
import utils.uribuilders.UriBuilders;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main  {
    public static void main(String[] args) throws Exception {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        Client client = new Client(threadPool);
        URI uri = UriBuilders.findJob().setText("java").build();
        String rep = client.makeRequest(uri).get().body();

        System.out.println(rep);
        threadPool.shutdownNow();
    }
}
