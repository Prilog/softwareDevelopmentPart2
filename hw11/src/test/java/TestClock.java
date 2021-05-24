import java.time.*;

public class TestClock extends Clock {
    private Instant now;

    public TestClock() {
        super();
        now = Instant.now();
    }

    @Override
    public ZoneId getZone() {
        return ZoneId.systemDefault();
    }

    @Override
    public Clock withZone(ZoneId zone) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Instant instant() {
        return now;
    }

    public void add(long hours) {
        now = now.plusSeconds(hours * 3600L);
    }

    public void setTime(Instant time) { now = time; }
}
