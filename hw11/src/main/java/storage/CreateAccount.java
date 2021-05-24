package storage;


import java.time.Instant;

public class CreateAccount extends Event {
    public CreateAccount(Instant date, String name, int visitations) {
        super();
        description.put("DATE", date);
        description.put("NAME", name);
        description.put("VISITATIONS", visitations);
    }
}
