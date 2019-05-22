package garbage.fish;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

public class MessageInterrupt {
	public static void timeout (int delay, MessageManager manager, String message) {
		try {
			MessageInterrupt .timeout (delay, manager .destination, manager .session (), message); }
		catch (FishException e) {
            e .printStackTrace (); } }
	public static void timeout (int delay, Destination destination, Session session, String message) {
		new Thread (() -> {
	        try (MessageProducer sender = session .createProducer (destination)) {
	            Thread .sleep (delay);
            	sender .send (session .createTextMessage (message)); }
	        catch (JMSException | InterruptedException e) {
	            e .printStackTrace (); } })
		.start(); } }