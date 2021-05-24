package cores;

import exceptions.EventValueException;
import exceptions.NullException;
import storage.Enter;
import storage.Event;
import storage.Leave;
import utils.Utils;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class ReportService {
    private final Map<Integer, Map<Integer, List<Integer>>> dailyStatistics = new HashMap<>();
    private final Map<Integer, Instant> entryTime = new HashMap<>();
    Clock clock;

    public void receiveEvent(int accountId, Event event) throws NullException {
        if (event instanceof Enter) {
            entryTime.put(accountId, (Instant) Utils.throwIfNull(event.get("TIME")));
        } else if (event instanceof Leave) {
            Instant exitTime = (Instant) Utils.throwIfNull(event.get("TIME"));
            Integer day = Utils.calculateDay(exitTime);
            dailyStatistics.computeIfAbsent(day, n -> new HashMap<>());
            dailyStatistics.get(day).computeIfAbsent(accountId, n -> new ArrayList<>());
            dailyStatistics.get(day).get(accountId).add((int) Duration.between(entryTime.get(accountId), exitTime).toMinutes());
        }
    }

    public int getAverageVisitationTime() {
        int total = 0;
        int cnt = 0;
        for (Map.Entry<Integer, Map<Integer, List<Integer>>> day : dailyStatistics.entrySet()) {
            for (Map.Entry<Integer, List<Integer>> user : day.getValue().entrySet()) {
                for (Integer duration : user.getValue()) {
                    total += duration;
                    cnt += 1;
                }
            }
        }
        return total / cnt;
    }

    public int getAverageVisitationsPerDay() {
        int total = 0;
        int cnt = 0;
        for (Map.Entry<Integer, Map<Integer, List<Integer>>> day : dailyStatistics.entrySet()) {
            for (Map.Entry<Integer, List<Integer>> user : day.getValue().entrySet()) {
                total += user.getValue().size();
            }
            cnt += 1;
        }
        return total / cnt;
    }

    public Map<Integer, Map<Integer, List<Integer>>> getDailyStatistics() {
        return dailyStatistics;
    }
}
