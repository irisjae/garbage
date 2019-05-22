package garbage.client;

import java.awt.GridLayout;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import garbage.Continuation;
import garbage.ProtocolResultHandler;
import garbage.Reactive;
import garbage.Signal;
import garbage.fish.FishException;
import garbage.gameplay.TwentyFourGameplayClient;
import garbage.gameplay.TwentyFourGameplayProtocol;

public class TwentyFourClientPlayPanel {	
	static JPanel from (TwentyFourClient client) {
		JPanel panel = new JPanel ();
		
		JPanel topPanel = new JPanel ();
		JButton userProfileButton = new JButton ("User Profile");
		JButton playGameButton = new JButton ("Play Game");
		JButton leaderBoardButton = new JButton ("Leader Board");
		JButton logoutButton = new JButton ("Logout");
		JButton newGameButton = new JButton ("New game");

		panel .setLayout (new GridLayout (0, 1));
		panel .add (topPanel);
		panel .add (newGameButton);
		topPanel .setLayout (new GridLayout (1, 0));
		topPanel .add (userProfileButton);
		topPanel .add (playGameButton);
		topPanel .add (leaderBoardButton);
		topPanel .add (logoutButton);

		Continuation continuation = Continuation .empty ();
		Reactive .watch (() -> {			
			client ._gameplayClient .mark ()
			.ifPresent (gameplay -> {
				if (gameplay .state .mark () == TwentyFourGameplayProtocol .LEFT) {
					continuation .unwind (); } 
				else if (gameplay .state .mark () == TwentyFourGameplayProtocol .WAIT) {
					continuation .flush ();
			    	client .panel .emit ("wait"); } }); });
		newGameButton .addActionListener (__ -> {
			if (continuation .lock ()) {
				try {
					client .gameplay ()
					.waiting (); }
				catch (Exception e) {
		    		JOptionPane .showMessageDialog (panel, e .getMessage (), "Error", JOptionPane .ERROR_MESSAGE);
					e .printStackTrace (); } } });
		userProfileButton .addActionListener (__ -> {
			if (! continuation .blocking ()) {
		    	client .panel .emit ("profile"); } });
		leaderBoardButton .addActionListener (__ -> {
			if (! continuation .blocking ()) {
				client .panel .emit ("leaderboard"); } });
		logoutButton .addActionListener (__ -> {
			if (! continuation .blocking ()) {
		    	try {
		    		client .protocol ()
	    			.logout (client .session .show () .get ())
	    			.handle (ProtocolResultHandler .of (
	    				___ -> {
	    					client .session .emit (Optional .empty ()); },
	    				error -> {
							JOptionPane .showMessageDialog (panel, error .error, "Error", JOptionPane .ERROR_MESSAGE); } ) ); }
		    	catch (Exception e) {
		    		JOptionPane .showMessageDialog (panel, e .getMessage (), "Error", JOptionPane .ERROR_MESSAGE);
		    		e .printStackTrace (); } } });
		
		return panel; } }