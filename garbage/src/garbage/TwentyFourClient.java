package garbage;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Optional;

public class TwentyFourClient implements Runnable {

	TwentyFourProtocol _protocol;

	Reactive <String> panel = new Reactive <String> ();
	Reactive <Optional <Session>> session = new Reactive <Optional <Session>> (Optional .empty ());
	Reactive <Optional <String>> userName = new Reactive <Optional <String>> (Optional .empty ());
	Reactive <Optional <Integer>> userGamesCount = new Reactive <Optional <Integer>> (Optional .empty ());
	Reactive <Optional <Integer>> userWinsCount = new Reactive <Optional <Integer>> (Optional .empty ());
	Reactive <Optional <Float>> userWinTimesAverage = new Reactive <Optional <Float>> (Optional .empty ());
	Reactive <Optional <Integer>> userRank = new Reactive <Optional <Integer>> (Optional .empty ());
	
	public TwentyFourClient () {
		var client = this;
		
		client .session .listen (maybeSession -> {
			maybeSession .ifPresentOrElse (
				session -> {
					client .userName .emit (Optional .of (session .loginName)); },
				() -> {
					client .userName .emit (Optional .empty ());
					client .userGamesCount .emit (Optional .empty ());
					client .userWinsCount .emit (Optional .empty ());
					client .userWinTimesAverage .emit (Optional .empty ());
					client .userRank .emit (Optional .empty ()); }); }); }
	
	
	TwentyFourProtocol protocol () throws RemoteException, MalformedURLException, NotBoundException {
		try {
			this ._protocol .ping (); }
		catch (Exception e) {
			this ._protocol = (TwentyFourProtocol) Naming .lookup("//localhost/TwentyFour"); }
		return this ._protocol; }	
	
	
	@Override
	public void run() {
		TwentyFourClientFrame .from (this) .setVisible (true); } }