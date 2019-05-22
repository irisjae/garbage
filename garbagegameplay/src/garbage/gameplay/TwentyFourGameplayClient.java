package garbage.gameplay;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.naming.NamingException;

import garbage.Signal;
import garbage.Utils;
import garbage.fish.FishException;
import garbage.fish.Lookup;
import garbage.fish.MessageReceiver;
import garbage.fish.MessageSender;

public class TwentyFourGameplayClient implements Runnable {
	TwentyFourGameplayClient gameplay;

	MessageSender requests;
	MessageReceiver responses;
	
	TwentyFourGameplayPlayer player;

	Signal <TwentyFourGameplayProtocol> state = new Signal (TwentyFourGameplayProtocol .LEFT);
	Signal <Optional <String>> attempt = new Signal ();
	Signal <Optional <TwentyFourGameplayQuestion>> roomQuestion = new Signal (Optional .empty ());
	Signal <List <TwentyFourGameplayPlayer>> roomPlayers = new Signal (List .of ());
	
	public TwentyFourGameplayClient (TwentyFourGameplayPlayer player) throws FishException {
		this .player = player;
		this .requests = new MessageSender (TwentyFourGameplayProtocol .requestQueue, TwentyFourGameplayProtocol .requestFactory);
		this .responses = new MessageReceiver (TwentyFourGameplayProtocol .responseTopic, TwentyFourGameplayProtocol .responseFactory); }

	
	
	void waiting () throws FishException {
		gameplay .state .emit (TwentyFourGameplayProtocol .WAIT);
		gameplay .requests .send (TwentyFourGameplayProtocol .request
			(TwentyFourGameplayProtocol .WAIT, gameplay .player .id) ); }
	void quiting () throws FishException {
		gameplay .state .emit (TwentyFourGameplayProtocol .QUIT);
		gameplay .requests .send (TwentyFourGameplayProtocol .request
			(TwentyFourGameplayProtocol .QUIT, gameplay .player .id) ); }
	void attemptting (String attempt) throws FishException {
		gameplay .state .emit (TwentyFourGameplayProtocol .ATTEMPT);
		gameplay .requests .send (TwentyFourGameplayProtocol .request
			(TwentyFourGameplayProtocol .ATTEMPT, gameplay .player .id, attempt) ); }
	
	

	void joined (TwentyFourGameplayQuestion question, List <TwentyFourGameplayPlayer> players) {
		gameplay .state .emit (TwentyFourGameplayProtocol .JOINED);
		gameplay .roomQuestion .emit (Optional .of (question));
		gameplay .roomPlayers .emit (players); }
	void left (TwentyFourGameplayPlayer player) {
		gameplay .state .emit (TwentyFourGameplayProtocol .LEFT);
		if (player == gameplay .player) {
			gameplay .roomQuestion .emit (Optional .empty ());
			gameplay .roomPlayers .emit (List .of ()); }
		else {
			gameplay .roomPlayers .emit (Utils .remove (player, gameplay .roomPlayers .show ())); } }
	void failed (TwentyFourGameplayPlayer player) {
		gameplay .state .emit (TwentyFourGameplayProtocol .FAILED); }
	void won (TwentyFourGameplayPlayer player) {
		gameplay .state .emit (TwentyFourGameplayProtocol .WON); 
		if (gameplay .roomPlayers .show () .contains (player)) {
			gameplay .roomQuestion .emit (Optional .empty ());
			gameplay .roomPlayers .emit (List .of ()); } }
	

	void apply (String responseText) throws TwentyFourGameplayException {
		var parts = responseText .split (" ");
		var response = TwentyFourGameplayProtocol .valueOf (parts [0]);
		var question = response == TwentyFourGameplayProtocol .JOINED ? TwentyFourGameplayQuestion .fromString (parts [1]) : null;
		var players = Utils .map (TwentyFourGameplayPlayer ::of, Arrays .asList (parts) .subList (1, parts .length));
		var player = players .get (0);

		if (response == TwentyFourGameplayProtocol .JOINED) {
			joined (question, players); }
		if (response == TwentyFourGameplayProtocol .LEFT) {
			left (player); }
		else if (response == TwentyFourGameplayProtocol .FAILED) {
			failed (player); }
		else if (response == TwentyFourGameplayProtocol .WON) {
			won (player); }
		else {
			throw new TwentyFourGameplayException ("Unexpected response " + response); } }
	
	@Override
	public void run () {
		var rules = this;

		while (true) {
			try {
				rules .apply (this .responses .receive ()); }
			catch (FishException | TwentyFourGameplayException e) {
				e .printStackTrace (); }
			/*catch (InterruptedException e) {
				break; }*/ } } }