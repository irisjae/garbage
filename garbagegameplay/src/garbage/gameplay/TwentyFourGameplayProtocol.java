package garbage.gameplay;

public enum TwentyFourGameplayProtocol {
	WAIT, HYPERWAIT, JOINED,
	QUIT, LEFT,
	ATTEMPT, FAILED, WON;

	static String requestQueue = "jms/JPoker24GameConnectionFactory";
	static String requestFactory = "jms/QueueConnectionFactory";
	static String responseTopic = "jms/JPoker24GameConnectionFactory";
	static String responseFactory = "jms/QueueConnectionFactory"; 

	static String request (TwentyFourGameplayProtocol request, String ... data) {
		return request + " " + String .join (" ", data); }
	
	static String response (TwentyFourGameplayProtocol response, String ... data) {
		return response + " " + String .join (" ", data); } }
