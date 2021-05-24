import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.util.Set;

import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ActorSystem system;
        ActorRef master;
        system = ActorSystem.create("ActorSystem");
        Set<SearchingEngine> engines = Set.of(
                new SearchingEngine("Google", new StubServer(0)),
                new SearchingEngine("Bing", new StubServer(1000)),
                new SearchingEngine("Yandex", new StubServer(6000)));
        master = system.actorOf(Props.create(MasterActor.class, engines, (long) 1), "Master");

        master.tell("main", ActorRef.noSender());
        sleep(4000);
        System.out.println(Result.result.keySet().size());
        for (SearchingEngine i : Result.result.keySet()) {
            System.out.println(i + " -> " + Result.result.get(i).getResponse().get("result"));
        }

        system.terminate();
    }
}
