package engine;

import java.util.List;
import java.util.Map;

public interface ExchangeEngine {
    boolean addCompany(String company);

    boolean addShares(String company, String name, int count, double initPrice) throws ExchangeException;

    double buyShares(String company, String name, int count);

    double sellShares(String company, String name, int count);

    boolean changePrice(String company, String name, double newPrice);

    List<Share> getCompanyInfo(String company);

    Map<String, List<Share>> getGlobalInfo();
}
