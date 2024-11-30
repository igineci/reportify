package melany.report;

import lombok.Setter;
import melany.model.Customer;
import melany.utils.Constants;
import melany.utils.JsonParser;

import java.io.IOException;
import java.util.List;

public class CustomerReport {

    @Setter
    private List<Customer> customers;
    private final JsonParser jsonParser = new JsonParser();

    /**
     * Generates a customer report in JSON format.
     *
     * @return JSON-formatted customer report as a string.
     * @throws IOException If an error occurs during JSON conversion.
     */
    public String generateCustomerReport() throws IOException {
        return jsonParser.listToJSON(customers, Constants.R_CUS);
    }

}