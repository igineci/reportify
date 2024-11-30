/**
 *
 * Author: Andjela
 * Date: 20.8.2024.
 *
 */

package melany.report;

import melany.model.Stock;
import melany.utils.Constants;
import melany.utils.FileManager;
import melany.utils.JsonParser;


import java.io.IOException;
import java.util.List;

public class StockReport {
    private List<Stock> stocks;
    private final JsonParser jsonParser = new JsonParser();

    // Method for generating STO report and save it to JSON file.
    public String generateSTOReport() throws IOException {
        return jsonParser.listToJSON(stocks, Constants.R_STO);
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }
}
