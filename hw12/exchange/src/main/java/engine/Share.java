package engine;

public class Share {
    private String company;
    private String name;
    private int count;
    private double price;

    public Share(String nm, String cmp, int cnt, double prc) {
        name = nm;
        company = cmp;
        count = cnt;
        price = prc;
    }

    public String getName() { return name; }

    public void setName(String nm) { name = nm; }

    public int getCount() {
        return count;
    }

    public void setCount(int cnt) {
        count = cnt;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double prc) {
        price = prc;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String cmp) {
        company = cmp;
    }

    @Override
    public String toString() {
        return "[" + company + "]" + name + ":" + count + " shares by " + price;
    }
}
