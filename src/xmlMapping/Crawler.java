package xmlMapping;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
    private ArrayList<String> parseAuthor(String aut)
    {
	String author = aut.replaceFirst("By ", "");
	String[] split = author.split(", ");
	for(int i =0; i<split.length; i++)
	{
	    if(split[i].contains(" and "))
	    {
		split[i].split(" and ");
	    }
	}
	ArrayList<String> arraylist = new ArrayList<String>(Arrays.asList(split));

	return arraylist;

    }
    private XMLGregorianCalendar parseData(String ex)
    {
	int year, month, day, hour, minute;

	String[] split = ex.split("--");
	String first =split[0];
	String second = split[1];
	String[] date = first.split(" ");
	String monthSt = date[0];
	String[] timeAux = second.split(" ");
	String time = timeAux[2];
	hour = Integer.parseInt(time.substring(0, 2));
	minute = Integer.parseInt(time.substring(2,4));
	day = Integer.parseInt(date[1].split(",")[0]);
	year = Integer.parseInt(date[2]);

	Date datex = null;
	try {
	    datex = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(monthSt);
	}
	catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	Calendar cal = Calendar.getInstance();
	cal.setTime(datex);
	month = cal.get(Calendar.MONTH);
	System.err.println(month);
	GregorianCalendar grecalendar = new GregorianCalendar(year, month, day, hour, minute);
	XMLGregorianCalendar xmlCalendar = null;
	try {
	    xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(grecalendar);
	}
	catch (DatatypeConfigurationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return xmlCalendar;
    }

    private void regionsParse() {
	String URL = "http://cnn.com";
	List<String> newsList = Arrays.asList("U.S.", "Africa", "Asia",
		"Europe", "Latin America", "Middle East");
	Document docAll, docReg;
	ArrayList<String> listOfNewsLink = new ArrayList<String>();
	String url;
	try {
	    docAll = Jsoup.connect(URL).userAgent("Mozilla").get();

	    // cnn_mtt1lftarea cnn_maintt2bul
	    Elements regions = (docAll.select("ul[id=intl-menu]"))
		    .select("a[href]");
	    for (Element element : regions) {
		if (newsList.contains(element.text())) {
		    // System.out.println(URL + element.attr("href"));
		    String region = URL + element.attr("href");
		    docReg = Jsoup.connect(URL + element.attr("href"))
			    .userAgent("Mozilla").get();

		    Elements lastnews = (docReg
			    .select("ul[class=cnn_bulletbin]").first())
			    .select("a[href]");
		    // newsListElement = doc.select("a[href^=/20");
		    for (Element news_cnn : lastnews) {

			if (news_cnn.attr("href").startsWith("/")) {
			    if (!(URL + news_cnn.attr("href")).contains("/video/")){
				listOfNewsLink.add(URL + news_cnn.attr("href"));
			    }

			} else {
			    if (!(news_cnn.attr("href")).contains("/video/")){
				listOfNewsLink.add(news_cnn.attr("href"));
			    }
			}

		    }

		}

	    }
	    for (int i =0; i<listOfNewsLink.size(); i++)
	    {
		System.out.println("News for : " + listOfNewsLink.get(i) );
		newsScraping(listOfNewsLink.get(i));

	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void newsScraping(String url) throws SocketTimeoutException
    {
	Document docAll, docPri, docReg, docNews = null;
	Elements newsListElement = null, journalists = null, photoslist = null, storyhighlightlist=null, regionsElement = null, newsEl = null;
	ArrayList<String> newsurl = new ArrayList<String>();
	ArrayList<String> allnewsurl = new ArrayList<String>();
	try {
	    docNews = Jsoup.connect(url).get();

	    newsEl = docNews.body().getElementsByClass("cnn_storyarea");

	    for (Element e : newsEl) {
		News tmpNews = new News();
		// Initialize the classes
		tmpNews.journalistList = new JournalistList();
		tmpNews.photoList = new PhotoList();
		tmpNews.storyhighlightsList = new StoryhighlightsList();

		// set journalist
		if(e.select(".cnnByline") != null)
		{
		    if(e.select(".cnnByline").text() != null)
		    {
			//System.out.println("author: " + newsEl.select(".cnnByline").first().getElementsByTag("strong").text());
			if( e.select(".cnnByline").first() != null){
			    if( e.select(".cnnByline").first().getElementsByTag("strong") != null)
			    {if( e.select(".cnnByline").first().getElementsByTag("strong").text() != null)
			    {
				journalists = newsEl.select(".cnnByline").first().getElementsByTag("strong");
				e.select(".cnnByline").first().getElementsByTag("strong").text();
				//tmpNews.journalistList.journalist = ;
				for (Element jour:journalists)
				{
				    tmpNews.getJournalistList().getJournalist().add(jour.text().replaceAll(",", ""));
				    System.out.println("author : " + jour.text().replaceAll(",", ""));
				}
			    }
			    }
			}}}
		//set location cnn_strycntntlft
		if(e.select("div[class=cnn_strycntntlft]>p").first()!= null) {
		    if(e.select("div[class=cnn_strycntntlft]>p").first().getElementsByTag("strong").text()!= null)
		    {
			tmpNews.location=e.select("div[class=cnn_strycntntlft]>p")
				.first().getElementsByTag("strong").text().replaceAll("(" +"CNN"+")", "");
		    }
		    System.out.println("location : " + tmpNews.getLocation());
		}

		//set photoList
		if(e.select("div[class=cnnArticleGalleryPhotoContainer]")!=null)
		{
		    photoslist = e.select("div[class=cnnArticleGalleryPhotoContainer]");
		    System.out.println("galery image:");
		    for (Element photo: photoslist)
		    {
			System.out.println(photo.select("img").first().attr("src"));
			tmpNews.getPhotoList().getPhoto().add(photo.select("img").first().attr("src"));
		    }
		    // check photos list
		    for (int j = 0; j<photoslist.size(); j++)
		    {
			System.out.println("photos list : " + tmpNews.getPhotoList().getPhoto().get(j));
		    }
		}
		// set storyhighlight

		storyhighlightlist = e.select("ul[class=cnn_bulletbin cnnStryHghLght]").select("li");
		if(storyhighlightlist!=null)
		{
		    System.out.println("storyhighlights : ");
		    for(Element sh :storyhighlightlist)
		    {
			System.out.println();
			tmpNews.getStoryhighlightsList().getStoryhighlight().add(sh.text());
		    }

		    // check storyhighlights list
		    for (int k = 0; k<storyhighlightlist.size(); k++)
		    {
			System.out.println("story hig : " +tmpNews.getStoryhighlightsList().getStoryhighlight().get(k));
		    }
		}

		// set title
		if(e.childNodeSize()>0){if(e.childNodeSize()>1){if(e.childNodeSize()>2){if(e.childNodeSize()>3)
		{
		    System.out.println("child size : " + e.childNodeSize() );

		    if(e.child(3).hasText()){
			tmpNews.setTitle(e.child(3).text());
			//String ttl = tmpNews.getTitle()
			System.out.println("title : " + tmpNews.getTitle());
		    }}}}}
		//set video url
		if(e.select("div[class=OUTBRAIN]").attr("data-src")!=null)
		{
		    tmpNews.setVideourl(e.select("div[class=OUTBRAIN]").attr("data-src"));
		    System.out.println("video : " + tmpNews.getVideourl());
		}
		//set text
		if(e.select(".cnn_strycntntlft p.cnn_storypgraphtxt").text()!=null)
		{
		    e.select(".cnn_strycntntlft p.cnn_storypgraphtxt").text();
		    tmpNews.setText(e.select(".cnn_strycntntlft p.cnn_storypgraphtxt ").text());
		    System.out.println("text: " + tmpNews.getText());
		}

		//get date and time 2014-10-24-19:24 long
		System.out.println("datetime : "+ e.select(".cnn_strytmstmp").text());

		//set date-time
		tmpNews.setDatetime(parseData(e.select(".cnn_strytmstmp").text()));
		System.out.println("xmltime : " + tmpNews.getDatetime());

		// set
		//newsList.getNews().add(tmpNews);
	    }
	}
	catch (IOException e1 ) {

	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	    System.out.println(" Error");
	}
    }
    public static void main(String[] args) {
	new Crawler().init();

    }
    private void init() {


    }
}
