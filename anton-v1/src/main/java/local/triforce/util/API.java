package local.triforce.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class API {

    public static JsonNode call(String endpoint) {
        URL url = null;
        try {
            url = new URL(endpoint);

            //Set connection
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP Error code : "
                        + conn.getResponseCode() + " - " + conn.getResponseMessage());
            }


            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readTree(in);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
