package storage;

import exceptions.EventValueException;
import cores.ReportService;
import exceptions.NullException;

import java.util.*;

public class EventStorage {
    private final Map<Integer, List<Event>> events = new HashMap<>();
    private ReportService reportService = null;

    public void assignReportService(ReportService rs) throws NullException {
        reportService = rs;
        for (Map.Entry<Integer, List<Event>> account : events.entrySet()) {
            for (Event event : account.getValue()) {
                reportService.receiveEvent(account.getKey(), event);
            }
        }
    }

    public boolean isExist(int id) {
        return events.containsKey(id);
    }

    public void addEvent(int accountId, Event event) throws NullException {
        events.computeIfAbsent(accountId, n -> new ArrayList<Event>());
        events.get(accountId).add(event);
        if (reportService != null) {
            reportService.receiveEvent(accountId, event);
        }
    }

    public List<Event> getEvents(int id) {
        return events.get(id);
    }
}
