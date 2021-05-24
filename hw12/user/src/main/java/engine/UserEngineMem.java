package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserEngineMem implements UserEngine {
    private Map<String, User> users = new HashMap<>();

    @Override
    public boolean addUser(User user) {
        if (users.containsKey(user.getName())) return false;
        users.put(user.getName(), user);
        return true;
    }

    @Override
    public boolean changeBalance(String name, double money) {
        if (!users.containsKey(name)) return false;
        User user = users.get(name);
        user.setBalance(user.getBalance() + money);
        return true;
    }

    @Override
    public boolean buyShares(String name, String company, UserShare userShare) {
        if (!users.containsKey(name)) return false;
        User user = users.get(name);
        user.buyShares(userShare);
        return true;
    }

    @Override
    public boolean sellShares(String name, String company, UserShare userShare) {
        if (!users.containsKey(name)) return false;
        User user = users.get(name);
        user.sellShares(userShare);
        return true;
    }

    @Override
    public double getBalance(String name) {
        if (!users.containsKey(name)) throw new IllegalStateException("User does not exists");
        User user = users.get(name);
        double balance = user.getBalance();
        for (Map.Entry<String, Map<String, UserShare>> company : user.getShares().entrySet()) {
            for (Map.Entry<String, UserShare> shareName : company.getValue().entrySet()) {
                balance += shareName.getValue().getCount() * shareName.getValue().getPrice();
            }
        }
        return balance;
    }

    @Override
    public List<UserShare> getShares(String name) {
        if (!users.containsKey(name)) throw new IllegalStateException("User does not exists");
        User user = users.get(name);
        List<UserShare> list = new ArrayList<>();
        for (Map.Entry<String, Map<String, UserShare>> company : user.getShares().entrySet()) {
            for (Map.Entry<String, UserShare> share : company.getValue().entrySet()) {
                list.add(share.getValue());
            }
        }
        return list;
    }
}
