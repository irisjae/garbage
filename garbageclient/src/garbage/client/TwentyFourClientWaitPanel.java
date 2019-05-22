package garbage.client;

import java.awt.GridLayout;
import java.util.Optional;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import garbage.Continuation;
import garbage.ProtocolResultHandler;
import garbage.Reactive;
import garbage.Signal;
import garbage.gameplay.TwentyFourGameplayClient;
import garbage.gameplay.TwentyFourGameplayProtocol;

public class TwentyFourClientWaitPanel {	
	static JPanel from (TwentyFourClient client) {
		JPanel panel = new JPanel ();
		
		JPanel topPanel = new JPanel ();
		JButton userProfileButton = new JButton ("User Profile");
		JButton playGameButton = new JButton ("Play Game");
		JButton leaderBoardButton = new JButton ("Leader Board");
		JButton logoutButton = new JButton ("Logout");
		JLabel waitingLabel = new JLabel ("Waiting for players...");

		panel .setLayout (new GridLayout (0, 1));
		panel .add (topPanel);
		panel .add (waitingLabel);
		topPanel .setLayout (new GridLayout (1, 0));
		topPanel .add (userProfileButton);
		topPanel .add (playGameButton);
		topPanel .add (leaderBoardButton);
		topPanel .add (logoutButton);

		Continuation continuation = Continuation .empty ();
		Reactive .watch (() -> {
			client ._gameplayClient .mark ()
			.ifPresent (gameplay -> {
				if (gameplay .state .mark () == TwentyFourGameplayProtocol .JOINED) {
					continuation .unwind ();
			    	client .panel .emit ("game"); } 
				else if (gameplay .state .mark () == TwentyFourGameplayProtocol .LEFT) {
					continuation .flush (); } }); });
		userProfileButton .addActionListener (__ -> {
			if (! continuation .blocking () ) {
				try {
					client .gameplay ()
					.quiting ();
					continuation .block (() -> {
						client .panel .emit ("profile"); }); }
				catch (Exception e) {
		    		JOptionPane .showMessageDialog (panel, e .getMessage (), "Error", JOptionPane .ERROR_MESSAGE);
		    		e .printStackTrace (); } } });
		leaderBoardButton .addActionListener (__ -> {
			if (! continuation .blocking () ) {
				try {
					client .gameplay ()
					.quiting ();
					continuation .block (() -> {
						client .panel .emit ("leaderboard"); }); }
				catch (Exception e) {
		    		JOptionPane .showMessageDialog (panel, e .getMessage (), "Error", JOptionPane .ERROR_MESSAGE);
		    		e .printStackTrace (); } } });
		logoutButton .addActionListener (__ -> {
			if (! continuation .blocking () ) {
		    	try {
					client .gameplay ()
					.quiting ();
					continuation .block (() -> {
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
				    		e .printStackTrace (); } }); }
		    	catch (Exception e) {
		    		JOptionPane .showMessageDialog (panel, e .getMessage (), "Error", JOptionPane .ERROR_MESSAGE);
		    		e .printStackTrace (); } } });
			
		return panel; } }