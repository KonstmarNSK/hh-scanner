import utils.Client;
import utils.HHRequest;
import utils.dataobjects.vacancy.Vacancy;
import utils.requestbuilders.RequestBuilders;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws Exception {

        ExecutorService threadPool = Executors.newFixedThreadPool(10);


        Client client = new Client(threadPool);
        HHRequest<Stream<Vacancy>> req = RequestBuilders.findJob().setText("java").build();
        List<Vacancy> list = req.doRequest(client).get().collect(Collectors.toList());

        System.out.println(list);

        threadPool.shutdownNow();
    }
}
