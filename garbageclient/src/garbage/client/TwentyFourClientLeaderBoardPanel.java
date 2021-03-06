package garbage.client;

import java.awt.GridLayout;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import garbage.ProtocolResultHandler;

public class TwentyFourClientLeaderBoardPanel {	
	static JPanel from (TwentyFourClient client) {
		JPanel panel = new JPanel ();
		
		JPanel topPanel = new JPanel ();
		JButton userProfileButton = new JButton ("User Profile");
		JButton playGameButton = new JButton ("Play Game");
		JButton leaderBoardButton = new JButton ("Leader Board");
		JButton logoutButton = new JButton ("Logout");
		JLabel infoLabel = new JLabel ();

		panel .setLayout (new GridLayout (0, 1));
		panel .add (topPanel);
		panel .add (infoLabel);
		topPanel .setLayout (new GridLayout (1, 0));
		topPanel .add (userProfileButton);
		topPanel .add (playGameButton);
		topPanel .add (leaderBoardButton);
		topPanel .add (logoutButton);

		userProfileButton .addActionListener (__ -> {
	    	client .panel .emit ("profile"); });
		playGameButton .addActionListener (__ -> {
	    	client .panel .emit ("play"); });
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