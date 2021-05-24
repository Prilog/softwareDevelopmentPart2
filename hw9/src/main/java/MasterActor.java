import akka.actor.AbstractActorWithTimers;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import org.json.JSONObject;

import java.time.Duration;
import java.util.*;


public class MasterActor extends AbstractActorWithTimers {
    private long time;
    private Duration receive;
    private Duration timeout;
    private Map<SearchingEngine, Response> responses = new HashMap<>();
    private List<ActorRef> actors = new ArrayList<>();
    private Set<SearchingEngine> engines;
    private String request;

    public MasterActor(Set<SearchingEngine> e, long t) {
        engines = e;
        time = t;
        receive = Duration.ofSeconds(time * 2);
        timeout = Duration.ofSeconds(time);
    }

    private void getRequest(String request) {
        this.request = request;
        for (SearchingEngine e : engines) {
            ActorRef a = getContext().actorOf(Props.create(ChildActor.class), e.toString());
            a.tell(new Request(e, request), getSelf());
            actors.add(a);
        }

        getTimers().startSingleTimer("TIMEOUT", new Timeout(), timeout);
        getContext().setReceiveTimeout(receive);
    }

    private void getResponse(Response response) {
        responses.put(response.getSearchingEngine(), response);
        actors.remove(getSender());
        if (responses.size() == 3) stopSearch();
    }

    private void getTimeout(Timeout timeout) {
        for (SearchingEngine e : engines) {
            if (!responses.containsKey(e)) {
                responses.put(e, new Response(e, new JSONObject("{\"result\": Timeout on " + e.toString() + " }")));
            }
        }
    }

    private void getReceive(ReceiveTimeout timeout) {
        stopSearch();
    }

    private void stopSearch() {
        for (ActorRef searcher : actors) {
            getContext().stop(searcher);
        }
        Result.result.putAll(responses);
        getContext().stop(getSelf());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, this::getRequest)
                .match(Response.class, this::getResponse)
                .match(Timeout.class, this::getTimeout)
                .match(ReceiveTimeout.class, this::getReceive)
                .build();
    }

    protected static class Timeout {}

}
