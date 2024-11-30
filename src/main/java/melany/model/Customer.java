package melany.model;

import lombok.Setter;
import melany.database.DatabaseConn;
import melany.utils.Constants;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Model class representing a customer entity within the system.
 * It provides functionality to load customer data and returned goods from a database.
 *
 * <p>
 * This class utilizes Lombok annotations to reduce boilerplate code.
 * </p>
 *
 * @author andjela.djekic
 */
@Setter
public class Customer {

    private String branch_ID;
    private String customer_ID;
    private String payer_ID;
    private String customer_name_1;
    private String customer_name_2;
    private String country_ID;
    private String city;
    private String postal_Code;
    private String street;
    private String tin;
    private Integer activity;


    /**
     * Loading all Customer Data from the database
     *
     * @param dbConn manager for database connection
     * @return List of {@link Customer} instances
     */
    public static List<Customer> loadCustomerData(DatabaseConn dbConn) {

        List<Customer> customers = new ArrayList<>();

        try {

            ResultSet rs = dbConn.fetch(Constants.CUS_QUERY);

            while (rs.next()) {

                Customer customer = new Customer();

                customer.setBranch_ID(rs.getString(Constants.BRANCH_ID));
                customer.setCustomer_ID(rs.getString(Constants.CUSTOMER_ID));
                customer.setPayer_ID(rs.getString(Constants.CUS_PAYER_ID));
                customer.setCustomer_name_1(rs.getString(Constants.CUS_CUSTOMER_NAME_1));
                customer.setCustomer_name_2(rs.getString(Constants.CUS_CUSTOMER_NAME_2));
                customer.setCountry_ID(rs.getString(Constants.CUS_COUNTRY_ID));
                String city = rs.getString(Constants.CUS_CITY);
                customer.setCity((city != null && !city.trim().isEmpty()) ? city : Constants.DEFAULT_STRING);
                String postalCode = rs.getString(Constants.CUS_POSTAL_CODE);
                customer.setPostal_Code((postalCode != null && !postalCode.trim().isEmpty()) ? postalCode : Constants.DEFAULT_POSTAL_CODE);
                String street = rs.getString(Constants.CUS_STREET);
                customer.setStreet((street != null && !street.trim().isEmpty()) ? street : Constants.DEFAULT_STRING);
                String tin = rs.getString(Constants.CUS_TIN);
                customer.setTin((tin != null && !tin.trim().isEmpty()) ? tin : Constants.DEFAULT_TIN);
                customer.setActivity(rs.getInt(Constants.ACTIVITY));

                customers.add(customer);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return customers;

    }
}

