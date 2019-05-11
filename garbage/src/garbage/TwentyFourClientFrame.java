package garbage;

import java.awt.CardLayout;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

public class TwentyFourClientFrame {
	
	static List <String> loggedOutPanels = Arrays .asList ("login", "register");
	static List <String> loggedInPanels = Arrays .asList ("profile", "play", "leaderboard", "game");

	static JFrame from (TwentyFourClient client) {
		var frame = new JFrame ("Twenty Four");
		var layout = new CardLayout ();
		
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
			session .ifPresentOrElse (
				__ -> {
					if (TwentyFourClientFrame .loggedOutPanels .contains (Reactive .show (client .panel))) {
						client .panel .emit (TwentyFourClientFrame .loggedInPanels .get (0)); } },
				() -> {
					if (TwentyFourClientFrame .loggedInPanels .contains (Reactive .show (client .panel))) {
						client .panel .emit (TwentyFourClientFrame .loggedOutPanels .get (0)); } } ); } );
		
		return frame; } }
