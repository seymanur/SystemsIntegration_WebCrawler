package functions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class Parse {

    // Parsing the date-time
    public static XMLGregorianCalendar parseData(String ex) {
	int year, month, day, hour, minute;

	String[] split = ex.split("--");
	String first = split[0];
	String second = split[1];
	String[] date = first.split(" ");
	String monthSt = date[0];
	String[] timeAux = second.split(" ");
	String time = timeAux[2];
	hour = Integer.parseInt(time.substring(0, 2));
	minute = Integer.parseInt(time.substring(2, 4));
	day = Integer.parseInt(date[1].split(",")[0]);
	year = Integer.parseInt(date[2]);

	Date datex = null;
	try {
	    datex = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(monthSt);
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	Calendar cal = Calendar.getInstance();
	cal.setTime(datex);
	month = cal.get(Calendar.MONTH);
	//System.err.println(month);
	GregorianCalendar grecalendar = new GregorianCalendar(year, month, day,
		hour, minute);
	XMLGregorianCalendar xmlCalendar = null;
	try {
	    xmlCalendar = DatatypeFactory.newInstance()
		    .newXMLGregorianCalendar(grecalendar);
	} catch (DatatypeConfigurationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return xmlCalendar;
    }

    // parse author / journalist


    private ArrayList<String> parseAuthor(String aut) {
	String author = aut.replaceFirst("By ", "");
	String[] split = author.split(", ");
	for (int i = 0; i < split.length; i++) {
	    if (split[i].contains(" and ")) {
		split[i].split(" and ");
	    }
	}
	ArrayList<String> arraylist = new ArrayList<String>(
		Arrays.asList(split));

	return arraylist;

    }


}
