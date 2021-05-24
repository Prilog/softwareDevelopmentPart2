import app.UserApplication;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
public class ExchangeTest {
    @ClassRule
    public static GenericContainer simpleWebServer
            = new FixedHostPortGenericContainer("exchange:1.0-SNAPSHOT")
            .withFixedExposedPort(8080, 8080)
            .withExposedPorts(8080);

    private UserApplication users;

    private HttpResponse<String> getResponse(HttpRequest request) throws IOException, InterruptedException {
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    @BeforeEach
    public void init() {
        users = new UserApplication();
    }

    @Test
    public void buyingTest() throws Exception {
        Assert.assertEquals("DONE", users.addUser("Kiryu"));
        Assert.assertEquals("DONE", users.addMoney("Kiryu", 100000.));
        double balance = 100000;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/add-company?company=gazprom"))
                .GET()
                .build();
        HttpResponse<String> response = getResponse(request);
        Assert.assertEquals("DONE", response.body());
        request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/add-shares?company=gazprom&name=gaz&count=50&initPrice=100"))
                .GET()
                .build();
        response = getResponse(request);
        Assert.assertEquals("DONE", response.body());
        request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/update"))
                .GET()
                .build();
        response = getResponse(request);
        Assert.assertEquals("DONE", response.body());
        request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/get-global-info"))
                .GET()
                .build();
        response = getResponse(request);
        request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/buy-shares?company=gazprom&name=gaz&count=2"))
                .GET()
                .build();
        response = getResponse(request);
        balance -= Double.parseDouble(response.body());
        double price = Double.parseDouble(response.body()) / 2;
        users.buyShares("Kiryu", "gazprom", "gaz", 2, price);
        Assert.assertEquals(100000., Double.parseDouble(users.getBalance("Kiryu")));
        request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/update"))
                .GET()
                .build();
        response = getResponse(request);
        Assert.assertEquals("DONE", response.body());
        request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/sell-shares?company=gazprom&name=gaz&count=2"))
                .GET()
                .build();
        response = getResponse(request);
        balance += Double.parseDouble(response.body());
        price = Double.parseDouble(response.body()) / 2;
        users.sellShares("Kiryu", "gazprom", "gaz", 2, price);
        Assert.assertEquals(balance, Double.parseDouble(users.getBalance("Kiryu")));
    }
}
