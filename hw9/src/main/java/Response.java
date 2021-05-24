import org.json.JSONObject;

public class Response {
    private SearchingEngine searchingEngine;
    private JSONObject response;

    public Response(SearchingEngine engine, JSONObject json) {
        searchingEngine = engine;
        response = json;
    }

    public SearchingEngine getSearchingEngine() {
        return searchingEngine;
    }

    public JSONObject getResponse() {
        return response;
    }
}
