package storage;

import java.time.Instant;

public class Leave extends Event {
    public Leave(Instant date) {
        super();
        description.put("TIME", date);
    }
}
