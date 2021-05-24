package cores;

import exceptions.EventValueException;
import exceptions.NullException;
import exceptions.TurnstileException;
import storage.*;
import utils.Utils;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

public class Turnstile {
    private final EventStorage storage;
    private final Clock clock;

    public Turnstile(EventStorage s, Clock c) {
        storage = s;
        clock = c;
    }

    public void checkIn(int accountId) throws NullException, EventValueException, TurnstileException {
        Account account = Utils.recreateAccount(accountId, storage);
        if (account.getInside()) {
            throw new TurnstileException("User can not enter while already inside");
        }
        if (account.getVisitations() == 0) {
            throw new TurnstileException("User can not enter while has no visitations left");
        }
        storage.addEvent(accountId, new Enter(clock.instant()));
    }

    public void checkOut(int accountId) throws NullException, EventValueException, TurnstileException {
        Account account = Utils.recreateAccount(accountId, storage);
        if (!account.getInside()) {
            throw new TurnstileException("User can not leave while not inside");
        }
        storage.addEvent(accountId, new Leave(clock.instant()));
    }
}
