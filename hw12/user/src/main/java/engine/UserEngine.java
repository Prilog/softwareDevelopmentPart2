package engine;

import java.util.List;

public interface UserEngine {
    boolean addUser(User user);

    boolean changeBalance(String name, double money);

    boolean buyShares(String name, String company, UserShare userShare);

    boolean sellShares(String name, String company, UserShare userShare);

    double getBalance(String name);

    List<UserShare> getShares(String name);
}
