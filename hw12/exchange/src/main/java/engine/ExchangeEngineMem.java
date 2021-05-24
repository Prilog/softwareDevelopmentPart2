package engine;

import java.util.*;

public class ExchangeEngineMem implements ExchangeEngine {
    private Map<String, Map<String, Share>> shares = new HashMap<>();

    @Override
    public boolean addCompany(String company) {
        if (shares.containsKey(company)) return false;
        shares.put(company, new HashMap<>());
        return true;
    }

    @Override
    public boolean addShares(String company, String name, int count, double initPrice) throws ExchangeException {
        if (!shares.containsKey(company)) {
            return false;
        }
        if (shares.get(company).containsKey(name)) {
            return false;
        }
        shares.get(company).put(name, new Share(name, company, count, initPrice));
        return true;
    }

    @Override
    public double buyShares(String company, String name, int count) {
        if (!shares.containsKey(company)) {
            return 0;
        }
        if (!shares.get(company).containsKey(name)) {
            return 0;
        }
        Share share = shares.get(company).get(name);
        if (share.getCount() < count) {
            return 0;
        }
        double price = share.getPrice();
        share.setCount(share.getCount() - count);
        return price * count;
    }

    @Override
    public double sellShares(String company, String name, int count) {
        if (!shares.containsKey(company)) {
            return 0;
        }
        if (!shares.get(company).containsKey(name)) {
            return 0;
        }
        Share share = shares.get(company).get(name);
        double price = share.getPrice();
        share.setCount(share.getCount() + count);
        return price * count;
    }

    @Override
    public boolean changePrice(String company, String name, double newPrice) {
        if (!shares.containsKey(company)) {
            return false;
        }
        if (!shares.get(company).containsKey(name)) {
            return false;
        }
        shares.get(company).get(name).setPrice(newPrice);
        return true;
    }

    @Override
    public List<Share> getCompanyInfo(String company) {
        List<Share> list = new ArrayList<>();
        for (Map.Entry<String, Share> share : shares.get(company).entrySet()) {
            list.add(share.getValue());
        }
        return list;
    }

    @Override
    public Map<String, List<Share>> getGlobalInfo() {
        Map<String, List<Share>> map = new HashMap<>();
        for (Map.Entry<String, Map<String, Share>> company : shares.entrySet()) {
            List<Share> list = new ArrayList<>();
            for (Map.Entry<String, Share> share : shares.get(company.getKey()).entrySet()) {
                list.add(share.getValue());
            }
            map.put(company.getKey(), list);
        }
        return map;
    }
}
