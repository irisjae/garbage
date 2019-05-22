package garbage.fish;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

public class MessageSender extends MessageManager {
	MessageProducer _producer;

	public MessageSender (String destination, String factory) throws FishException {
		this ((Destination) Lookup .thing (destination), (ConnectionFactory) Lookup .thing (factory)); }
	public MessageSender (Destination destination, ConnectionFactory factory) {
		super (destination, factory); }
	
	MessageProducer producer () throws FishException {
		try {
			if (this ._producer == null) {
				this ._producer = this .session () .createProducer (this .destination); }
			return this ._producer; }
		catch (JMSException e) {
			throw new FishException (e); } }
	
	public void send (String message) throws FishException {
		try {
			System .out .println ("sending: " + message);
			TextMessage sessionMessage = this .session () .createTextMessage (message);
			this .producer () .send (sessionMessage); }
		catch (JMSException e) {
			throw new FishException (e); } } }