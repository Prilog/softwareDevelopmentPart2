package Application;

import org.bson.Document;

import java.util.Currency;

public class User {
    private final int id;
    private final Currency currency;
    private final String name;

    public User(int i, Currency c, String n) {
        id = i;
        currency = c;
        name = n;
    }

    public User(Document document) {
        this(document.getInteger("id"), Currency.getInstance(document.getString("currency")), document.getString("name"));
    }

    public Document toDocument() {
        return new Document("id", id).append("name", name).append("currency", currency.toString());
    }

    public int getId() {
        return id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "[USER " + id + "] " + name + "(" + currency.getSymbol() + ")";
    }
}
