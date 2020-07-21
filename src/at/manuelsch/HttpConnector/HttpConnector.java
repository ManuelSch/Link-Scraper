package at.manuelsch.HttpConnector;

import java.io.IOException;
import java.net.URL;

/**
 * Allows to fetch the HTML code of the website
 */
public interface HttpConnector {

    /**
     * Fetches the given URL via a HTTP GET request and returns the page source as a string
     *
     * @param url the URL to be fetched
     * @return the page source
     * @throws IOException in case the GET request failed
     */
    String getPageSource(URL url) throws IOException;

}
