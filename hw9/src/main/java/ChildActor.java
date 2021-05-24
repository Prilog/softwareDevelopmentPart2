import akka.actor.UntypedAbstractActor;

public class ChildActor extends UntypedAbstractActor {
    @Override
    public void onReceive(Object message) {
        if (message instanceof Request) {
            Request request = (Request) message;
            Response response = new Response(request.getSearchingEngine(), request.getSearchingEngine().server.api(request));
            getSender().tell(response, getSelf());
        }
    }
}
