package garbage.client;

import java.awt.GridLayout;
import java.util.Optional;
import java.util.function.Function;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import garbage.Continuation;
import garbage.ProtocolResultHandler;
import garbage.Reactive;
import garbage.Signal;
import garbage.Utils;
import garbage.fish.FishException;
import garbage.gameplay.TwentyFourGameplayPlayer;
import garbage.gameplay.TwentyFourGameplayProtocol;

public class TwentyFourClientGamePanel {	
	static JPanel from (TwentyFourClient client) {
		JPanel panel = new JPanel ();
		
		JPanel topPanel = new JPanel ();
		JButton userProfileButton = new JButton ("User Profile");
		JButton playGameButton = new JButton ("Play Game");
		JButton leaderBoardButton = new JButton ("Leader Board");
		JButton logoutButton = new JButton ("Logout");
		JPanel bottomPanel = new JPanel ();
		JPanel problemPanel = new JPanel ();
		JLabel questionLabel = new JLabel ();
		JTextField attemptBox = new JTextField ();
		JLabel playersLabel = new JLabel ();
		

		panel .setLayout (new GridLayout (0, 1));
		panel .add (topPanel);
		panel .add (bottomPanel);
		topPanel .setLayout (new GridLayout (1, 0));
		topPanel .add (userProfileButton);
		topPanel .add (playGameButton);
		topPanel .add (leaderBoardButton);
		topPanel .add (logoutButton);
		bottomPanel .setLayout (new GridLayout (1, 0));
		bottomPanel .add (problemPanel);
		bottomPanel .add (playersLabel);
		problemPanel .setLayout (new GridLayout (0, 1));
		problemPanel .add (questionLabel);
		problemPanel .add (attemptBox);

		Continuation continuation = Continuation .empty ();
		Reactive .watch (() -> {
			client ._gameplayClient .mark ()
			.ifPresent (gameplay -> {
				gameplay .roomQuestion .mark () .ifPresent (question -> {
					questionLabel .setText (Utils .nowrapText (question .toString () .replace (';', '\n'))); });
				playersLabel .setText (
					String .join (", ", 
						Utils .map (player -> player .id, gameplay .roomPlayers .mark ())));

				if (gameplay .state .mark () == TwentyFourGameplayProtocol .JOINED) {
					continuation .unwind (); }
				else if (gameplay .state .mark () == TwentyFourGameplayProtocol .FAILED) {
					continuation .unwind (); }
				else if (gameplay .state .mark () == TwentyFourGameplayProtocol .WON) {
					continuation .unwind (); 
					client .panel .emit ("profile"); }
				else if (gameplay .state .mark () == TwentyFourGameplayProtocol .LEFT) {
					continuation .flush (); } }); });
		attemptBox .addActionListener (__ -> {
			if (! continuation .blocking () ) {
				try {
					client .gameplay ()
					.attemptting (attemptBox .getText ());
					continuation .block (); }
				catch (Exception e) {
		    		JOptionPane .showMessageDialog (panel, e .getMessage (), "Error", JOptionPane .ERROR_MESSAGE);
					e .printStackTrace (); } } });
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