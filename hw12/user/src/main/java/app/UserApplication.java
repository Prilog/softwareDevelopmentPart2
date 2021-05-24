package app;

import engine.UserShare;
import engine.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import engine.UserEngine;
import engine.UserEngineMem;

import java.util.stream.Stream;

@RestController
public class UserApplication {
    private UserEngine userEngine = new UserEngineMem();

    @RequestMapping("/add-user")
    public String addUser(String name) {
        if (userEngine.addUser(new User(name))) return "User " + name + " added";
        return "Can not add user " + name;
    }

    @RequestMapping("/add-money")
    public String addMoney(String name, double money) {
        if (userEngine.changeBalance(name, money)) return "Added funds to " + name;
        return "Can not add money to " + name;
    }

    @RequestMapping("/buy-shares")
    public String buyShares(String userName, String company, String shareName, int count, double price) {
        if (userEngine.buyShares(userName, company, new UserShare(shareName, company, count, price))
                && userEngine.changeBalance(userName, -price * count)) return "" + count * price;
        return "Can not buy shares " + shareName + " to user " + userName;
    }

    @RequestMapping("/sell-shares")
    public String sellShares(String userName, String company, String shareName, int count, double price) {
        if (userEngine.sellShares(userName, company, new UserShare(shareName, company, count, price))
                && userEngine.changeBalance(userName, price * count)) return "" + count * price;
        return "Can not sell shares " + shareName + " from user " + userName;
    }

    @RequestMapping("/get-balance")
    public String getBalance(String userName) {
        return "" + userEngine.getBalance(userName);
    }

    @RequestMapping("/get-shares")
    public Stream<String> getShares(String userName) {
        return userEngine.getShares(userName).stream().map(UserShare::toString);
    }
}
