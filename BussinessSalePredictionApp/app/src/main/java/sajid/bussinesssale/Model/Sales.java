package sajid.bussinesssale.Model;

/**
 * Created by aazib on 03-Jun-17.
 */

public class Sales {
    String Company;
    String Product;
    String Region;
    String year;
    int color;
    float[] salesPerMonth;

    public Sales() {
        Company = "";
        Product = "";
        Region = "";
        salesPerMonth = new float[12];  // 12 Months
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getCompany() {
        return Company;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public String getProduct() {
        return Product;
    }

    public void setRegion(String region) {
        Region = region;
    }

    public String getRegion() {
        return Region;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYear() {
        return year;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public void setSalesPerMonth(float[] salesPerMonth) {
        this.salesPerMonth = salesPerMonth;
    }

    public float[] getSalesPerMonth() {
        return salesPerMonth;
    }
}
