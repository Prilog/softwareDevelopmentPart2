package Application;

import com.mongodb.rx.client.*;
import org.bson.Document;
import rx.Observable;

public class ReactiveApplication {
    private static MongoClient client;
    private static MongoDatabase database;
    private static MongoCollection<Document> users;
    private static MongoCollection<Document> items;

    public static void init() {
        client = MongoClients.create("mongodb://localhost:27017");
        database = client.getDatabase("app");
        users = database.getCollection("users");
        items = database.getCollection("items");
    }

    public static Observable<Success> addUser(User user) {
        return users.insertOne(user.toDocument());
    }

    public static Observable<Success> addItem(Item item) {
        return items.insertOne(item.toDocument());
    }

    public static Observable<User> getUsers() {
        return users.find().toObservable().map(User::new);
    }

    public static Observable<Item> getItems() {
        return items.find().toObservable().map(Item::new);
    }

    public static void close() {
        client.close();
    }
}