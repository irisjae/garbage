package garbage;

import java.awt.GridLayout;
import java.util.Optional;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class TwentyFourClientProfilePanel {	
	static JPanel from (TwentyFourClient client) {
		var panel = new JPanel ();
		
		var topPanel = new JPanel ();
		var userProfileButton = new JButton ("User Profile");
		var playGameButton = new JButton ("Play Game");
		var leaderBoardButton = new JButton ("Leader Board");
		var logoutButton = new JButton ("Logout");
		var infoLabel = new JLabel ();
		Reactive .watch (() -> {
			infoLabel .setText (Utils .labelText (
				Reactive .maybeMarkM (client .userName) .orElse ("Loading...") + "\n" +
				"\n" +
				"Number of wins: " + Reactive .maybeMarkM (client .userWinsCount) .map (n -> "" + n) .orElse ("Loading...") + "\n" +
				"Number of games: " + Reactive .maybeMarkM (client .userGamesCount) .map (n -> "" + n) .orElse ("Loading...") + "\n" +
				"Average time to win: " + Reactive .maybeMarkM (client .userWinTimesAverage) .map (n -> "" + n) .orElse ("Loading...") + "\n" +
				"Rank: " + Reactive .maybeMarkM (client .userRank) .map (n -> "" + n) .orElse ("Loading...") ) ); } );

		panel .setLayout (new GridLayout (0, 1));
		panel .add (topPanel);
		panel .add (infoLabel);
		topPanel .setLayout (new GridLayout (1, 0));
		topPanel .add (userProfileButton);
		topPanel .add (playGameButton);
		topPanel .add (leaderBoardButton);
		topPanel .add (logoutButton);

		playGameButton .addActionListener (__ -> {
	    	client .panel .emit ("play"); });
		leaderBoardButton .addActionListener (__ -> {
	    	client .panel .emit ("leaderboard"); });
		logoutButton .addActionListener (__ -> {
	    	try {
	    		client .protocol ()
    			.logout (Reactive .show (client .session) .get())
    			.handle (ProtocolResultHandler .of (
    				session -> {
    					client .session .emit (Optional .empty ()); },
    				error -> {
						JOptionPane .showMessageDialog (panel, error .error, "Error", JOptionPane .ERROR_MESSAGE); } ) ); }
	    	catch (Exception e) {
	    		e .printStackTrace (); } });
		
		return panel; } }