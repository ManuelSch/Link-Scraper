package at.manuelsch.LinkExtractor;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Allows to extract all <a href=""> tags from a HTML string
 */
public interface LinkExtractor {

    /**
     * Returns all links that are present in the given HTML string
     *
     * @param html    the HTML string
     * @param hostUrl the host URL of the website
     * @return list of all links extracted from the HTML string
     * @throws IOException in case the HTML parsing failed
     */
    List<Link> getAllLinksFromHTML(String html, URL hostUrl) throws IOException;

}
