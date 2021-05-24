import Application.ReactiveApplication;
import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.fail;

public class ServiceTest {

    private String sendRequest(String requestStr) {
        try {
            URL url = new URL("http://localhost:8080/" + requestStr);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            Assertions.assertEquals(connection.getResponseCode(), HttpURLConnection.HTTP_OK);
            StringBuilder responseStr = new StringBuilder();
            try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = input.readLine()) != null) {
                    responseStr.append(line);
                }
            }
            return responseStr.toString();
        } catch (IOException ex) {
            fail("Send request error: " + ex.getMessage());
        }
        return "";
    }

    @Test
    void baseTest() {
        String respStr;

        respStr = sendRequest("register?id=Senku&currency=RUB");
        Assertions.assertTrue(respStr.contains("Senku is registered"));
        respStr = sendRequest("register?id=Koichi&currency=USD");
        Assertions.assertTrue(respStr.contains("Koichi is registered"));
        respStr = sendRequest("register?id=Slava&currency=");
        Assertions.assertTrue(respStr.contains("Slava is registered"));
        ReactiveApplication.getUsers().toList().forEach(list -> Assertions.assertEquals(list.size(), 3));

        respStr = sendRequest("add?id=0&name=oil&price=4745.0");
        Assertions.assertTrue(respStr.contains("successfully"));
        respStr = sendRequest("add?id=1&name=mobile&price=666.666");
        Assertions.assertTrue(respStr.contains("successfully"));
        ReactiveApplication.getItems().toList().forEach(list -> Assertions.assertEquals(list.size(), 2));

        respStr = sendRequest("show-users");
        Assertions.assertTrue(respStr.contains("Senku") && respStr.contains("Koichi") && respStr.contains("Slava"));

    }
}