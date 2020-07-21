package at.manuelsch.LinkScraper;

import at.manuelsch.HttpConnector.HttpConnector;
import at.manuelsch.LinkExtractor.Link;
import at.manuelsch.LinkExtractor.LinkExtractor;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Fetches a given URL, extracts a set of links that point to the same domain and executes the given fallbacks
 */
public class ScrapeWorkerImpl implements ScrapeWorker {

    private final Logger logger = Logger.getLogger(ScrapeWorkerImpl.class.getName());

    private final URL hostUrl;
    private final URL url;
    private final HttpConnector httpConnector;
    private final LinkExtractor linkExtractor;
    private final Consumer<Link> onLinkFound;
    private final Runnable onFinished;

    /**
     * Initializes the ScrapeWorker
     *
     * @param hostUrl       the host URL
     * @param url           the URL that should be scraped
     * @param httpConnector HttpConnector implementation
     * @param linkExtractor LinkExtractor implementation
     * @param onLinkFound   is executed every time a link is found
     * @param onFinished    is executed as soon as the worker has finished
     */
    public ScrapeWorkerImpl(
            URL hostUrl,
            URL url,
            HttpConnector httpConnector,
            LinkExtractor linkExtractor,
            Consumer<Link> onLinkFound,
            Runnable onFinished
    ) {
        this.hostUrl = hostUrl;
        this.url = url;
        this.httpConnector = httpConnector;
        this.linkExtractor = linkExtractor;
        this.onLinkFound = onLinkFound;
        this.onFinished = onFinished;
    }

    /**
     * Fetches a given URL, extracts a set of links that point to the same domain and executes the given fallbacks
     */
    @Override
    public void run() {
        try {
            String pageSource = this.httpConnector.getPageSource(this.url);

            List<Link> linkList = this.linkExtractor.getAllLinksFromHTML(pageSource, this.hostUrl);

            linkList.stream()
                    .filter(link -> link.getUrl() != null)
                    // exclude any links that do not point to the same domain:
                    .filter(link -> link.getUrl().getHost().endsWith(this.hostUrl.getHost()))
                    .forEach(this.onLinkFound);

        } catch (IOException e) {
            logger.log(Level.WARNING, "Scraping failed: " + e.getMessage());
        } finally {
            onFinished.run();
        }
    }

}
