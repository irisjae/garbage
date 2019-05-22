package garbage.fish;

import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Lookup {
	static Context context;
	public static Object thing (String name) throws FishException {
		try {
			if (Lookup .context == null) {
				Lookup .context = new InitialContext (); }
			return Lookup .context .lookup (name); }
		catch (NamingException e) {
			throw new FishException (e); } }

	public static Queue queue (String name) throws FishException {
		return (Queue) Lookup .thing (name); }
	public static QueueConnectionFactory queueFactory (String name) throws FishException {
		return (QueueConnectionFactory) Lookup .thing (name); }
	public static Topic topic (String name) throws FishException {
		return (Topic) Lookup .thing (name); }
	public static TopicConnectionFactory topicFactory (String name) throws FishException {
		return (TopicConnectionFactory) Lookup .thing (name); }	
	}
