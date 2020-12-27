import utils.Client;
import utils.hhapi.HHApi;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main  {
    public static void main(String[] args) throws Exception {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        Client client = new Client(threadPool);
        HHApi api = new HHApi(client);

        String rep = api.searchJobsByTextContent("java").get().body();

        System.out.println(rep);
        threadPool.shutdownNow();
    }
}
