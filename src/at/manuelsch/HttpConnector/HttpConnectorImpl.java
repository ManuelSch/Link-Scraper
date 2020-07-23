package at.manuelsch.HttpConnector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Allows to fetch the HTML code of the website
 */
public class HttpConnectorImpl implements HttpConnector {

    /**
     * Fetches the given URL via a HTTP GET request and returns the page source as a string
     *
     * @param url the URL to be fetched
     * @return the page source
     * @throws IOException in case the GET request failed
     */
    @Override
    public String getPageSource(URL url) throws IOException {
        HttpURLConnection con = null;
        BufferedReader in = null;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            StringBuilder content = new StringBuilder();

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            return content.toString();
        } catch (IOException e) {
            String errorMsg = "GET request for url '" + url + "' failed";
            if (con != null) {
                errorMsg += " (status " + con.getResponseCode() + ")";
            }
            throw new IOException(errorMsg, e);
        } finally {
            if (in != null) {
                in.close();
            }
            if (con != null) {
                con.disconnect();
            }
        }
    }

}
