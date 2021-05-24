package engine;

public class UserShare {
    private String company;
    private String name;
    private int count;
    private double price;

    public UserShare(String n, String cmp, int cnt, double p) {
        name = n;
        company = cmp;
        count = cnt;
        price = p;
    }

    public String getName() {
        return name;
    }

    public void setName(String n) {
        name = n;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int c) {
        count = c;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String c) {
        company = c;
    }

    @Override
    public String toString() {
        return "[" + company + "]" + name + ":" + count + " shares by " + price;
    }
}
