package utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Iterator;

/**
 * Iterator which lazily parses elements from json array.
 *
 * @param <T> type of elements returned by this iterator
 */
public class JsonListIter<T> implements Iterator<T> {
    private final JsonParser parser;
    private final ObjectMapper mapper;
    private final Class<T> itemType;

    private T next;
    private boolean firstElementRead = false;
    private boolean hasErrors = false;

    /**
     *
     * @param parser jackson's {@link JsonParser}.
     *               !NOTE: current parser's position must be {@link JsonToken#START_OBJECT} (first object in array)
     * @param mapper jackson's {@link ObjectMapper} which is used for array's elements' deserializaton
     * @param itemType type of array's elements
     */
    public JsonListIter(JsonParser parser, ObjectMapper mapper, Class<T> itemType) {
        this.parser = parser;
        this.mapper = mapper;
        this.itemType = itemType;

        readNext();
    }

    @Override
    public boolean hasNext() {
        return (next != null) && !hasErrors;
    }

    @Override
    public T next() {
        T result = next;

        readNext();
        return result;
    }

    private void readNext() {
        if (firstElementRead && next == null) {
            return;
        }

        if (hasErrors) {
            return;
        }

        if (!firstElementRead) {
            firstElementRead = true;
        }

        try {
            // end of list of objects, iterator has no more elements
            if (parser.nextToken() == JsonToken.END_ARRAY) {
                next = null;
                return;
            }

            next = mapper.readValue(parser, itemType);
        } catch (Exception e) {
            hasErrors = true;
        }
    }
}
