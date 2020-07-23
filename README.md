# Java Link Scraper

A simple web scraper implemented in Java 8.

1. Fetches the given entry URL
2. Extracts all ``<a href="">`` tags from the HTML page source that point to the same domain as the entry URL
3. Repeats steps 1. and 2. with the newly found links until the whole website has been scraped
4. Outputs all found URLs together with their ``title`` attributes

Uses a fixed-size Thread pool for concurrent execution of each scrape request.
