package project;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;

import jms.Receiver;
import xmlMapping.Cnn;
import functions.Functions;

public class HTMLSummaryCreator extends Receiver {
    private String dirXSL = "src/files/newscnn.xsl";
    private String dirOutput="html_generated";

    public HTMLSummaryCreator()
    {
	super("seyma", "Seyma_0101", "olala");
    }
    /*
    public boolean validateXML(String xml) {
	boolean ret = true;
	try {
	    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	    Schema schema = schemaFactory.newSchema(new File("newscnn.xsd"));
	    Validator validator = schema.newValidator();
	    validator.validate(new StreamSource(new StringReader(xml)));
	}
	catch (SAXException | IOException e) {
	    System.err.println(e.getMessage());
	    ret = false;
	}
	return ret;
    } */




    public void saveFile(String message, String filename)
    {
	// java 7 autoclosable
	try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
	    writer.write (message);
	    System.out.println("HTML file was created succesfully");
	}
	catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Override
    public void onMessage(Message message)
    {
	//TextMessage tmsg = (TextMessage) message;
	Cnn cnnall;
	String dir = "D:\\Seyma\\workspace\\Project_01_IS\\src\\files\\test.xml";
	try {
	    cnnall = (Cnn) ((ObjectMessage)message).getObject();
	    Functions.marshallCnn(dir, cnnall);
	    System.out.println("Marshall - done");
	    // check the XML validation
	    System.out.println();
	    Functions.validateXML("newscnn.xsd", dir);
	    System.out.println("xml validated");
	}
	catch (JMSException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) throws IOException, JMSException
    {
	HTMLSummaryCreator creator = new HTMLSummaryCreator();
	System.in.read();
	creator.close();
    }

}
