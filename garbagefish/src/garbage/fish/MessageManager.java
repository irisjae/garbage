package garbage.fish;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

public abstract class MessageManager {
	Destination destination;
	ConnectionFactory factory;
	
	Session _session;
	
	public MessageManager (Destination destination, ConnectionFactory factory) {
		this .destination = destination;
		this .factory = factory; }
	
	Session session () throws FishException {
		try {
			if (this ._session == null) {
				Connection connection = this .factory .createConnection ();
				this ._session = connection .createSession (false, Session .AUTO_ACKNOWLEDGE); }
			return this ._session; }
		catch (JMSException e) {
			throw new FishException (e); } } }