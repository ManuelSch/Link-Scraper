package at.manuelsch.LinkScraper;

import at.manuelsch.LinkExtractor.Link;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Allows to scrape all pages of a given website and gathers all found <a href=""> tags as a set of links;
 * Is able to spawn multiple ScraperWorkers concurrently
 */
public interface LinkScraper {

    /**
     * Start the scraping and wait for it to finish
     *
     * @param timeout the maximum time to wait
     * @param unit    the time unit of the timeout argument
     * @return set of all links that were found on the website
     */
    Set<Link> scrape(long timeout, TimeUnit unit);

}
