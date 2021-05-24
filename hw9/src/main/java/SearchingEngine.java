public class SearchingEngine {
    public String name;
    public StubServer server;

    public SearchingEngine(String n, StubServer s) {
        name = n;
        server = s;
    }

    public String toString() {
        return name;
    }
}
