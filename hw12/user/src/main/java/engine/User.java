package engine;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String name;
    private double balance;
    private Map<String, Map<String, UserShare>> shares = new HashMap<>();

    public User(String n) {
        name = n;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double b) {
        balance = b;
    }

    public Map<String, Map<String, UserShare>> getShares() {
        return shares;
    }

    public void buyShares(UserShare share) {
        UserShare share1 = shares.get(share.getCompany()).get(share.getName());
        if (share1 == null) {
            shares.computeIfAbsent(share.getCompany(), n -> new HashMap<>());
            shares.get(share.getCompany()).computeIfAbsent(share.getName(), n -> share);
        } else {
            share1.setCount(share1.getCount() + share.getCount());
            share1.setPrice(share.getPrice());
        }
    }

    public void sellShares(UserShare share) {
        UserShare share1 = shares.get(share.getCompany()).get(share.getName());
        if (share1 == null || share1.getCount() < share.getCount())
            throw new IllegalStateException("Can not sell " + share.toString());
        else {
            share1.setCount(share1.getCount() - share.getCount());
            share1.setPrice(share.getPrice());
        }
    }
}
