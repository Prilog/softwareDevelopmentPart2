import static java.lang.Thread.sleep;
import org.json.JSONObject;

public class StubServer {
    public long timeout;

    public StubServer(long time) {
        timeout = time;
    }

    public JSONObject api(Request request) {
        try {
            sleep(timeout);
            return new JSONObject("{ \"result\": Found " + request.getRequest() + " in " + request.getSearchingEngine() + "}");
        } catch (InterruptedException e) {
            return new JSONObject("{\"result\": Request interrupted}");
        }
    }
}
