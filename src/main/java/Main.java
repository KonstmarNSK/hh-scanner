import utils.AreaSearch;
import utils.Client;
import utils.HHSearchParams;
import utils.requestbuilders.RequestBuilders;

import java.lang.ref.Cleaner;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {

        var threadPool = Executors.newFixedThreadPool(10);


        var client = new Client(threadPool);
        var hhSearchParams = new HHSearchParams(client);
        var exp3Years = hhSearchParams.getAllPossibleExperienceLevels().stream()
                .filter(exp -> exp.id.equals("between1And3"))
                .findAny()
                .orElseThrow();

        var russia = hhSearchParams.getCountries().stream()
                .filter(country -> country.name.equals("Россия"))
                .findAny()
                .orElseThrow();

        var req = RequestBuilders.findJob()
                .findTextOccurrences("java")
                .withExperience(exp3Years)
                .inArea(russia)
                .build();

        var list = req.doRequest(client).get().collect(Collectors.toList());

        System.out.println(list);

        var r = AreaSearch.findChildrenByName("Новосибирск", russia, client).get();
        r = AreaSearch.findChildrenByName("Новосибирск", russia, client).get();

        threadPool.shutdownNow();

        for(int i = 0; i< 100; i++){
            System.gc();
        }

    }
}
