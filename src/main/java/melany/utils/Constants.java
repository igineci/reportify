package melany.utils;

import java.time.format.DateTimeFormatter;

/**
 * Utility class containing all the constants used in app.
 * Constants are separated in groups.
 *
 * @author andjela.djekic
 */
public class Constants {

    // Command Line Arguments
    public static final String HOST_OPTION = "h";
    public static final String PORT_OPTION = "p";
    public static final String DATABASE_OPTION = "d";
    public static final String USERNAME_OPTION = "U";
    public static final String PASSWORD_OPTION = "P";
    public static final String REPORTS_OPTION = "r";
    public static final String HOST_OPTION_LONG = "host";
    public static final String PORT_OPTION_LONG = "port";
    public static final String DATABASE_OPTION_LONG = "database";
    public static final String USERNAME_OPTION_LONG = "username";
    public static final String PASSWORD_OPTION_LONG = "password";
    public static final String REPORTS_OPTION_LONG = "reports";
    public static final String HOST_OPTION_DESC = "Host name or IP, this parameter is required.";
    public static final String PORT_OPTION_DESC = "Database port, default value is 5432. Must be integer.";
    public static final String DATABASE_OPTION_DESC = "Database name, this parameter is required.";
    public static final String USERNAME_OPTION_DESC = "Database username, this parameter is required.";
    public static final String PASSWORD_OPTION_DESC = "Database password, this parameter is required.";
    public static final String REPORTS_OPTION_DESC = "Reports that are going to be created and sent to REST API. This parameter is required.";
    public static final String CLI_MISSING_ARGS_HELP = "Missing required options for connecting to Database!";
    public static final String CLI_EXAMPLE_ARGS_HELP = "java -jar reportify.jar -h localhost -p 654789 -U andjela -P pwd123 -d b1";

    // Report JSON Maps
    public static final String R_CUS = "CUSTOMER-REPORT";
    public static final String R_PRO = "PRODUCTS-REPORT";
    public static final String R_SAL = "SALES-REPORT";
    public static final String R_SAL5 = "SALES-REPORT-SENT-5";
    public static final String R_STO = "STOCK-REPORT";
    public static final String SAL_JSON_MAP = "SALES_ITEM";

    // Default Values
    public static final String DEFAULT_STRING = "N/A";
    public static final String DEFAULT_POSTAL_CODE = "00000";
    public static final String DEFAULT_TIN = "0000000000";
    public static final String DEFAULT_CLI_PORT = "5432";

    // REST API Credentials & Endpoints
    public static final String API_USERNAME = "YOUR_USERNAME";
    public static final String API_PASSWORD = "YOUR_PASSWORD";
    public static final String API_URL = "YOUR_API_URL";
    public static final String API_CONTENT_HEADER_NAME = "Content-Type";
    public static final String API_CONTENT_HEADER_VALUE = "application/json";
    public static final String API_AUTH_HEADER_NAME = "Authorization";
    public static final String API_STATUS_CODE = "<status code=\"0\"/>";
    public static final String API_AUTH_BASIC = "Basic ";

    // HTTP statuses
    public static final String HTTP_200_OK = "200";
    public static final String HTTP_500_ERROR = "500";

    // Formatters
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    // Punctuation marks
    public static final String COMMA = ",";
    public static final String DOT = ".";
    public static final String QUESTION_MARK = "?";
    public static final String UNDERSCORE = "_";
    public static final String EQUALS = "=";
    public static final String COLON = ":";
    public static final String SLASH = "/";

    // Error Messages
    public static final String CLI_ERROR = "Parsing Error: ";
    public static final String DATABASE_CONNECTING_ERROR = "Error during connecting to the database. Error: ";
    public static final String DATABASE_FETCHING_ERROR = "Error during fetching from the database. Error: ";
    public static final String DATABASE_DISCONNECTING_ERROR = "Error during disconeccting from the database. Error: ";

    // Database
    public static final String JDBC_URL = "jdbc:postgresql://";
    public static final String DISCONNECTED_MSG = "Disconnected from the database!";

    // SQL QUERIES
    public static final String CUS_QUERY = "SELECT * FROM get_customer_master_data()";
    public static final String PRO_QUERY = "SELECT e.artikal AS artikal      ,\n" +
                                                "e.tip AS tip                ,\n" +
                                                "e.boja AS boja              ,\n" +
                                                "e.model AS model            ,\n" +
                                                "ec.elcode AS ean            ,\n" +
                                                "e.naziv AS product_name_1   ,\n" +
                                                "e.naziv AS product_name_2   ,\n" +
                                                "e.jed AS measure_unit_id    ,\n" +
                                                "e.jed AS measure_unit       ,\n" +
                                                "CASE WHEN e.factive=true     \n" +
                                                "THEN 1 ELSE 0 END AS activity\n" +
                                                "FROM elementi AS e           \n" +
                                                "INNER JOIN elemcodeagr AS ec ON e.elid=ec.elid\n" +
                                                "WHERE ec.codetype=1 AND e.manid=6101";

    public static final String SAL_QUERY = "SELECT r.datdoc AS ISSUE_DATE,\n" +
                                                "COALESCE(r.dattrans, r.datdoc) AS SALE_DATE  ,\n" +
                                                "r.racbr AS DOC_ID                            ,\t\n" +
                                                "CONCAT(r.kupid,r.kupsekid) AS CUSTOMER_ID    ,\n" +
                                                "sr.sasracid AS ITEM                          ,\n" +
                                                "e.artikal AS artikal                         ,\n" +
                                                "e.tip AS tip                                 ,\n" +
                                                "e.boja AS boja                               ,\n" +
                                                "e.model AS model                             ,\n" +
                                                "e.jed AS MEASURE_UNIT_ID                     ,\n" +
                                                "sr.kol AS QUANTITY                            \n" +
                                                "FROM racuni AS r\n" +
                                                "INNER JOIN kupci AS k         ON r.kupid=k.kupid\n" +
                                                "INNER JOIN skladista AS skl   ON r.sklid=skl.sklid\n" +
                                                "INNER JOIN sasrac AS sr       ON r.raclink=sr.raclink\n" +
                                                "INNER JOIN elementi AS e      ON sr.elid=e.elid\n" +
                                                "INNER JOIN defdoctip AS dt    ON dt.dtid=r.doctip\n" +
                                                "WHERE e.manid=6101" +
                                                "AND EXTRACT(MONTH FROM r.datdoc) = 9 " +
                                                "AND EXTRACT(YEAR FROM r.datdoc) = EXTRACT(YEAR FROM CURRENT_DATE) " +
                                                "AND dt.fprodaja AND dt.fvp;";
                                                //"AND r.datdoc >= CURRENT_DATE - INTERVAL '%d days'" +
                                                //"AND dt.fprodaja AND dt.fvp;";

    public static final String SAL_RETURN_QUERY = "SELECT p.ulbr AS DOC_ID   ,\n" +
                                                    "p.datdoc AS ISSUE_DATE  ,\n" +
                                                    "p.datdoc AS SALE_DATE   ,\n" +
                                                    "p.ispid AS CUSTOMER_ID  ,\n" +
                                                    "p.doppod AS COR_DOC_ID  ,\n" +
                                                    "d.datum AS COR_DOC_DATE ,\n" +
                                                    "sr.sasracid AS ITEM     ,\n" +
                                                    "e.artikal               ,\n" +
                                                    "e.boja                  ,\n" +
                                                    "e.tip                   ,\n" +
                                                    "e.model                 ,\n" +
                                                    "e.jed AS MEASURE_UNIT_ID,\n" +
                                                    "p.kol AS QUANTITY       ,\n" +
                                                    "ABS(sr.kol) AS QUANTITY_WAS   \n" +
                                                    "FROM vg_povrat AS p      \n" +
                                                    "INNER JOIN sasrac AS sr ON p.elid=sr.elid\n" +
                                                    "INNER JOIN (SELECT docid,link,datum      \n" +
                                                    "FROM doclink AS dl                       \n" +
                                                    "WHERE dl.origdoctip IN (SELECT dtid      \n" +
                                                    "FROM defDocTip WHERE IDHTable=1 AND grupa=100)) AS d ON sr.raclink=d.link AND p.ulid=d.link\n" +
                                                    "INNER JOIN elementi AS e ON p.elid = e.elid\n" +
                                                    "WHERE e.manid=6101 AND p.datdoc >= CURRENT_DATE - INTERVAL '%d days'\n" +
                                                    "UNION ALL                         \n" +
                                                    "SELECT p.ulbr AS DOC_ID          ,\n" +
                                                    "p.datdoc AS ISSUE_DATE           ,\n" +
                                                    "p.datdoc AS SALE_DATE            ,\n" +
                                                    "p.ispid AS CUSTOMER_ID           ,\n" +
                                                    "CAST(p.ulid AS VARCHAR) AS COR_DOC_ID    ,\n" +
                                                    "p.datdoc AS COR_DOC_DATE,\n" +
                                                    "p.sasulid AS ITEM       ,\n" +
                                                    "e.artikal               ,\n" +
                                                    "e.boja                  ,\n" +
                                                    "e.tip                   ,\n" +
                                                    "e.model                 ,\n" +
                                                    "e.jed AS MEASURE_UNIT_ID,\n" +
                                                    "0 AS QUANTITY           ,\n" +
                                                    "ABS(p.kol) AS QUANTITY_WAS    \n" +
                                                    "FROM vg_povrat AS p      \n" +
                                                    "INNER JOIN elementi AS e ON p.elid=e.elid\n" +
                                                    "WHERE e.manid=6101 AND p.datdoc >= CURRENT_DATE - INTERVAL '%d days'";

    public static final String STO_QUERY = "SELECT e.artikal AS artikal                ,\n" +
                                                "e.tip AS tip                          ,\n" +
                                                "e.boja AS boja                        ,\n" +
                                                "e.model AS model                      ,\n" +
                                                "l.inkol-COALESCE(l.outkol, 0) AS stock,\n" +
                                                "e.jed AS measure_unit_id               \n" +
                                                "FROM lagerbrx(3, CURRENT_DATE) AS l    \n" +
                                                "INNER JOIN elementi AS e ON l.elid=e.elid\n" +
                                                "WHERE e.manid=6101";

    // Common Report Fields
    public static final String BRANCH_ID = "branch_id";
    public static final String MEASURE_UNIT_ID = "measure_unit_id";
    public static final String PRODUCER_ID = "producer_id";
    public static final String PRODUCT_ID = "product_id";
    public static final String CUSTOMER_ID = "customer_id";
    public static final String ACTIVITY = "activity";
    public static final String ARTIKAL = "artikal";
    public static final String TIP = "tip";
    public static final String BOJA = "boja";
    public static final String MODEL = "model";
    public static final String FIELD_1_VALUE = "9756704";
    public static final String FIELD_2_VALUE = "Marko Vukovic Žuca";
    public static final String PRODUCER_ID_VALUE = "6101";
    public static final String BRANCH_ID_VALUE = "1111";
    public static final String PRODUCER_NAME_VALUE = "COCA-COLA";


    // Customer Master Data Report
    public static final String CUS_PAYER_ID = "payer_id";
    public static final String CUS_CUSTOMER_NAME_1 = "customer_name_1";
    public static final String CUS_CUSTOMER_NAME_2 = "customer_name_2";
    public static final String CUS_COUNTRY_ID = "country_id";
    public static final String CUS_CITY = "city";
    public static final String CUS_POSTAL_CODE = "postal_code";
    public static final String CUS_STREET = "street";
    public static final String CUS_TIN = "tin";


    // Product Master Data Report
    public static final String PRO_EAN = "ean";
    public static final String PRO_PRODUCT_NAME_1 = "product_name_1";
    public static final String PRO_PRODUCT_NAME_2 = "product_name_2";
    public static final String PRO_MEASURE_UNIT_ID = "measure_unit_id";
    public static final String PRO_MEASURE_UNIT = "measure_unit";
    public static final Double PRO_MULTIPLIER_VALUE = 1.0;

    // Sales Report
    public static final String SAL_FROM_DATE = "from_date";
    public static final String SAL_TO_DATE = "to_date";
    public static final String SAL_TYPE_DATE = "type_date";
    public static final String SAL_ISSUE_DATE = "issue_date";
    public static final String SAL_SALE_DATE = "sale_date";
    public static final String SAL_TYPE_ID = "type_id";
    public static final String SAL_DOC_ID = "doc_id";
    public static final String SAL_FIELD_1 = "field_1";
    public static final String SAL_FIELD_2 = "field_2";
    public static final String SAL_ITEM = "item";
    public static final String SAL_QUANTITY = "quantity";
    public static final String SAL_TYPE_ID_VALUE = "INV";
    public static final String SAL_TYPE_ID_RETURN_VALUE = "INC";
    public static final Double SAL_QUANTITY_RETURN_VALUE = 0.0;

    // Sales Report Correction Fields
    public static final String SAL_COR_DOC_ID = "cor_doc_id";
    public static final String SAL_COR_DOC_DATE = "cor_doc_date";
    public static final String SAL_QUANTITY_WAS = "quantity_was";

    // Warehouse Stocks Report
    public static final String STO_STOCK = "stock";

    // Sales Report intervals
    public static final int SAL_INTERVAL = 14;
    public static final int SAL5_INTERVAL = 35;

}
