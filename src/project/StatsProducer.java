package project;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.naming.NamingException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;

import jms.Receiver;
import xmlMapping.Cnn;
import functions.Functions;


public class StatsProducer extends Receiver {
    private String dirFile = "src/files/last12.xml"; //file which contains last 12 hours news
    //number of top movies

    public StatsProducer() throws NamingException, JMSException {
	super("seyma", "Seyma_0101", "bla");	//user, pass, id
    }

    @Override
    public void onMessage(Message message) {
	Cnn cnnStats;
	String dir = "\\files\\statsProducer.xml";
	try {
	    cnnStats = (Cnn) ((ObjectMessage)message).getObject();
	    lastNews(cnnStats);
	    Functions.marshallCnn(dir, cnnStats);
	    System.out.println("Marshall - done");
	    // check the XML validation
	    Functions.validateXML("\\files\\newscnn.xsd", dir);
	    System.out.println("xml validated");
	}
	catch (JMSException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	/*//TextMessage tmsg = (TextMessage) msg;
	try {
	    //get String XML and unmoarshall top Movies object
	    //StringReader reader = new StringReader(tmsg.getText());
	    //Source source = new StreamSource(reader);
	    //Cnn cnn = Functions.Unmarshalls(source);
	    // get the last 12 hours news
	    // lastNews(cnn);
	} catch (JMSException e) {
	    e.printStackTrace();
	}*/
    }


    public void lastNews(Cnn cnn)
    {
	int hours = 0;
	int numberOfLast = 0;
	DatatypeFactory dtf = new DatatypeFactory() {

	    @Override
	    public XMLGregorianCalendar newXMLGregorianCalendar(BigInteger year, int month, int day, int hour, int minute,
		    int second, BigDecimal fractionalSecond, int timezone) {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public XMLGregorianCalendar newXMLGregorianCalendar(GregorianCalendar cal) {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public XMLGregorianCalendar newXMLGregorianCalendar(String lexicalRepresentation) {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public XMLGregorianCalendar newXMLGregorianCalendar() {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public Duration newDuration(boolean isPositive, BigInteger years, BigInteger months, BigInteger days,
		    BigInteger hours, BigInteger minutes, BigDecimal seconds) {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public Duration newDuration(long durationInMilliSeconds) {
		// TODO Auto-generated method stub
		return null;
	    }

	    @Override
	    public Duration newDuration(String lexicalRepresentation) {
		// TODO Auto-generated method stub
		return null;
	    }
	};
	for (int i = 0; i<cnn.getRegion().size(); i++)
	{
	    int j = 0;
	    while( j<cnn.getRegion().get(i).getNews().size())
	    {
		try {
		    XMLGregorianCalendar xmlCalendar = cnn.getRegion().get(i).getNews().get(j).getDatetime();

		    if(dtf.newDuration(Functions.getXMLGregorianCalendarNow().toGregorianCalendar().getTimeInMillis() - xmlCalendar.toGregorianCalendar().getTimeInMillis()).getHours()>0)
		    {
			hours = dtf.newDuration(Functions.getXMLGregorianCalendarNow().toGregorianCalendar().getTimeInMillis() - xmlCalendar.toGregorianCalendar().getTimeInMillis()).getHours();
			if(hours>12)
			{

			    cnn.getRegion().get(i).getNews().remove(j);
			    numberOfLast ++;
			}
			else
			{
			    j++;
			}
		    }}
		catch (DatatypeConfigurationException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

	    }


	}
    }

    public static void main(String[] args)  {
	StatsProducer r;
	try {
	    r = new StatsProducer();
	    System.in.read();
	    r.close();
	}
	catch (NamingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	catch (JMSException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
}

