package sajid.bussinesssale.PrimarySharedData;

/**
 * Created by aazib on 07-Jun-17.
 */

// It contains all the data from Server that has to be shown in UI
public class AppData {

    public String NO_ARGUMENT = "No Argument";

    String[] companiesList;
    String[] productsList_1;
    String[] productsList_2;
    String[] regionsList;
    String[] yearsList;

    public AppData(){  }

    public AppData(String[] companiesList, String[] productsList, String[] regionsList, String[] yearsList) {
        this.companiesList = companiesList;
        this.productsList_1 = productsList;
        this.productsList_2 = productsList;
        this.regionsList = regionsList;
        this.yearsList = yearsList;
    }

    public void setCompaniesList(String[] companiesList) {
        this.companiesList = companiesList;
    }

    public String[] getCompaniesList() {
        return companiesList;
    }

    public void setProductsList_1(String[] productsList_1) {
        this.productsList_1 = productsList_1;
    }

    public String[] getProductsList_1() {
        return productsList_1;
    }

    public void setProductsList_2(String[] productsList_2) {
        this.productsList_2 = productsList_2;
    }

    public String[] getProductsList_2() {
        return productsList_2;
    }

    public void setRegionsList(String[] regionsList) {
        this.regionsList = regionsList;
    }

    public String[] getRegionsList() {
        return regionsList;
    }

    public void setYearsList(String[] yearsList) {
        this.yearsList = yearsList;
    }

    public String[] getYearsList() {
        return yearsList;
    }
}
