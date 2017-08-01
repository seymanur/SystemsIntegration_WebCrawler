package xmlMapping;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.jms.JMSException;

import jms.Sender;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import functions.Parse;

public class WebCrawler {

    private static String site;
    private static Cnn regionList;
    private static Region newsList;
    private static News news;

    /**
     * Construtor
     *
     * @throws JMSException
     */
    public WebCrawler() {

    }

    public Cnn screenScraping() throws IOException {

	Document docAll, docReg, docNews = null;
	Elements journalists = null, photoslist = null, storyhighlightlist = null, newsEl = null;
	ArrayList<String> newsurl = new ArrayList<String>();
	ArrayList<String> allnewsurl = new ArrayList<String>();
	regionList = new Cnn();


	String URL = "http://cnn.com";
	List<String> newsListRegion = Arrays.asList("U.S.", "Africa", "Asia","Europe", "Latin America", "Middle East");

	String url = null;
	String region = null;

	try {
	    docAll = Jsoup.connect(URL).userAgent("Mozilla").get();

	    // cnn_mtt1lftarea cnn_maintt2bul
	    Elements regions = (docAll.select("ul[id=intl-menu]")).select("a[href]");
	    for (Element element : regions) {
		if (newsListRegion.contains(element.text())) {
		    //System.out.println(URL + element.attr("href"));
		    docReg = Jsoup.connect(URL + element.attr("href")).userAgent("Mozilla").get();

		    region = element.attr("href").substring(1, element.attr("href").length() - 1);

		    Elements lastnews = (docReg.select("ul[class=cnn_bulletbin]").first()).select("a[href]");

		    newsList = new Region();
		    newsList.setId(region);

		    for (Element news_cnn : lastnews) {

			if (news_cnn.attr("href").startsWith("/")) {
			    url = "";
			    if (!(URL + news_cnn.attr("href")).contains("/video/")) {
				url = URL + news_cnn.attr("href");
			    }

			} else {
			    url = "";
			    if (!(news_cnn.attr("href")).contains("/video/")) {
				url = news_cnn.attr("href");
			    }
			}

			try {
			    docNews = Jsoup.connect(url).userAgent("Mozilla").timeout(0)
				    .get();

			    newsEl = docNews.body().getElementsByClass(
				    "cnn_storyarea");
			    //System.out.println(url);

			    for (Element e : newsEl) {
				News tmpNews = new News();
				// Initialize the classes
				tmpNews.setNewsurl(url);
				tmpNews.journalistList = new JournalistList();
				tmpNews.photoList = new PhotoList();
				tmpNews.storyhighlightsList = new StoryhighlightsList();

				tmpNews.setId(new BigInteger("123456"));
				// set journalist
				if (e.select(".cnnByline") != null) {
				    if (e.select(".cnnByline").text() != null) {
					if (e.select(".cnnByline").first() != null) {
					    if (e.select(".cnnByline").first().getElementsByTag("strong") != null) {
						if (e.select(".cnnByline").first().getElementsByTag("strong").text() != null) {
						    journalists = newsEl.select(".cnnByline").first().getElementsByTag("strong");
						    e.select(".cnnByline").first().getElementsByTag("strong").text();
						    // tmpNews.journalistList.journalist
						    // = ;
						    for (Element jour : journalists) {
							tmpNews.getJournalistList().getJournalist().add(jour.text().replaceAll(",",""));
							//System.out.println("author : "+ jour.text().replaceAll(",",""));
						    }
						}
					    }
					}
				    }
				}
				// set location cnn_strycntntlft
				if (e.select("div[class=cnn_strycntntlft]>p").first() != null) {
				    if (e.select("div[class=cnn_strycntntlft]>p").first().getElementsByTag("strong").text() != null) {
					String local = e.select("div[class=cnn_strycntntlft]>p").first().getElementsByTag("strong").text();
					local = local.substring(0, local.indexOf("(CNN)"));
					tmpNews.location = local;
				    }
				    //System.out.println("location : "+ tmpNews.getLocation());
				}

				// set photoList
				if (e.select("div[class=cnnArticleGalleryPhotoContainer]") != null) {
				    photoslist = e.select("div[class=cnnArticleGalleryPhotoContainer]");
				    //System.out.println("galery image:");
				    for (Element photo : photoslist) {
					//System.out.println(photo.select("img").first().attr("src"));
					tmpNews.getPhotoList().getPhoto().add(photo.select("img").first().attr("src"));
				    }

				    // check photos list
				    /*
									for (int j = 0; j < photoslist.size(); j++) {
										System.out.println("photos list : " + tmpNews.getPhotoList().getPhoto().get(j));
									}
				     */
				}
				// set storyhighlight

				storyhighlightlist = e.select("ul[class=cnn_bulletbin cnnStryHghLght]").select("li");
				if (storyhighlightlist != null) {
				    //System.out.println("storyhighlights : ");
				    for (Element sh : storyhighlightlist) {
					//System.out.println();
					tmpNews.getStoryhighlightsList().getStoryhighlight().add(sh.text());
				    }

				    // check storyhighlights list
				    /*
									for (int k = 0; k < storyhighlightlist.size(); k++) {
										System.out.println("story hig : " + tmpNews.getStoryhighlightsList().getStoryhighlight().get(k));
									}
				     */
				}

				// set title
				tmpNews.setTitle(e.select("h1").first().text());

				// set video url
				if (e.select("div[class=OUTBRAIN]").attr("data-src") != null) {
				    tmpNews.setVideourl(e.select("div[class=OUTBRAIN]").attr("data-src"));
				    //System.out.println("video : " + tmpNews.getVideourl());
				}

				// set text
				if (e.select(".cnn_strycntntlft p").text() != null) {
				    String text = e.select( ".cnn_strycntntlft p").text();
				    text = text.substring(text.indexOf("(CNN) -- ") + 9);
				    //System.out.println("text: " + text);
				}

				// get date and time 2014-10-24-19:24 lonlg
				//System.out.println("datetime : "+ e.select(".cnn_strytmstmp").text());

				// set date-time
				tmpNews.setDatetime(Parse.parseData(e.select(".cnn_strytmstmp").text()));
				System.out.println("xmltime : " + tmpNews.getDatetime());

				// set
				newsList.getNews().add(tmpNews);
			    }
			} catch (Exception e) {
			    // System.out.println("error - " + url);
			}
		    }

		    regionList.getRegion().add(newsList);
		}
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return regionList;
    }



    private void init() {
	try {
	    Sender send;

	    //Functions.marshallsOb(screenScraping());
	    send = new Sender("seyma", "Seyma_0101"); //user, pass

	    ///System.out.println(xml);
	    //sendMessage(xml);
	    send.send(screenScraping());
	    System.out.println("Message has been sent succesfully");


	} catch (IOException | JMSException e) {
	    // TODO Auto-generated catch block
	    System.out.println("Not able to send message");

	    e.printStackTrace();
	}
    }

    private void sendMessage(String xml) {
	// TODO Auto-generated method stub

    }


    public static void main(String[] args) {
	new WebCrawler().init();

    }

}