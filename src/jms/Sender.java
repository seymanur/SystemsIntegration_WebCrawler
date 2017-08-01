package jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import xmlMapping.Cnn;

public class Sender {
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageProducer messageProducer;

    public Sender(String user, String pass)
    {
	InitialContext init;
	try {
	    init = new InitialContext();
	    this.connectionFactory = (ConnectionFactory) init.lookup("jms/RemoteConnectionFactory");
	    this.destination = (Destination) init.lookup("jms/topic/test");
	    this.connection = this.connectionFactory.createConnection("seyma", "Seyma_0101");
	    this.connection.start();
	    this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	    this.messageProducer = this.session.createProducer(this.destination);
	}
	catch (NamingException | JMSException e) {
	    // TODO Auto-generated catch block
	    System.out.println("Jboss connection is not successful!");
	    e.printStackTrace();
	}
    }

    public void send(Cnn cnn) throws JMSException
    {
	ObjectMessage obMessage = this.session.createObjectMessage();
	obMessage.setObject(cnn);
	//TextMessage textMessage = this.session.createTextMessage(string);
	this.messageProducer.send(obMessage);
	this.connection.close();
    }

    public void sendMessage(String string) throws JMSException {
	TextMessage tm = this.session.createTextMessage(string);
	this.messageProducer.send(tm);
	this.connection.close();
    }
}
