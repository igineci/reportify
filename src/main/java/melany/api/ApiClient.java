package melany.api;

import melany.utils.Constants;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * A utility class for interacting with the third-party API.
 * <p>
 * Provides methods for HTTP Requests, Basic Authorization.
 *
 * @author andjela.djekic
 */
public class ApiClient {

    /**
     * HTTP POST method
     *
     * @return String containing Basic Authentication Header
     */
    private String getBasicAuthHeader() {
        String auth = Constants.API_USERNAME + ":" + Constants.API_PASSWORD;
        return Constants.API_AUTH_BASIC + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * HTTP POST method
     *
     * @param requestBody body that will be sent in HTTP POST request
     * @return true if HTTP request went successfully
     */
    public boolean post(String requestBody) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(Constants.API_URL))
                    .header(Constants.API_CONTENT_HEADER_NAME, Constants.API_CONTENT_HEADER_VALUE)
                    .header(Constants.API_AUTH_HEADER_NAME, getBasicAuthHeader())
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String httpStatus = Integer.toString(response.statusCode());
            String httpBody = response.body().toString();
            System.out.println(httpBody);
            System.out.println(httpStatus);
            if (httpStatus.equalsIgnoreCase(Constants.HTTP_200_OK) && httpBody.contains(Constants.API_STATUS_CODE)) {
                return true;
            }
            return false;

        } catch (IOException | InterruptedException ex) {
            System.out.println(ex.getMessage());
            String httpResponse = Constants.HTTP_500_ERROR;
            String httpBody = ex.getMessage();
            return false;
        }
    }

}
