package sajid.bussinesssale.Database;

/**
 * Created by aazib on 09-Jun-17.
 */

public class DB_Config {
    public static int DEFAULT_VERSION = 0;

    public static final String DATABASE_NAME = "BI_DB.db";

    public static class SALES {
        public static final String TABLE = "sales";
        public static final String ID = "id";
        public static final String COMPANY = "Company";
        public static final String PRODUCT = "Product";
        public static final String REGION = "Region";
        public static final String AMOUNT = "Amount";
        public static final String SALE_DATE = "Sale_Date";
    }

    public static class LOOKUP {
        public static final String TABLE = "lookup";
        public static final String ID = "id";
        public static final String VERSION = "db_version";
    }
}
