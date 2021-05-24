package storage;

import java.time.Instant;

public class Enter extends Event {
    public Enter(Instant date) {
        super();
        description.put("TIME", date);
    }
}
