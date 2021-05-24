package utils;

import cores.Account;
import exceptions.EventValueException;
import exceptions.NullException;
import storage.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class Utils {
    public static Object throwIfNull(Object obj) throws NullException {
        if (obj == null) {
            throw new NullException();
        }
        return obj;
    }

    public static Integer calculateDay(Instant instant) {
        return Instant.now().atZone(ZoneId.systemDefault()).getDayOfYear();
    }

    public static Account recreateAccount(int id, EventStorage storage) throws NullException, EventValueException {
        List<Event> events = storage.getEvents(id);
        if (events == null) {
            throw new EventValueException("Id " + id + " doesn't exist");
        }
        Account account = new Account(id);
        account.setCreated((Instant) Utils.throwIfNull(events.get(0).get("DATE")));
        account.setName((String) Utils.throwIfNull(events.get(0).get("NAME")));
        int visitations = (int) Utils.throwIfNull(events.get(0).get("VISITATIONS"));
        boolean inside = false;
        Instant lastTime = account.getCreated();
        for (int i = 1; i < events.size(); i++) {
            Event event = events.get(i);
            if (event instanceof Enter) {
                inside = true;
                visitations -= 1;
            } else if (event instanceof Leave) {
                inside = false;
                lastTime = (Instant) Utils.throwIfNull(events.get(i).get("TIME"));
            } else if (event instanceof ExtendAccount) {
                visitations += (int) Utils.throwIfNull(events.get(i).get("VISITATIONS"));
            }
        }
        account.setInside(inside);
        account.setLastVisit(lastTime);
        account.setVisitations(visitations);
        return account;
    }
}
