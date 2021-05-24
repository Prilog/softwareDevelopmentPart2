import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static java.lang.Thread.sleep;

public class ActorTest {
    private ActorSystem system;
    private ActorRef master;

    @BeforeEach
    void init() {
        system = ActorSystem.create("ActorSystem");
    }

    @AfterEach
    void terminate() {
        system.terminate();
        Result.result.clear();
    }

    @Test
    void simpleTest() {
        Set<SearchingEngine> engines = Set.of(
                new SearchingEngine("Google", new StubServer(0)),
                new SearchingEngine("Bing", new StubServer(0)),
                new SearchingEngine("Yandex", new StubServer(0)));
        master = system.actorOf(Props.create(MasterActor.class, engines, (long) 1), "Master");
        master.tell("kumo desu ga nani ka", ActorRef.noSender());
        try {
            sleep(4000);
            Assertions.assertEquals(3, Result.result.keySet().size());
        } catch (InterruptedException e) {
            Assertions.fail();
        }
    }

    @Test
    void partialTimeout() {
        Set<SearchingEngine> engines = Set.of(
                new SearchingEngine("Google", new StubServer(0)),
                new SearchingEngine("Bing", new StubServer(1000)),
                new SearchingEngine("Yandex", new StubServer(6000)));
        master = system.actorOf(Props.create(MasterActor.class, engines, (long) 1), "Master");

        master.tell("kumo desu ga nani ka", ActorRef.noSender());
        try {
            sleep(4000);
            Assertions.assertEquals(3, Result.result.keySet().size());
            Assertions.assertEquals(1, Result.result.values().stream().
                    map((response)->response.getResponse().get("result")).
                    filter((str)->str.toString().contains("Timeout")).count());
        } catch (InterruptedException e) {
            Assertions.fail();
        }
    }

    @Test
    void fullTimeout() {
        Set<SearchingEngine> engines = Set.of(
                new SearchingEngine("Google", new StubServer(6000)),
                new SearchingEngine("Bing", new StubServer(6000)),
                new SearchingEngine("Yandex", new StubServer(6000)));
        master = system.actorOf(Props.create(MasterActor.class, engines, (long) 1), "Master");
        master.tell("kumo desu ga nani ka", ActorRef.noSender());
        try {
            sleep(4000);
            Assertions.assertEquals(3, Result.result.keySet().size());
            Assertions.assertEquals(3, Result.result.values().stream().
                    map((response)->response.getResponse().get("result")).
                    filter((str)->str.toString().contains("Timeout")).count());
        } catch (InterruptedException e) {
            Assertions.fail();
        }
    }
}
