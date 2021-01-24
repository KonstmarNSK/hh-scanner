package utils;

import utils.dataobjects.hhsearchparams.area.Area;
import utils.dataobjects.hhsearchparams.area.Country;
import utils.dataobjects.hhsearchparams.area.CountryPart;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class AreaSearch {

    // TODO: 24.01.2021 soft reference
    private static Map<Tuple<String, Country>, Set<Area>> searchCache = new ConcurrentHashMap<>();

    public static CompletableFuture<Set<Area>> findChildrenByName(String partOfName, Country country, Client client) {
        var argsTuple = new Tuple<>(partOfName, country);
        var cached = searchCache.get(argsTuple);

        if (cached != null) {
            return CompletableFuture.supplyAsync(() -> cached, client.getThreadPool());
        }

        return country.fetchChildren(client)
                .thenApplyAsync(
                        children -> findInChildren(partOfName, children, new HashSet<>()),
                        client.getThreadPool()
                ).thenApply(areas -> {
                    searchCache.put(argsTuple, areas);
                    return areas;
                });
    }

    private static Set<Area> findInChildren(String partOfName, List<CountryPart> children, Set<Area> result) {
        children.forEach(child -> findInChild(partOfName, child, result));

        return result;
    }

    private static void findInChild(String partOfName, CountryPart countryPart, Set<Area> result) {

        if (countryPart.name.contains(partOfName)) {
            result.add(countryPart);
        }

        var children = countryPart.getChildren();

        // is leaf
        if (children == null || children.isEmpty()) {
            return;
        }

        findInChildren(partOfName, children, result);
    }
}
