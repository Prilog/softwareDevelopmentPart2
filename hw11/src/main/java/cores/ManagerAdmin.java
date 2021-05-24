package cores;

import exceptions.EventValueException;
import exceptions.ManagerException;
import exceptions.NullException;
import storage.*;
import utils.Utils;

import java.time.Clock;

public class ManagerAdmin {
    private EventStorage storage;
    private Clock clock;

    public ManagerAdmin(EventStorage s, Clock c) {
        storage = s;
        clock = c;
    }

    public Account getAccount(int accountId) throws NullException, EventValueException {
        return Utils.recreateAccount(accountId, storage);
    }

    public void createAccount(int accountId, String name, int visitations) throws ManagerException, NullException {
        if (storage.isExist(accountId)) {
            throw new ManagerException("Can not create account " + accountId + " which already exists");
        }
        storage.addEvent(accountId, new CreateAccount(clock.instant(), name, visitations));
    }

    public void extendAccount(int accountId, int visitations) throws NullException, EventValueException {
        Account account = Utils.recreateAccount(accountId, storage);
        storage.addEvent(accountId, new ExtendAccount(clock.instant(), visitations));
    }
}
