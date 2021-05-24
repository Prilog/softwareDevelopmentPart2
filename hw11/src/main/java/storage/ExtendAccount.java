package storage;

import java.time.Instant;

public class ExtendAccount extends Event {
    public ExtendAccount(Instant date, int visitations) {
        super();
        description.put("DATE", date);
        description.put("VISITATIONS", visitations);
    }
}
