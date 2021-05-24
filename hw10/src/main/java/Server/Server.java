package Server;

import Application.Item;
import Application.LocalCurrency;
import Application.ReactiveApplication;
import Application.User;
import io.reactivex.netty.protocol.http.server.HttpServer;
import rx.Observable;

import java.util.Currency;
import java.util.List;
import java.util.Map;

public class Server {
    public static void main(String[] args) {
        try {
            HttpServer.newServer(8080).start((req, resp) -> {
                ReactiveApplication.init();
                String name = req.getDecodedPath().substring(1);
                Map<String, List<String>> parameters = req.getQueryParameters();
                Observable<String> response;
                System.out.println(name + req.toString());
                switch (name) {
                    case "register":
                        response = ReactiveApplication
                                .getUsers()
                                .exists(user -> parameters.get("id").get(0).equals("" + user.getId()))
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Observable.just("Error: can not register user with this ID\n");
                                    } else {
                                        String currency = parameters.get("currency").get(0);
                                        if (!LocalCurrency.isLegal(currency)) {
                                            return Observable.just("Sorry, currency " + currency + " is invalid\n");
                                        }
                                        User user = new User(Integer.parseInt(parameters.get("id").get(0)),
                                                Currency.getInstance(currency), parameters.get("name").get(0));
                                        return ReactiveApplication.addUser(user)
                                                .map(success -> user.toString() + " is registered\n");
                                    }
                                });
                        break;
                    case "add":
                        response = ReactiveApplication
                                .getItems()
                                .exists(item -> parameters.get("id").get(0).equals("" + item.getId()))
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Observable.just("Error: can not add the item because it already exists\n");
                                    } else {
                                        String currency = parameters.get("currency").get(0);
                                        if (!LocalCurrency.isLegal(currency)) {
                                            return Observable.just("Sorry, currency " + currency + " is invalid\n");
                                        }
                                        Item item = new Item(Integer.parseInt(parameters.get("id").get(0)),
                                                parameters.get("name").get(0),
                                                Double.parseDouble(parameters.get("price").get(0)));
                                        return ReactiveApplication.addItem(item)
                                                .map(success -> item.toString() + " is added\n");
                                    }
                                });
                        break;
                    case "show-users":
                        response = ReactiveApplication
                                .getUsers()
                                .map(User::toString);
                        break;
                    case "show-items":
                        response = ReactiveApplication
                                .getItems()
                                .map(item -> item.toLocalizedString(Currency.getInstance(ReactiveApplication.getUsers()
                                        .filter(user -> parameters.get("id").get(0).equals("" + user.getId()))
                                        .firstOrDefault(new User(0, Currency.getInstance("RUB"), "default"))
                                        .map(User::getCurrency).toString())));
                        break;
                    default:
                        response = null;
                }
                return resp.writeString(response);
            })
                    .awaitShutdown();
        } finally {
            ReactiveApplication.close();
        }
    }
}
