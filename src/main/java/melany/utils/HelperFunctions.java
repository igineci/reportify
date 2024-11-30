package melany.utils;

/**
 * Utility class for helper functions.
 * Currently only custom function for generating product ID for reports.
 *
 * @author andjela.djekic
 */
public class HelperFunctions {

    /**
     * Function for joining artikal+tip+boja+model into product ID
     *
     * @param artikal name of the product (artikal is database field name from company - refers to article)
     * @param tip     type of the product (tip is database field name from company - refers to type)
     * @param boja    color of the product (boja is database field name from company - refers to color)
     * @param model   model of the product (model is database field name from company - refers to model)
     * @return a {@link String} that is ProductID field ready for reports.
     */
    public static String generateProductID(String artikal, String tip, String boja, String model) {

        StringBuilder productId = new StringBuilder();

        if (artikal != null && !artikal.isEmpty()) {
            productId.append(artikal);
        }
        if (tip != null && !tip.isEmpty()) {
            if (productId.length() > 0) {
                productId.append(Constants.UNDERSCORE);
            }
            productId.append(tip);
        }
        if (boja != null && !boja.isEmpty()) {
            if (productId.length() > 0) {
                productId.append(Constants.UNDERSCORE);
            }
            productId.append(boja);
        }
        if (model != null && !model.isEmpty()) {
            if (productId.length() > 0) {
                productId.append(Constants.UNDERSCORE);
            }
            productId.append(model);
        }

        return productId.toString();
    }

}
