import cores.ReportService;
import cores.Turnstile;
import exceptions.*;
import cores.ManagerAdmin;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import cores.Account;
import storage.EventStorage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.Assert.*;

public class FitnessTest {

    @BeforeEach
    void init() {

    }

    @Test
    public void turnstileBaseTest() throws ManagerException, NullException, TurnstileException, EventValueException {
        TestClock clock = new TestClock();
        EventStorage eventStorage = new EventStorage();
        ManagerAdmin managerAdmin = new ManagerAdmin(eventStorage, clock);
        Turnstile turnstile = new Turnstile(eventStorage, clock);
        managerAdmin.createAccount(1, "Jotaro", 3);
        turnstile.checkIn(1);
        clock.add(1);
        turnstile.checkOut(1);
        clock.add(1);
        turnstile.checkIn(1);
        clock.add(1);
        turnstile.checkOut(1);
        clock.add(1);
        turnstile.checkIn(1);
        clock.add(1);
        turnstile.checkOut(1);
    }

    @Test
    public void turnstileFailTest() throws ManagerException, NullException, TurnstileException, EventValueException {
        TestClock clock = new TestClock();
        EventStorage eventStorage = new EventStorage();
        ManagerAdmin managerAdmin = new ManagerAdmin(eventStorage, clock);
        Turnstile turnstile = new Turnstile(eventStorage, clock);
        managerAdmin.createAccount(1, "Jotaro", 2);
        turnstile.checkIn(1);
        clock.add(1);
        turnstile.checkOut(1);
        clock.add(1);
        turnstile.checkIn(1);
        clock.add(1);
        turnstile.checkOut(1);
        clock.add(1);
        try {
            turnstile.checkIn(1);
            fail("Exception was not thrown");
        } catch (TurnstileException ignored) {}
    }

    @Test
    public void turnstileManyPeopleTest() throws ManagerException, NullException, TurnstileException, EventValueException {
        TestClock clock = new TestClock();
        EventStorage eventStorage = new EventStorage();
        ManagerAdmin managerAdmin = new ManagerAdmin(eventStorage, clock);
        Turnstile turnstile = new Turnstile(eventStorage, clock);
        managerAdmin.createAccount(1, "Jotaro", 2);
        managerAdmin.createAccount(2, "Jonatan", 1);

        turnstile.checkIn(1);
        clock.add(1);
        turnstile.checkOut(1);
        clock.add(1);
        turnstile.checkIn(1);
        clock.add(1);
        turnstile.checkOut(1);
        clock.add(1);
        try {
            turnstile.checkIn(1);
            fail("Exception was not thrown");
        } catch (TurnstileException ignored) {}

        turnstile.checkIn(2);
        clock.add(1);
        turnstile.checkOut(2);
        clock.add(1);
        try {
            turnstile.checkIn(2);
            fail("Exception was not thrown");
        } catch (TurnstileException ignored) {}
    }

    @Test
    public void managerExtendTest() throws ManagerException, NullException, TurnstileException, EventValueException {
        TestClock clock = new TestClock();
        EventStorage eventStorage = new EventStorage();
        ManagerAdmin managerAdmin = new ManagerAdmin(eventStorage, clock);
        Turnstile turnstile = new Turnstile(eventStorage, clock);
        managerAdmin.createAccount(1, "Jotaro", 1);
        turnstile.checkIn(1);
        clock.add(1);
        turnstile.checkOut(1);
        clock.add(1);
        try {
            turnstile.checkIn(1);
            fail("Exception was not thrown");
        } catch (TurnstileException ignored) {}
        managerAdmin.extendAccount(1, 1);
        turnstile.checkIn(1);
        clock.add(1);
        turnstile.checkOut(1);
        clock.add(1);
        try {
            turnstile.checkIn(1);
            fail("Exception was not thrown");
        } catch (TurnstileException ignored) {}
    }

    @Test
    public void managerAccessTest() throws ManagerException, NullException, TurnstileException, EventValueException {
        TestClock clock = new TestClock();
        EventStorage eventStorage = new EventStorage();
        ManagerAdmin managerAdmin = new ManagerAdmin(eventStorage, clock);
        Turnstile turnstile = new Turnstile(eventStorage, clock);
        managerAdmin.createAccount(1, "Jotaro", 5);
        turnstile.checkIn(1);
        clock.add(1);
        turnstile.checkOut(1);
        clock.add(1);
        turnstile.checkIn(1);
        clock.add(1);
        turnstile.checkOut(1);
        clock.add(1);
        turnstile.checkIn(1);
        clock.add(1);
        turnstile.checkOut(1);
        Account account = managerAdmin.getAccount(1);
        assertEquals(1, account.getId());
        assertEquals("Jotaro", account.getName());
        assertEquals(2, account.getVisitations());
        assertEquals(clock.instant(), account.getLastVisit());
        assertFalse(account.getInside());
        clock.add(1);
        turnstile.checkIn(1);
        account = managerAdmin.getAccount(1);
        assertTrue(account.getInside());
    }

    @Test
    public void reportBaseTest() throws ManagerException, NullException, TurnstileException, EventValueException {
        LocalDate localDate = LocalDate.parse("2018-06-23");
        LocalDateTime startOfDay = localDate.atStartOfDay();
        TestClock clock = new TestClock();
        clock.setTime(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
        EventStorage eventStorage = new EventStorage();
        ManagerAdmin managerAdmin = new ManagerAdmin(eventStorage, clock);
        Turnstile turnstile = new Turnstile(eventStorage, clock);
        managerAdmin.createAccount(1, "Jotaro", 3);
        turnstile.checkIn(1);
        clock.add(1);
        turnstile.checkOut(1);
        clock.add(1);
        turnstile.checkIn(1);
        clock.add(1);
        turnstile.checkOut(1);
        clock.add(1);
        turnstile.checkIn(1);
        clock.add(1);
        turnstile.checkOut(1);
        ReportService reportService = new ReportService();
        eventStorage.assignReportService(reportService);
        assertEquals(60, reportService.getAverageVisitationTime());
        assertEquals(3, reportService.getAverageVisitationsPerDay());
    }
}
