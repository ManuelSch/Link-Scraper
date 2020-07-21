package at.manuelsch.LinkExtractor;

import java.net.URL;
import java.util.Objects;

/**
 * Represents a <a href=""> tag extracted from HTML
 */
public class LinkImpl implements Link {

    private final String label;
    private final URL url;

    /**
     * Initializes the link
     *
     * @param label the link label
     * @param url   the URL the link points to
     */
    public LinkImpl(String label, URL url) {
        this.label = label;
        this.url = url;
    }

    /**
     * Returns the title attribute of the <a> tag
     *
     * @return the label of the link; null if no title attribute was given
     */
    @Override
    public String getLabel() {
        return label;
    }

    /**
     * Returns the href attribute of the <a> tag
     *
     * @return the URL of the link
     */
    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkImpl link = (LinkImpl) o;
        return Objects.equals(label, link.label) &&
                Objects.equals(url, link.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label, url);
    }

    @Override
    public String toString() {
        return "Link: {" +
                "label:\"" + label + '"' +
                ", url:\"" + url + '"' +
                '}';
    }

    @Override
    public int compareTo(Link o) {
        return (this.getLabel() + this.getUrl().toString()).compareToIgnoreCase(o.getLabel() + o.getUrl().toString());
    }
}
