package sajid.bussinesssale.Server;

/**
 * Created by aazib on 08-Jun-17.
 */

public class ServerConfig {
    public static String HOSTNAME = "192.168.1.104/";               // set your local Machine IP
    public static String BASEURL = "http://" + HOSTNAME + "BI/sales/";
    public static String LIST = "list/";
    public static String CHECKUPDATE = "checkUpdate/";

    public static String LIST_URL = BASEURL + LIST;
    public static String CHECKUPDATES_URL = BASEURL + CHECKUPDATE;

    public static class RESPONSE {
        public static String LATEST_DB_VERSION = "latest_DB_version";
        public static String TOTALSALES = "TotalSales";
        public static String UPDATEAVAILABLE = "UpdateAvailable";
    }
}