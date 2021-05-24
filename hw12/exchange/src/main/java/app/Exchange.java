package app;

import engine.ExchangeEngine;
import engine.ExchangeException;
import engine.Share;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import engine.ExchangeEngineMem;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
public class Exchange {
    private ExchangeEngine exchangeEngine = new ExchangeEngineMem();

    @RequestMapping("/add-company")
    public String addCompany(String company) throws ExchangeException {
        if (exchangeEngine.addCompany(company)) {
            return "DONE";
        }
        throw new ExchangeException("Can not add company " + company);
    }

    @RequestMapping("/add-shares")
    public String addShares(String company, String name , int count, double initPrice) throws ExchangeException {
        if (exchangeEngine.addShares(company, name, count, initPrice)) {
            return "DONE";
        }
        throw new ExchangeException("Can not add shares " + name + " to " + company);
    }

    @RequestMapping("/buy-shares")
    public String buyShares(String company, String name, int count) throws ExchangeException {
        double result = exchangeEngine.buyShares(company, name, count);
        if (result > 0.0) {
            return "" + result;
        }
        throw new ExchangeException("Unable to buy shares");
    }

    @RequestMapping("/sell-shares")
    public String sellShares(String company, String name, int count) throws ExchangeException {
        double result = exchangeEngine.sellShares(company, name, count);
        if (result > 0.0) {
            return "" + result;
        }
        throw new ExchangeException("Unable to sell shares");
    }

    @RequestMapping("/get-company-info")
    public String getCompanyInfo(String company) {
        StringBuilder result = new StringBuilder();
        for (Share share : exchangeEngine.getCompanyInfo(company)) {
            result.append(share.toString()).append("\n");
        }
        return result.toString();
    }

    @RequestMapping("/get-global-info")
    public String getGlobalInfo() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, List<Share>> entry : exchangeEngine.getGlobalInfo().entrySet()) {
            for (Share share : entry.getValue()) {
                result.append(share.toString()).append("\n");
            }
        }
        return result.toString();
    }

    @RequestMapping("/update")
    public String update() throws ExchangeException {
        for (Map.Entry<String, List<Share>> entry : exchangeEngine.getGlobalInfo().entrySet()) {
            for (Share share : entry.getValue()) {
                double fluctuation = Math.random() * 6. * Math.signum(Math.random() - 0.5);
                if (!exchangeEngine.changePrice(entry.getKey(), share.getName(), share.getPrice() + fluctuation)) {
                    throw new ExchangeException("Unable to update prices");
                }
            }
        }
        return "DONE";
    }
}
