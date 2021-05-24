public class Request {
    private SearchingEngine searchingEngine;
    private String request;

    public Request(SearchingEngine engine, String s) {
        searchingEngine = engine;
        request = s;
    }

    public SearchingEngine getSearchingEngine() {
        return searchingEngine;
    }

    public String getRequest() {
        return request;
    }
}
