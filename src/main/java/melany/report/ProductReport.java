/**
 *
 * Author: Andjela
 * Date: 20.8.2024.
 *
 */

package melany.report;

import melany.model.Product;
import melany.utils.Constants;
import melany.utils.JsonParser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ProductReport {

    private List<Product> products;
    private final JsonParser jsonParser = new JsonParser();

    public String generatePROReport() throws IOException {
        return jsonParser.listToJSON(products, Constants.R_PRO);
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
