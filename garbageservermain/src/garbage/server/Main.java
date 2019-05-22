package garbage.server;

public class Main {
	public static void main (String [] args) {
        Thread .setDefaultUncaughtExceptionHandler ((Thread t, Throwable e) -> {
        	e .printStackTrace ();
        	System .exit (1); });
		try {
			/*if (args .length == 0 || args [0] == "client") {
				(new TwentyFourClient ()) .run (); }
			else {*/
				(new TwentyFourServer ()) .run (); } /*}*/
		catch (Exception e) {
			e .printStackTrace (); } } }