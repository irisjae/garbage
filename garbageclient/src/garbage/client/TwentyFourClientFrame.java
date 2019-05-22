package garbage.client;

import java.awt.CardLayout;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import garbage.ProtocolResultHandler;
import garbage.Utils;

public class TwentyFourClientFrame {
	
	static List <String> loggedOutPanels = Arrays .asList ("login", "register");
	static List <String> loggedInPanels = Arrays .asList ("profile", "play", "leaderboard", "game");

	static JFrame from (TwentyFourClient client) {
		JFrame frame = new JFrame ("Twenty Four");
		CardLayout layout = new CardLayout ();
		
		frame .setLayout (layout);
		frame .add (TwentyFourClientLoginPanel .from (client), "login");
		frame .add (TwentyFourClientRegisterPanel .from (client), "register");
		frame .add (TwentyFourClientProfilePanel .from (client), "profile");
		frame .add (TwentyFourClientPlayPanel .from (client), "play");
		frame .add (TwentyFourClientLeaderBoardPanel .from (client), "leaderboard");
		// frame .add (TwentyFourClientProfilePanel .from (client), "game");
		
		frame .pack ();
		
		frame .setDefaultCloseOperation (JFrame .EXIT_ON_CLOSE);
		
		client .panel .emit ("login");
		
		client .panel .listen (panel -> {
			layout .show (frame .getContentPane (), panel); });
		client .session .listen (session -> {
			Utils .ifPresentOrElse (session, 
				__ -> {
					if (TwentyFourClientFrame .loggedOutPanels .contains (client .panel .show ())) {
						client .panel .emit (TwentyFourClientFrame .loggedInPanels .get (0)); } },
				() -> {
					if (TwentyFourClientFrame .loggedInPanels .contains (client .panel .show ())) {
						client .panel .emit (TwentyFourClientFrame .loggedOutPanels .get (0)); } } ); } );
		
		client .session .listen (maybeSession -> {
			maybeSession .ifPresent (
				session -> {
					try {
						client .protocol ()
						.stats (session .loginName)
						.handle (ProtocolResultHandler .of (
							userInfo -> {
								client .userInfo .emit (Optional .of (userInfo)); },
		    				error -> {
								JOptionPane .showMessageDialog (frame, error .error, "Error", JOptionPane .ERROR_MESSAGE); } )); }
					catch (Exception e) {
						JOptionPane .showMessageDialog (frame, e .getMessage (), "Error", JOptionPane .ERROR_MESSAGE);
						e .printStackTrace (); } } ); } ); 
		
		return frame; } }
