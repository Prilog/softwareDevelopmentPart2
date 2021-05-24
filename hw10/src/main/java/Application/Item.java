package Application;

import org.bson.Document;

import java.util.Currency;

public class Item {
    private final int id;
    private final String name;
    private final LocalCurrency ruPrice;

    public Item(int i, String n, double rp) {
        id = i;
        name = n;
        ruPrice = new LocalCurrency(Currency.getInstance("RUB"), rp);
    }

    public Item(Document document) {
        this(document.getInteger("id"), document.getString("name"), document.getDouble("ruPrice"));
    }

    public Document toDocument() {
        return new Document("id", id).append("name", name).append("ruPrice", ruPrice.getAmount());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalCurrency getRuPrice() {
        return ruPrice;
    }

    @Override
    public String toString() {
        return "[ITEM " + id + "] " + name + " " + ruPrice;
    }

    public String toLocalizedString(Currency newCurrency) {
        return "[ITEM " + id + "] " + name + " " + ruPrice.convertTo(newCurrency);
    }
}
