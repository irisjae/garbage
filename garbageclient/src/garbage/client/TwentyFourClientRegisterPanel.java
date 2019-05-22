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

public class TwentyFourClientRegisterPanel {
	static JPanel from (TwentyFourClient client) {
		var panel = new JPanel ();
		
		var loginNameLabel = new JLabel ("Login name:");
		var loginNameField = new JTextField ();
		var passwordLabel = new JLabel ("Password:");
		var passwordField = new JPasswordField ();
		var confirmPasswordLabel = new JLabel ("Confirm Password:");
		var confirmPasswordField = new JPasswordField ();
		var registerButton = new JButton ("Register");
		var cancelButton = new JButton ("Cancel");
		
		panel .setLayout (new GridLayout (0, 1));
		panel .add (loginNameLabel);
		panel .add (loginNameField);
		panel .add (passwordLabel);
		panel .add (passwordField);
		panel .add (confirmPasswordLabel);
		panel .add (confirmPasswordField);
		panel .add (registerButton);
		panel .add (cancelButton);

		registerButton .addActionListener (__ -> {
	    	try {
	    		if (passwordField .getText () .equals (confirmPasswordField .getText ())) {
		    		client .protocol ()
	    			.register (loginNameField .getText (), passwordField .getText ())
	    			.handle (ProtocolResultHandler .of (
	    				session -> {
	    					client .session .emit (Optional .of (session)); },
	    				error -> {
							JOptionPane .showMessageDialog (panel, error .error, "Error", JOptionPane .ERROR_MESSAGE); } ) ); }
	    		else {
	    			JOptionPane .showMessageDialog (panel, "Make sure the passwords are the same!", "Error", JOptionPane .ERROR_MESSAGE); } }
	    	catch (Exception e) {
	    		JOptionPane .showMessageDialog (panel, e .getMessage (), "Error", JOptionPane .ERROR_MESSAGE);
	    		e .printStackTrace (); } });
		cancelButton .addActionListener (__ -> {
	    	client .panel .emit ("login"); });
		
		return panel; } }
