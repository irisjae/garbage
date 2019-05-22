package garbage.fish;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;

public class MessageReceiver extends MessageManager {
	MessageConsumer _consumer;

	public MessageReceiver (String destination, String factory) throws FishException {
		this ((Destination) Lookup .thing (destination), (ConnectionFactory) Lookup .thing (factory)); }
	public MessageReceiver (Destination destination, ConnectionFactory factory) {
		super (destination, factory); }

	MessageConsumer consumer () throws FishException {
		try {
			if (this ._consumer == null) {
				this ._consumer = this .session () .createConsumer (this .destination); }
			return this ._consumer; }
		catch (JMSException e) {
			throw new FishException (e); } }
	
	public String receive () throws FishException {
		try {
			TextMessage sessionMessage = (TextMessage) this .consumer () .receive ();
			return sessionMessage .getText (); } 
		catch (JMSException e) {
			throw new FishException (e); } } }
