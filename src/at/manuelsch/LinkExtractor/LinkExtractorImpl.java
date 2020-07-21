package at.manuelsch.LinkExtractor;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Allows to extract all <a href=""> tags from a HTML string
 */
public class LinkExtractorImpl implements LinkExtractor {

    /**
     * Returns all links that are present in the given HTML string
     *
     * @param html    the HTML string
     * @param hostUrl the host URL of the website
     * @return list of all links extracted from the HTML string
     * @throws IOException in case the HTML parsing failed
     */
    @Override
    public List<Link> getAllLinksFromHTML(String html, URL hostUrl) throws IOException {
        List<Link> linkList = new ArrayList<>();

        ParserCallback anchorTagHandler = new ParserCallback() {
            @Override
            public void handleStartTag(HTML.Tag tag, MutableAttributeSet attributes, int position) {
                super.handleStartTag(tag, attributes, position);

                // handle only <a href=""> tags:
                if (tag == HTML.Tag.A) {
                    String href = (String) attributes.getAttribute(HTML.Attribute.HREF);

                    if (href != null) {
                        String title = (String) attributes.getAttribute(HTML.Attribute.TITLE);

                        if (href.startsWith("/")) {
                            href = hostUrl.getProtocol() + "://" + hostUrl.getHost() + href;
                        }

                        try {
                            linkList.add(new LinkImpl(title, new URL(href)));
                        } catch (MalformedURLException e) {
                            // ignore malformed URLs
                        }
                    }
                }
            }
        };

        // parse HTML:
        try {
            new ParserDelegator().parse(new StringReader(html), anchorTagHandler, true);
        } catch (IOException e) {
            throw new IOException("Could not parse HTML", e);
        }

        return linkList;
    }

}
