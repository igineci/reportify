package melany.model;

import lombok.Setter;
import melany.database.DatabaseConn;
import melany.utils.Constants;
import melany.utils.HelperFunctions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a product entity within the system.
 * It provides functionality to load product data and returned goods from a database.
 *
 * <p>
 * This class utilizes Lombok annotations to reduce boilerplate code.
 * </p>
 *
 * @author andjela.djekic
 */
@Setter
public class Product {

    private String branch_ID;
    private String product_ID;
    private String ean;
    private String product_name_1;
    private String product_name_2;
    private String measure_unit_ID;
    private String measure_unit;
    private String producer_ID;
    private String producer_name;
    private Integer activity;
    private Double multiplier;

    /**
     * Loading all Product Data from the database
     *
     * @param dbConn manager for database connection
     * @return List of {@link Product} instances
     */
    public static List<Product> loadProductData(DatabaseConn dbConn) {

        List<Product> products = new ArrayList<>();

        try {

            ResultSet rs = dbConn.fetch(Constants.PRO_QUERY);

            while (rs.next()) {

                Product product = new Product();

                product.setBranch_ID(Constants.BRANCH_ID_VALUE);
                product.setProduct_ID(HelperFunctions.generateProductID(rs.getString(Constants.ARTIKAL), rs.getString(Constants.TIP), rs.getString(Constants.BOJA), rs.getString(Constants.MODEL)));
                product.setEan(rs.getString(Constants.PRO_EAN));
                product.setProduct_name_1(rs.getString(Constants.PRO_PRODUCT_NAME_1));
                product.setProduct_name_2(rs.getString(Constants.PRO_PRODUCT_NAME_2));
                product.setMeasure_unit_ID(rs.getString(Constants.PRO_MEASURE_UNIT_ID));
                product.setMeasure_unit(rs.getString(Constants.PRO_MEASURE_UNIT));
                product.setProducer_ID(Constants.PRODUCER_ID_VALUE);
                product.setProducer_name(Constants.PRODUCER_NAME_VALUE);
                product.setActivity(rs.getInt(Constants.ACTIVITY));
                product.setMultiplier(Constants.PRO_MULTIPLIER_VALUE);

                products.add(product);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return products;
    }
}


