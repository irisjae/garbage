package garbage.client;

import java.awt.GridLayout;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import garbage.ProtocolResultHandler;

public class TwentyFourClientLoginPanel {
	
	static JPanel from (TwentyFourClient client) {
		var panel = new JPanel ();
		
		var loginNameLabel = new JLabel ("Login name:");
		var loginNameField = new JTextField ();
		var passwordLabel = new JLabel ("Password:");
		var passwordField = new JPasswordField ();
		var loginButton = new JButton ("Login");
		var registerButton = new JButton ("Register");
		
		panel .setLayout (new GridLayout (0, 1));
		panel .add (loginNameLabel);
		panel .add (loginNameField);
		panel .add (passwordLabel);
		panel .add (passwordField);
		panel .add (loginButton);
		panel .add (registerButton);

		loginButton .addActionListener (__ -> {
	    	try {
	    		client .protocol ()
    			.login (loginNameField .getText (), passwordField .getText ())
    			.handle (ProtocolResultHandler .of (
					session -> {
    					client .session .emit (Optional .of (session)); },
    				error -> {
						JOptionPane .showMessageDialog (panel, error .error, "Error", JOptionPane .ERROR_MESSAGE); } ) ); }
	    	catch (Exception e) {
	    		JOptionPane .showMessageDialog (panel, e .getMessage (), "Error", JOptionPane .ERROR_MESSAGE);
	    		e .printStackTrace (); } });
		registerButton .addActionListener (__ -> {
	    	client .panel .emit ("register"); });	
		
		return panel; } }
