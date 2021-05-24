package storage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class Event {
    protected Map<String, Object> description = new HashMap<>();

    public Object get(String key) {
        return description.get(key);
    }
}
