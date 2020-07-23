package at.manuelsch.LinkScraper;

import at.manuelsch.HttpConnector.HttpConnector;
import at.manuelsch.LinkExtractor.Link;
import at.manuelsch.LinkExtractor.LinkExtractor;

import java.net.URL;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Allows to scrape all pages of a given website and gathers all found <a href=""> tags as a set of links;
 * Is able to spawn multiple ScraperWorkers concurrently
 */
public class LinkScraperImpl implements LinkScraper {

    private final Logger logger = Logger.getLogger(LinkScraperImpl.class.getName());

    private final URL entryUrl;
    private final ExecutorService executor;
    private final Set<Link> allLinks = new ConcurrentSkipListSet<>();
    private final AtomicInteger pendingWorkers = new AtomicInteger(0);
    private final HttpConnector httpConnector;
    private final LinkExtractor linkExtractor;

    /**
     * Initializes the LinkScraper
     *
     * @param entryUrl      the first URL that will be scraped
     * @param nThreads      the maximum number of worker threads
     * @param httpConnector HttpConnector implementation
     * @param linkExtractor LinkExtractor implementation
     */
    public LinkScraperImpl(URL entryUrl, int nThreads, HttpConnector httpConnector, LinkExtractor linkExtractor) {
        this.entryUrl = entryUrl;
        this.executor = Executors.newFixedThreadPool(nThreads);
        this.httpConnector = httpConnector;
        this.linkExtractor = linkExtractor;
    }

    /**
     * Start the scraping and wait for it to finish
     *
     * @param timeout the maximum time to wait
     * @param unit    the time unit of the timeout argument
     * @return set of all links that were found on the website
     */
    @Override
    public Set<Link> scrape(long timeout, TimeUnit unit) {
        this.executeNewWorker(this.entryUrl);
        this.awaitTermination(timeout, unit);
        return allLinks;
    }

    /**
     * Creates and executes a new ScrapeWorker
     */
    private synchronized void executeNewWorker(URL url) {
        this.executor.execute(this.createWorker(url));
        int nWorkers = this.pendingWorkers.incrementAndGet();
        logger.log(Level.INFO, "pending workers: " + nWorkers + " (+) " + url);
    }

    /**
     * Creates a new ScrapeWorker
     *
     * @param url the URL the new worker should scrape
     * @return the newly created worker
     */
    private ScrapeWorker createWorker(URL url) {
        return new ScrapeWorkerImpl(this.entryUrl, url, httpConnector, linkExtractor,
                link -> {
                    // start a new worker if the found link hasn't already been scraped:
                    if (this.allLinks.add(link)) {
                        this.executeNewWorker(link.getUrl());
                    }
                },
                () -> {
                    // shutdown the LinkScraper if no other workers are pending:
                    int nWorkers = this.pendingWorkers.decrementAndGet();
                    logger.log(Level.INFO, "pending workers: " + nWorkers + " (-) " + url);
                    if (nWorkers <= 0) {
                        this.executor.shutdown();
                    }
                });
    }

    /**
     * Wait for the executor to shutdown or the time to run out
     *
     * @param timeout the maximum time to wait
     * @param unit    the time unit of the timeout argument
     */
    private void awaitTermination(long timeout, TimeUnit unit) {
        try {
            if (!this.executor.awaitTermination(timeout, unit)) {
                this.executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "The termination took longer than the given timeout.");
            this.executor.shutdownNow();
        }
    }
}
