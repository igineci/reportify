package melany.report;

import lombok.Getter;
import lombok.Setter;
import melany.model.SalesDoc;
import melany.utils.Constants;
import melany.utils.JsonParser;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Generates JSON sales reports from a collection of sales documents.
 * Includes sales and return data, grouped by document ID.
 *
 * @author andjela.djekic
 */
@Getter
@Setter
public class SalesReport {
    // Collection of Sales Documents
    private List<SalesDoc> salesDocs;
    private final JsonParser jsonParser = new JsonParser();

    /**
     * Generates a sales report in JSON format for a specified interval of days.
     *
     * @param intervalDays The number of days from the current date to include in the report.
     * @return JSON-formatted sales report as a string.
     * @throws IOException If an error occurs during JSON conversion.
     */
    public String generateSalesReport(int intervalDays) throws IOException {
        Map<String, Object> reportData = prepareReportData(salesDocs, intervalDays);
        return jsonParser.mapToJSON(reportData);
    }

    /**
     * Prepares the data for the sales report by organizing sales and return documents.
     *
     * @param salesDocs    List of sales documents.
     * @param intervalDays The number of days from the current date to include in the report.
     * @return A map containing the sales report data.
     */
    private Map<String, Object> prepareReportData(List<SalesDoc> salesDocs, int intervalDays) {

        Map<String, Object> result = new LinkedHashMap<>();

        // Add header metadata
        result.put(Constants.SAL_FROM_DATE.toUpperCase(), LocalDate.now().minusDays(intervalDays).toString());
        result.put(Constants.SAL_TO_DATE.toUpperCase(), LocalDate.now().toString());
        result.put(Constants.SAL_TYPE_DATE.toUpperCase(), Constants.SAL_ISSUE_DATE.toUpperCase());

        // Separate sales and return documents
        List<SalesDoc> saleDocs = filterSalesDocuments(salesDocs, false);
        List<SalesDoc> returnDocs = filterSalesDocuments(salesDocs, true);

        // Sales Data grouped by DOC ID - Processing Sales Data
        List<Map<String, Object>> salesData = processSalesData(saleDocs);

        // Add return data
        addReturnDataToJSON(salesData, returnDocs);

        result.put(Constants.R_SAL, salesData);
        return result;
    }

    /**
     * Filters sales documents based on their return status.
     *
     * @param salesDocs  List of sales documents.
     * @param isReturned Whether to filter returned documents.
     * @return Filtered list of sales documents.
     */
    private List<SalesDoc> filterSalesDocuments(List<SalesDoc> salesDocs, boolean isReturned) {
        return salesDocs.stream().filter(doc -> doc.isReturned() == isReturned).collect(Collectors.toList());
    }


    /**
     * Processes sales data by grouping it by document ID and formatting it.
     *
     * @param saleDocs List of sales documents.
     * @return A list of maps representing sales data.
     */
    private List<Map<String, Object>> processSalesData(List<SalesDoc> salesDocs) {
        Map<String, List<SalesDoc>> groupedByDocID = salesDocs.stream().collect(Collectors.groupingBy(SalesDoc::getDoc_ID));

        List<Map<String, Object>> salesData = new ArrayList<>();

        for (Map.Entry<String, List<SalesDoc>> entry : groupedByDocID.entrySet()) {
            Map<String, Object> docData = createSalesDocumentData(entry.getValue());
            salesData.add(docData);
        }

        return salesData;
    }

    /**
     * Creates the JSON data for a single sales document group.
     *
     * @param salesGroup List of sales documents in the group.
     * @return A map representing the sales document data.
     */
    private Map<String, Object> createSalesDocumentData(List<SalesDoc> salesGroup) {
        Map<String, Object> salesDocData = new LinkedHashMap<>();
        SalesDoc firstDoc = salesGroup.get(0);

        salesDocData.put(Constants.SAL_TYPE_ID.toUpperCase(), Constants.SAL_TYPE_ID_RETURN_VALUE);
        salesDocData.put(Constants.BRANCH_ID.toUpperCase(), Constants.BRANCH_ID_VALUE);
        salesDocData.put(Constants.SAL_DOC_ID.toUpperCase(), firstDoc.getDoc_ID());
        salesDocData.put(Constants.SAL_ISSUE_DATE.toUpperCase(), firstDoc.getIssue_date());
        salesDocData.put(Constants.SAL_SALE_DATE.toUpperCase(), firstDoc.getSale_date());
        salesDocData.put(Constants.CUSTOMER_ID.toUpperCase(), firstDoc.getCustomer_ID());
        salesDocData.put(Constants.SAL_FIELD_1.toUpperCase(), Constants.FIELD_1_VALUE);
        salesDocData.put(Constants.SAL_FIELD_2.toUpperCase(), Constants.FIELD_2_VALUE);

        List<Map<String, Object>> salItems = salesGroup.stream().map(this::createSaleItemData).collect(Collectors.toList());

        salesDocData.put(Constants.SAL_JSON_MAP, salItems);
        return salesDocData;

    }

    /**
     * Creates JSON data for a single sales item.
     *
     * @param doc The sales document representing the item.
     * @return A map representing the sales item data.
     */
    private Map<String, Object> createSaleItemData(SalesDoc salesDoc) {
        Map<String, Object> itemData = new LinkedHashMap<>();
        itemData.put(Constants.SAL_ITEM.toUpperCase(), salesDoc.getItem());
        itemData.put(Constants.PRODUCT_ID.toUpperCase(), salesDoc.getProduct_ID());
        itemData.put(Constants.MEASURE_UNIT_ID.toUpperCase(), salesDoc.getMeasure_unit_ID());
        itemData.put(Constants.PRODUCER_ID.toUpperCase(), Constants.PRODUCER_ID_VALUE);
        itemData.put(Constants.SAL_QUANTITY.toUpperCase(), salesDoc.getQuantity());
        return itemData;
    }

    /**
     * Adds return data to the sales report JSON.
     *
     * @param salesData  List of sales data maps.
     * @param returnDocs List of return documents.
     */
    private void addReturnDataToJSON(List<Map<String, Object>> salesData, List<SalesDoc> returnedDoc) {
        Map<String, List<SalesDoc>> groupedByDocID = returnedDoc.stream().collect(Collectors.groupingBy(SalesDoc::getDoc_ID));

        for (Map.Entry<String, List<SalesDoc>> entry : groupedByDocID.entrySet()) {
            Map<String, Object> returnedData = createReturnDocumentData(entry.getValue());
            salesData.add(returnedData);
        }
    }

    /**
     * Creates the JSON data for a single return document group.
     *
     * @param returnGroup List of return documents in the group.
     * @return A map representing the return document data.
     */
    private Map<String, Object> createReturnDocumentData(List<SalesDoc> returnGroup) {
        Map<String, Object> docReturnData = new LinkedHashMap<>();

        SalesDoc firstDoc = returnGroup.get(0);

        docReturnData.put(Constants.SAL_TYPE_ID.toUpperCase(), Constants.SAL_TYPE_ID_RETURN_VALUE);
        docReturnData.put(Constants.BRANCH_ID.toUpperCase(), Constants.BRANCH_ID_VALUE);
        docReturnData.put(Constants.SAL_DOC_ID.toUpperCase(), firstDoc.getDoc_ID());
        docReturnData.put(Constants.SAL_ISSUE_DATE.toUpperCase(), firstDoc.getIssue_date());
        docReturnData.put(Constants.SAL_SALE_DATE.toUpperCase(), firstDoc.getSale_date());
        docReturnData.put(Constants.CUSTOMER_ID.toUpperCase(), firstDoc.getCustomer_ID());
        docReturnData.put(Constants.SAL_COR_DOC_DATE.toUpperCase(), firstDoc.getCor_doc_date());
        docReturnData.put(Constants.SAL_COR_DOC_ID.toUpperCase(), firstDoc.getCor_doc_id());
        docReturnData.put(Constants.SAL_FIELD_1.toUpperCase(), Constants.FIELD_1_VALUE);
        docReturnData.put(Constants.SAL_FIELD_2.toUpperCase(), Constants.FIELD_2_VALUE);

        List<Map<String, Object>> returnItems = returnGroup.stream().map(this::createReturnItemData).collect(Collectors.toUnmodifiableList());

        docReturnData.put(Constants.SAL_JSON_MAP, returnItems);

        return docReturnData;
    }

    /**
     * Creates JSON data for a single return item.
     *
     * @param doc The return document representing the item.
     * @return A map representing the return item data.
     */
    private Map<String, Object> createReturnItemData(SalesDoc salesDoc) {
        Map<String, Object> returnedItemData = new LinkedHashMap<>();
        returnedItemData.put(Constants.SAL_ITEM.toUpperCase(), salesDoc.getItem());
        returnedItemData.put(Constants.PRODUCT_ID.toUpperCase(), salesDoc.getProduct_ID());
        returnedItemData.put(Constants.MEASURE_UNIT_ID.toUpperCase(), salesDoc.getMeasure_unit_ID());
        returnedItemData.put(Constants.PRODUCER_ID.toUpperCase(), Constants.PRODUCER_ID_VALUE);
        returnedItemData.put(Constants.SAL_QUANTITY.toUpperCase(), Constants.SAL_QUANTITY_RETURN_VALUE);
        returnedItemData.put(Constants.SAL_QUANTITY_WAS.toUpperCase(), salesDoc.getQuantity_was());
        return returnedItemData;
    }


}
