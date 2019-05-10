package garbage;

public class Main {
	public static void main (String [] args) {
		if (args .length == 0 || args [0] == "client") {
			TwentyFourClient client = new TwentyFourClient ();
			client .run (); }
		else {
			TwentyFourServer server = new TwentyFourServer ();
			server .run (); } } }