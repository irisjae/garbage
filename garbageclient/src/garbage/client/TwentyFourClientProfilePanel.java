package garbage.client;

import java.awt.GridLayout;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import garbage.ProtocolResultHandler;
import garbage.Reactive;
import garbage.Utils;

public class TwentyFourClientProfilePanel {	
	static JPanel from (TwentyFourClient client) {
		JPanel panel = new JPanel ();
		
		JPanel topPanel = new JPanel ();
		JButton userProfileButton = new JButton ("User Profile");
		JButton playGameButton = new JButton ("Play Game");
		JButton leaderBoardButton = new JButton ("Leader Board");
		JButton logoutButton = new JButton ("Logout");
		JLabel infoLabel = new JLabel ();
		Reactive .watch (() -> {
			infoLabel .setText (Utils .nowrapText (
				client .session .mark () .map (session -> session .loginName) .orElse ("Loading...") + "\n" +
				"\n" +
				"Number of wins: " + client .userStat .mark () .map (userStat -> "" + userStat .winsCount) .orElse ("Loading...") + "\n" +
				"Number of games: " + client .userStat .mark () .map (userStat -> "" + userStat .gamesCount) .orElse ("Loading...") + "\n" +
				"Average time to win: " + client .userStat .mark () .map (userStat -> "" + userStat .winsTimesAverage) .orElse ("Loading...") + "\n" +
				"Rank: " + client .userStat .mark () .map (userStat -> "" + userStat .rank) .orElse ("Loading...") ) ); } );

		panel .setLayout (new GridLayout (0, 1));
		panel .add (topPanel);
		panel .add (infoLabel);
		topPanel .setLayout (new GridLayout (1, 0));
		topPanel .add (userProfileButton);
		topPanel .add (playGameButton);
		topPanel .add (leaderBoardButton);
		topPanel .add (logoutButton);

		client .panel .listen (_panel -> {
			if (_panel == "profile") {
				try {
					client .protocol ()
					.stats (client .session .show () .get () .loginName)
					.handle (ProtocolResultHandler .of (
						userStat -> {
							client .userStat .emit (Optional .of (userStat));}, 
						error -> {
							JOptionPane .showMessageDialog (panel, error .error, "Error", JOptionPane .ERROR_MESSAGE); })); } 
				catch (Exception e) {
					JOptionPane .showMessageDialog (panel, e .getMessage (), "Error", JOptionPane .ERROR_MESSAGE);
					e .printStackTrace (); } } });
		playGameButton .addActionListener (__ -> {
	    	client .panel .emit ("play"); });
		leaderBoardButton .addActionListener (__ -> {
	    	client .panel .emit ("leaderboard"); });
		logoutButton .addActionListener (__ -> {
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
	    		e .printStackTrace (); } });
		
		return panel; } }