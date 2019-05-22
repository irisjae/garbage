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
import garbage.UserStat;
import garbage.fish.FishException;
import garbage.gameplay.TwentyFourGameplayClient;
import garbage.gameplay.TwentyFourGameplayPlayer;

public class TwentyFourClient implements Runnable {
	TwentyFourProtocol _protocol;
	
	Signal <Optional <TwentyFourGameplayClient>> _gameplayClient = new Signal (Optional .empty ());	

	Signal <String> panel = new Signal ();
	Signal <Optional <Session>> session = new Signal (Optional .empty ());
	Signal <Optional <UserStat>> userStat = new Signal (Optional .empty ());
	
	TwentyFourProtocol protocol () throws RemoteException, MalformedURLException, NotBoundException {
		try {
			this ._protocol .ping (); }
		catch (Exception e) {
			this ._protocol = (TwentyFourProtocol) Naming .lookup("//localhost/TwentyFour"); }
		return this ._protocol; }	
	TwentyFourGameplayClient gameplay () throws FishException {
		if (! this ._gameplayClient .show () .isPresent ()) {
			TwentyFourClient client = this;
			
			Session session = client .session .show () .get ();
			TwentyFourGameplayPlayer player = TwentyFourGameplayPlayer .of (session .loginName);
			client ._gameplayClient .emit (Optional .of (new TwentyFourGameplayClient (player)));
			Thread thread = new Thread (client ._gameplayClient .show () .get ());
			thread .start ();
			
			Signal <Boolean> done = new Signal (false);
			Reactive .watch (() -> {
				if (! done .show ()) {
					if (! client .session .mark () .isPresent ()) {
						thread .interrupt ();
						done .emit (true); } } } ); }
		return this ._gameplayClient .show () .get (); }
	
	
	@Override
	public void run() {
		TwentyFourClientFrame .from (this) .setVisible (true); } }