package cores;

import java.time.Instant;

public class Account {
    private int id;
    private String name;
    private int visitations;
    private boolean isInside;
    private Instant created;
    private Instant lastVisit;


    public Account(int id) {
        this.id = id;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant c) {
        created = c;
    }

    public int getId() {
        return id;
    }

    public void setId(int i) {
        id = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public Instant getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Instant l) {
        lastVisit = l;
    }

    public boolean getInside() { return isInside; }

    public void setInside(boolean i) { isInside = i; }

    public int getVisitations() { return visitations; }

    public void setVisitations(int v) { visitations = v; }
}
