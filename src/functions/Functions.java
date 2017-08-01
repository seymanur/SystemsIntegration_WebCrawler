package functions;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.GregorianCalendar;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import xmlMapping.Cnn;

public class Functions {


    /**
     * Gets current XMLGregorianCalendar
     * @param dirXML
     * @return news object
     */

    public static XMLGregorianCalendar getXMLGregorianCalendarNow()
	    throws DatatypeConfigurationException
    {
	GregorianCalendar gregorianCalendar = new GregorianCalendar();
	DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
	XMLGregorianCalendar now =
		datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
	return now;
    }


    /**
     * Unmarshalls news
     * @param dirXML
     * @return news object
     */
    public static Cnn Unmarshalls(String dirXML) {
	Cnn cnn = new Cnn();
	try {
	    File file = new File(dirXML);
	    JAXBContext jaxbContext = JAXBContext.newInstance(Cnn.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	    cnn = (Cnn) jaxbUnmarshaller.unmarshal(file);
	} catch (JAXBException e) {
	}
	return cnn;
    }

    /**
     * Unmarshalls cnn
     * @param source XML in string format
     * @return cnn object
     */
    public static Cnn Unmarshalls(Source source) {
	Cnn cnn = null;
	try {
	    JAXBContext jaxbContext = JAXBContext.newInstance(Cnn.class);
	    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	    cnn = (Cnn) jaxbUnmarshaller.unmarshal(source);
	} catch (JAXBException e) {
	}
	return cnn;
    }

    /**
     * Marshalls cnn
     * @param movies object
     * @return XML in string format
     */

    public static String marshallsOb(Cnn cnn) {
	StringWriter sw = new StringWriter();
	try {
	    JAXBContext jaxbContext = JAXBContext.newInstance(Cnn.class);
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    jaxbMarshaller.marshal(cnn, sw);
	} catch (JAXBException e) {
	}
	return sw.toString();
    }


    /**
     * Marshalls cnn
     * @param dirFile
     * @param cnn
     */
    public static void marshallCnn(String dirFile, Cnn cnn) {
	try {
	    File file = new File(dirFile);
	    if (file.exists()) {
		file.delete();
	    }
	    JAXBContext jaxbContext = JAXBContext.newInstance(Cnn.class);
	    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    jaxbMarshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders",
		    "<?xml-stylesheet type='text/xsl' href='newscnn.xsl'?>");
	    jaxbMarshaller.marshal(cnn, file);
	} catch (JAXBException e) {
	}
    }


    public static boolean validateXML(String schemaFileName, String xmlFileName)
    {
	boolean val = true;
	try {
	    Source schemaFile = new StreamSource(new File(schemaFileName));
	    Source xmlFile = new StreamSource(xmlFileName);
	    SchemaFactory schemaFactory = SchemaFactory
		    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
	    Schema schema = schemaFactory.newSchema(schemaFile);
	    Validator validator = schema.newValidator();

	    val = true;
	    validator.validate(xmlFile);
	    System.out.println(xmlFile.getSystemId() + " is valid");
	} catch (SAXException | IOException e) {
	    val = false;
	    System.out.println(xmlFileName + " is NOT valid");
	    System.out.println("Reason: " + e.getLocalizedMessage());
	}
	return val;
    }


}
