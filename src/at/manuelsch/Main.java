package at.manuelsch;

import at.manuelsch.HttpConnector.HttpConnectorImpl;
import at.manuelsch.LinkExtractor.Link;
import at.manuelsch.LinkExtractor.LinkExtractorImpl;
import at.manuelsch.LinkScraper.LinkScraper;
import at.manuelsch.LinkScraper.LinkScraperImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Scrapes a website, starting from the ENTRY_URL, and outputs a set of all found links that point to the same domain
 */
public class Main {

    /**
     * The maximum number of worker threads
     */
    private static final int N_THREADS = 4;


    public static void main(String[] args) {
        String entryUrl = args[0];

        if(entryUrl == null || "".equals(entryUrl)) {
            System.err.println("Please provide an entry URL");
            return;
        }

        try {
            LinkScraper linkScraper = new LinkScraperImpl(new URL(entryUrl), N_THREADS, new HttpConnectorImpl(), new LinkExtractorImpl());

            System.out.println("Start scraping '" + entryUrl + "' with " + N_THREADS + " worker threads...\n");
            Set<Link> foundLinks = linkScraper.scrape(1, TimeUnit.HOURS);

            System.out.println();
            for (Link link : foundLinks) {
                System.out.println(link);
            }
        } catch (MalformedURLException e) {
            System.err.println("The given entry URL is malformed.");
            e.printStackTrace();
        }
    }
}
