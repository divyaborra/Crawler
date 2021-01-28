import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Crawler
{
	private static final int MAX_PAGES_TO_SEARCH = 10;
	// set with unique entries to ensure we don't revisit pages
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisit = new LinkedList<String>();

	public void search(String url, String searchWord)
	{
		boolean wordFound = false;
		while (this.pagesVisited.size() < MAX_PAGES_TO_SEARCH)
		{
			String currentUrl;
			CrawlerParser parser = new CrawlerParser();
			if (this.pagesToVisit.isEmpty())
			{
				currentUrl = url;
				this.pagesVisited.add(url);
			} else
			{
				currentUrl = this.nextUrl();
			}
			parser.crawl(currentUrl);
			boolean success = parser.searchForWord(searchWord);
			if (success)
			{
				System.out.println("Success! Word " + searchWord + " found at " + currentUrl);
				wordFound = true;
				break;
			}
			this.pagesToVisit.addAll(parser.getLinks());
		}
		if (!wordFound)
			System.out.println("\nWord " + searchWord + " not found.");
		System.out.println("\nDone crawling. Visited " + this.pagesVisited.size() + " web pages.");
	}

	// returns next url to visit (in order found)
	public String nextUrl()
	{
		String nextUrl;

		nextUrl = this.pagesToVisit.remove(0);
		// just in case nextURL is already in Visited list
		while (this.pagesVisited.contains(nextUrl))
		{
			nextUrl = this.pagesToVisit.remove(0);
		}

		this.pagesVisited.add(nextUrl);
		return nextUrl;
	}
}