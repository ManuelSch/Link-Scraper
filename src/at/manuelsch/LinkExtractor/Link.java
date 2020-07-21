package at.manuelsch.LinkExtractor;

import java.net.URL;

/**
 * Represents a <a href=""> tag extracted from HTML
 */
public interface Link extends Comparable<Link> {

    /**
     * Returns the title attribute of the <a> tag
     *
     * @return the label of the link; null if no title attribute was given
     */
    String getLabel();

    /**
     * Returns the href attribute of the <a> tag
     *
     * @return the URL of the link
     */
    URL getUrl();
}
