package melany.model;

import lombok.Getter;
import lombok.Setter;
import melany.database.DatabaseConn;
import melany.utils.Constants;
import melany.utils.HelperFunctions;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a Sales Report entity within the system.
 * This class encapsulates all attributes and operations related to sales and return transactions.
 * It provides functionality to load sales data and returned goods from a database.
 * <p>
 * This class utilizes Lombok annotations to reduce boilerplate code.
 * </p>
 *
 * @author andjela.djekic
 */
@Setter
@Getter
public class SalesDoc {

    // Attributes representing sales document details
    private String doc_ID;
    private LocalDate issue_date;
    private LocalDate sale_date;
    private String customer_ID;
    private Integer item;
    private String product_ID;
    private String measure_unit_ID;
    private Double quantity;
    private String cor_doc_id;
    private LocalDate cor_doc_date;
    private Double quantity_was;

    /**
     * Flag indicating if the document is a return transaction
     */
    private boolean isReturned;

    /**
     * Load sales and returned goods data from the database for a given time interval.
     *
     * @param dbConn       the database connection manager
     * @param intervalDays the number of days for filtering the data
     * @return List of {@link SalesDoc} instances
     */
    public static List<SalesDoc> loadSalesData(DatabaseConn dbConn, int intervalDays) {

        List<SalesDoc> salesDocs = new ArrayList<>();

        salesDocs.addAll(fetchSalesData(dbConn, intervalDays));
        salesDocs.addAll(fetchReturnData(dbConn, intervalDays));

        return salesDocs;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    /**
     * Maps a {@link ResultSet} row to a {@link SalesDoc} instance.
     *
     * @param resultSet  the result set containing sales data
     * @param isReturned indicates if the data represents a return transaction
     * @return a {@link SalesDoc} instance
     * @throws SQLException if an SQL error occurs
     */
    private static SalesDoc mapResultSetToSalesDoc(ResultSet resultSet, boolean isReturned) throws SQLException {

        SalesDoc salesDoc = new SalesDoc();

        salesDoc.setIssue_date(resultSet.getDate(Constants.SAL_ISSUE_DATE).toLocalDate());
        salesDoc.setSale_date(resultSet.getDate(Constants.SAL_SALE_DATE).toLocalDate());
        salesDoc.setDoc_ID(resultSet.getString(Constants.SAL_DOC_ID));
        salesDoc.setCustomer_ID(resultSet.getString(Constants.CUSTOMER_ID));
        salesDoc.setItem(resultSet.getInt(Constants.SAL_ITEM));
        salesDoc.setProduct_ID(HelperFunctions.generateProductID(resultSet.getString(Constants.ARTIKAL), resultSet.getString(Constants.TIP), resultSet.getString(Constants.BOJA), resultSet.getString(Constants.MODEL)));
        salesDoc.setMeasure_unit_ID(resultSet.getString(Constants.MEASURE_UNIT_ID));
        salesDoc.setQuantity(resultSet.getDouble(Constants.SAL_QUANTITY));

        salesDoc.setReturned(isReturned);

        if (isReturned) {
            salesDoc.setQuantity_was(resultSet.getDouble(Constants.SAL_QUANTITY_WAS));
            salesDoc.setCor_doc_date(resultSet.getDate(Constants.SAL_COR_DOC_DATE).toLocalDate());
            salesDoc.setCor_doc_id(resultSet.getString(Constants.SAL_COR_DOC_ID));
        }

        return salesDoc;
    }

    /**
     * Fetches return data from the database.
     *
     * @param dbConn       the database connection manager
     * @param intervalDays the number of days for filtering the data
     * @return a list of {@link SalesDoc} instances representing return data
     */
    private static List<SalesDoc> fetchReturnData(DatabaseConn dbConn, int intervalDays) {
        List<SalesDoc> returnSalesDocs = new ArrayList<>();
        try (ResultSet resultSet = dbConn.fetch(String.format(Constants.SAL_RETURN_QUERY, intervalDays, intervalDays))) {
            while (resultSet.next()) {
                returnSalesDocs.add(mapResultSetToSalesDoc(resultSet, true));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return returnSalesDocs;
    }

    /**
     * Fetches sales data from the database.
     *
     * @param dbConn       the database connection manager
     * @param intervalDays the number of days for filtering the data
     * @return a list of {@link SalesDoc} instances representing return data
     */
    private static List<SalesDoc> fetchSalesData(DatabaseConn dbConn, int intervalDays) {
        List<SalesDoc> salesDocs = new ArrayList<>();
        try (ResultSet resultSet = dbConn.fetch(String.format(Constants.SAL_QUERY, intervalDays, intervalDays))) {
            while (resultSet.next()) {
                salesDocs.add(mapResultSetToSalesDoc(resultSet, false));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return salesDocs;
    }

}

