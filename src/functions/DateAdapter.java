package functions;



import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

public class DateAdapter extends XmlAdapter<String, XMLGregorianCalendar> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    private static Date toDate(XMLGregorianCalendar calendar){
	if(calendar == null) {
	    return null;
	}
	return calendar.toGregorianCalendar().getTime();
    }



    @Override
    public String marshal(XMLGregorianCalendar v) throws Exception {
	System.out.println("Entrei marshall " + v);
	String yolo = null;
	try {
	    yolo =  dateFormat.format(toDate(v));
	} catch (Exception e) {
	    e.printStackTrace();
	}
	System.out.println("Corri Marshal fixe " + yolo);
	return yolo;
    }

    @Override
    public XMLGregorianCalendar unmarshal(String v) throws Exception {
	System.out.println("Entrei unmarshall");
	XMLGregorianCalendar asd =  Parse.parseData(v);
	System.out.println("Corri unmarshall fixe");
	return asd;
    }

}

