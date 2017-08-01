package jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Receiver implements MessageListener {
    private ConnectionFactory cf;
    private Connection c;
    private Session s;
    private Destination d;
    private MessageConsumer mc;

    public Receiver(String user, String pass, String id)
    {
	InitialContext init;
	try {
	    init = new InitialContext();
	    this.cf = (ConnectionFactory) init.lookup("jms/RemoteConnectionFactory");
	    this.d = (Destination) init.lookup("jms/topic/test");
	    this.c=cf.createConnection(user, pass);
	    this.c.setClientID(id);
	    this.c.start();
	    this.s = this.c.createSession(false, Session.AUTO_ACKNOWLEDGE);
	    mc = s.createDurableSubscriber((Topic) this.d, id);
	    mc.setMessageListener(this);
	}
	catch (NamingException | JMSException e) {
	    // TODO Auto-generated catch block
	    System.out.println("Jboss doesn't work");
	    e.printStackTrace();
	}
    }

    @Override
    public void onMessage(Message arg0) {
	// TODO Auto-generated method stub
    }

    protected void close() throws JMSException
    {
	this.c.close();
    }
}
