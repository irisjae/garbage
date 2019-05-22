package garbage.client;

import java.awt.GridLayout;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import garbage.ProtocolResultHandler;

public class TwentyFourClientPlayPanel {	
	static JPanel from (TwentyFourClient client) {
		var panel = new JPanel ();
		
		var topPanel = new JPanel ();
		var userProfileButton = new JButton ("User Profile");
		var playGameButton = new JButton ("Play Game");
		var leaderBoardButton = new JButton ("Leader Board");
		var logoutButton = new JButton ("Logout");
		var newGameButton = new JButton ("New game");

		panel .setLayout (new GridLayout (0, 1));
		panel .add (topPanel);
		panel .add (newGameButton);
		topPanel .setLayout (new GridLayout (1, 0));
		topPanel .add (userProfileButton);
		topPanel .add (playGameButton);
		topPanel .add (leaderBoardButton);
		topPanel .add (logoutButton);

		newGameButton .addActionListener (__ -> {
			
	    	client .panel .emit ("wait"); });
		userProfileButton .addActionListener (__ -> {
	    	client .panel .emit ("profile"); });
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