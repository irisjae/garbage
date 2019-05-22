package garbage.gameplay;

import java.util.List;

public enum TwentyFourGameplayProtocol {
	WAIT, HYPERWAIT, JOINED,
	QUIT, LEFT,
	ATTEMPT, FAILED, WON;

	static String requestQueue = "jms/JPoker24GameQueue";
	static String requestFactory = "jms/JPoker24GameConnectionFactory";
	static String responseTopic = "jms/JPoker24GameTopic";
	static String responseFactory = "jms/JPoker24GameConnectionFactory"; 

	static String request (TwentyFourGameplayProtocol request, String data) {
		return request + " " + data; }
	static String request (TwentyFourGameplayProtocol request, List <String> data) {
		return request + " " + String .join (" ", data); }

	static String response (TwentyFourGameplayProtocol response, String data) {
		return response + " " + data; }
	static String response (TwentyFourGameplayProtocol response, List <String> list) {
		return response + " " + String .join (" ", list); } }
