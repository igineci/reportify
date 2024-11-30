package melany;

import melany.api.ApiClient;

import melany.cli.CliArgsParser;
import melany.database.DatabaseConn;
import melany.model.Customer;

import melany.model.Product;
import melany.model.SalesDoc;
import melany.model.Stock;
import melany.report.CustomerReport;
import melany.report.ProductReport;
import melany.report.SalesReport;
import melany.report.StockReport;
import melany.utils.Constants;

import java.util.Set;

/**
 * Main Application Entry Point
 * <p>
 * This class orchestrates the generation and submission of various reports
 * (Customer, Product, Sales, Stock) based on command-line -r argument.
 * It connects to the database, processes data into JSON Reports and sends them to an external API.
 *
 * @author andjela.djekic
 */
public class Main {

    public static void main(String[] args) {

        try {

            // Initialize the API client for communication with the external system
            ApiClient apiClient = new ApiClient();

            // Parse command-line arguments to extract user-specified options
            CliArgsParser parser = new CliArgsParser();
            parser.parse(args);

            // Establish a connection to the database using the provided CLI arguments
            DatabaseConn dbConn = new DatabaseConn(parser);
            dbConn.getConnection();

            // Retrieve the set of reports the user wants to generate
            Set<String> reportsToGenerate = parser.getReports();

            // Generate and send Customer Report (CUS)
            if (reportsToGenerate.contains(Constants.R_CUS.toLowerCase())) {

                // Creating CUS Report
                CustomerReport cusReport = new CustomerReport();
                cusReport.setCustomers(Customer.loadCustomerData(dbConn));
                String report_CUS = cusReport.generateCustomerReport();

                //System.out.println(report_CUS);

                boolean isCUSok = apiClient.post(report_CUS);
                System.out.println(isCUSok);
            }

            // Generate and send Product Report (PRO)
            if (reportsToGenerate.contains(Constants.R_PRO.toLowerCase())) {

                // Creating PRO Report
                ProductReport proReport = new ProductReport();
                proReport.setProducts(Product.loadProductData(dbConn));
                String report_PRO = proReport.generatePROReport();

                //System.out.println(report_PRO);

                boolean isPROok = apiClient.post(report_PRO);
                System.out.println(isPROok);
            }

            // Generate and send Sales Report (SAL)
            if (reportsToGenerate.contains(Constants.R_SAL.toLowerCase())) {

                // Creating SAL Report (for the last 14 days)
                SalesReport salReport = new SalesReport();
                salReport.setSalesDocs(SalesDoc.loadSalesData(dbConn, Constants.SAL_INTERVAL));
                String report_SAL = salReport.generateSalesReport(Constants.SAL_INTERVAL);

                //System.out.println(report_SAL);

                boolean isSALok = apiClient.post(report_SAL);
                System.out.println(isSALok);
            }

            // Generate and send Sales Report (SAL - Sent every 5th in the month)
            if (reportsToGenerate.contains(Constants.R_SAL5.toLowerCase())) {

                // Creating SAL5 Report (for the last 35 days)
                SalesReport sal5Report = new SalesReport();
                sal5Report.setSalesDocs(SalesDoc.loadSalesData(dbConn, Constants.SAL5_INTERVAL));
                String report_SAL5 = sal5Report.generateSalesReport(Constants.SAL5_INTERVAL);

                //System.out.println(report_SAL5);

                boolean isSAL5ok = apiClient.post(report_SAL5);
                System.out.println(isSAL5ok);
            }

            // Generate and send Stock Report (STO)
            if (reportsToGenerate.contains(Constants.R_STO.toLowerCase())) {

                // Creating STO Report
                StockReport stoReport = new StockReport();
                stoReport.setStocks(Stock.loadStockData(dbConn));
                String report_STO = stoReport.generateSTOReport();

                //System.out.println(report_STO);

                boolean isSTOok = apiClient.post(report_STO);
                System.out.println(isSTOok);
            }

            // Close the database connection after all reports have been processed.
            dbConn.disconnect();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
}