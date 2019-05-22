package garbage.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Optional;

import javax.naming.NamingException;

import garbage.Session;
import garbage.Signal;
import garbage.Reactive;
import garbage.TwentyFourProtocol;
import garbage.UserInfo;
import garbage.fish.FishException;
import garbage.gameplay.TwentyFourGameplayClient;
import garbage.gameplay.TwentyFourGameplayPlayer;

public class TwentyFourClient implements Runnable {
	TwentyFourProtocol _protocol;
	TwentyFourGameplayClient _gameplayClient;	

	Signal <String> panel = new Signal <String> ();
	Signal <Optional <Session>> session = new Signal (Optional .empty ());
	Signal <Optional <UserInfo>> userInfo = new Signal (Optional .empty ());
	
	TwentyFourProtocol protocol () throws RemoteException, MalformedURLException, NotBoundException {
		try {
			this ._protocol .ping (); }
		catch (Exception e) {
			this ._protocol = (TwentyFourProtocol) Naming .lookup("//localhost/TwentyFour"); }
		return this ._protocol; }	
	
	
	@Override
	public void run() {
		TwentyFourClient client = this;
		Reactive .watch (() -> {
			client .session .mark ()
			.ifPresent (session -> {
				try {
					TwentyFourGameplayPlayer player = TwentyFourGameplayPlayer .of (session .loginName);
					client ._gameplayClient = new TwentyFourGameplayClient (player);
					Thread thread = new Thread (client ._gameplayClient);
					thread .start ();
					Reactive .cleanup (() -> { thread .interrupt (); });
					}
				catch (FishException e) {
					e .printStackTrace (); } }); } );
		
		TwentyFourClientFrame .from (this) .setVisible (true); } }