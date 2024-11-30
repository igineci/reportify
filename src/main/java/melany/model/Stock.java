package melany.model;

import lombok.Setter;
import melany.database.DatabaseConn;
import melany.utils.Constants;
import melany.utils.HelperFunctions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Stock class represents data on the current stock of products in the warehouse.

/**
 * Model class representing data on the current stock of products in the warehouse.
 * <p>
 * It provides functionality to load data on the current stock of products in the warehouse from a database.
 * <p>
 * This class utilizes Lombok annotations to reduce boilerplate code.
 * </p>
 *
 * @author andjela.djekic
 */
@Setter
public class Stock {

    private String branch_ID;
    private String product_ID;
    private LocalDate stock_date;
    private Double stock;
    private String measure_unit_ID;
    private String producer_ID;

    /**
     * Load stock data from the database
     *
     * @param dbConn the database connection manager
     * @return List of {@link Stock} instances
     */
    public static List<Stock> loadStockData(DatabaseConn dbConn) {

        List<Stock> stocks = new ArrayList<>();

        try {

            ResultSet rs = dbConn.fetch(Constants.STO_QUERY);

            while (rs.next()) {

                Stock stock = new Stock();

                stock.setBranch_ID(Constants.BRANCH_ID_VALUE);
                stock.setProduct_ID(HelperFunctions.generateProductID(rs.getString(Constants.ARTIKAL), rs.getString(Constants.TIP), rs.getString(Constants.BOJA), rs.getString(Constants.MODEL)));
                stock.setStock_date(LocalDate.now());
                stock.setStock(rs.getDouble(Constants.STO_STOCK));
                stock.setMeasure_unit_ID(rs.getString(Constants.MEASURE_UNIT_ID));
                stock.setProducer_ID(Constants.PRODUCER_ID_VALUE);

                stocks.add(stock);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return stocks;

    }
}
