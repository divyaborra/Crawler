import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerParser
{
	// fake user agent??
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	private List<String> links = new LinkedList<String>();
	private Document htmlDoc;

	// makes HTTP request, checks response, collects all links on page
	public boolean crawl(String url)
	{
		try
		{
			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			// fetching content & parsing it into a Document
			Document htmlDoc = connection.get();
			this.htmlDoc = htmlDoc;

			// http 200 ok status means request was successful
			if (connection.response().statusCode() == 200)
			{
				System.out.println("\nVisiting web page at " + url);
			}
			if (!connection.response().contentType().contains("text/html"))
			{
				System.out.println("Failure! Retrieved something other than HTML");
				return false;
			}
			// parsing all a href links from doc
			Elements linksOnPage = htmlDoc.select("a[href]");
			System.out.println("Found " + linksOnPage.size() + " links");
			for (Element link : linksOnPage)
			{
				this.links.add(link.absUrl("href"));
			}
			return true;
		}
		catch (IOException ioe)
		{
			// if http request wasn't successful
			return false;
		}
	}

	// performs a search in body of HTML document that we retrieved
	public boolean searchForWord(String searchWord)
	{
		System.out.println("Searching for word " + searchWord + "...");
		String bodyText = this.htmlDoc.body().text();
		return bodyText.toLowerCase().contains(searchWord.toLowerCase());
	}

	public List<String> getLinks()
	{
		return this.links;
	}

}