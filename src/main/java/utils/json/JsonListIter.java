package utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Cleaner;
import java.util.Iterator;

/**
 * Iterator which lazily parses elements from json array.
 *
 * @param <T> type of elements returned by this iterator
 */
public class JsonListIter<T> implements Iterator<T> {
    private static final Cleaner cleaner = Cleaner.create();
    private final Cleaner.Cleanable cleanable;

    private final JsonParser parser;
    private final ObjectMapper mapper;
    private final Class<T> itemType;
    private final StateToCleanUp stateToCleanUp;
    private final boolean closeISOnArrayEnd;
    private final boolean closeISOnErrors;

    private T next;
    private boolean firstElementRead = false;
    private boolean hasErrors = false;

    public static <T> IterBuilder<T> builder() {
        return new IterBuilder<>();
    }

    /**
     * @param parser   jackson's {@link JsonParser}.
     *                 !NOTE: current parser's position must be {@link JsonToken#START_OBJECT} (first object in array)
     * @param mapper   jackson's {@link ObjectMapper} which is used for array's elements' deserializaton
     * @param itemType type of array's elements
     */
    private JsonListIter(InputStream source,
                         JsonParser parser,
                         ObjectMapper mapper,
                         Class<T> itemType,
                         boolean closeISOnEndOfArray,
                         boolean closeISOnErrors,
                         boolean closeISOnIterGCCollected) {

        this.parser = parser;
        this.mapper = mapper;
        this.itemType = itemType;

        this.stateToCleanUp = new StateToCleanUp(source);

        // in order to close input stream when this iterator is about to be deleted by GC
        if (closeISOnIterGCCollected) {
            cleanable = cleaner.register(this, stateToCleanUp);
        } else {
            cleanable = null;
        }
        this.closeISOnArrayEnd = closeISOnEndOfArray;
        this.closeISOnErrors = closeISOnErrors;

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
                if (closeISOnArrayEnd) {
                    cleanable.clean();
                }
                next = null;
                return;
            }

            next = mapper.readValue(parser, itemType);
        } catch (Exception e) {
            if (closeISOnErrors) {
                cleanable.clean();
            }
            hasErrors = true;
        }
    }


    private static final class StateToCleanUp implements Runnable {
        private final InputStream source;

        public StateToCleanUp(InputStream source) {
            this.source = source;
        }

        @Override
        public void run() {
            try {
                source.close();
            } catch (IOException e) {
                // todo: log
            }
        }
    }

    public static final class IterBuilder<T> {
        private InputStream source;
        private JsonParser parser;
        private ObjectMapper mapper;
        private Class<T> itemType;
        private boolean closeISOnEndOfArray;
        private boolean closeISOnErrors;
        private boolean closeISOnIterGCCollected;

        public IterBuilder<T> setSource(InputStream source) {
            this.source = source;
            return this;
        }

        public IterBuilder<T> setParser(JsonParser parser) {
            this.parser = parser;
            return this;
        }

        public IterBuilder<T> setMapper(ObjectMapper mapper) {
            this.mapper = mapper;
            return this;
        }

        public IterBuilder<T> setItemType(Class<T> itemType) {
            this.itemType = itemType;
            return this;
        }

        public IterBuilder<T> setCloseISOnEndOfArray(boolean closeISOnEndOfArray) {
            this.closeISOnEndOfArray = closeISOnEndOfArray;
            return this;
        }

        public IterBuilder<T> setCloseISOnErrors(boolean closeISOnErrors) {
            this.closeISOnErrors = closeISOnErrors;
            return this;
        }

        public IterBuilder<T> setCloseISOnIterGCCollected(boolean closeISOnIterGCCollected) {
            this.closeISOnIterGCCollected = closeISOnIterGCCollected;
            return this;
        }

        public JsonListIter<T> build() {
            return new JsonListIter<>(
                    source,
                    parser,
                    mapper,
                    itemType,
                    closeISOnEndOfArray,
                    closeISOnErrors,
                    closeISOnIterGCCollected
            );
        }
    }
}
