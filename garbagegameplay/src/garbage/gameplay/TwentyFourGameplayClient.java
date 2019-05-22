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
	
	public TwentyFourGameplayPlayer player;

	public Signal <TwentyFourGameplayProtocol> state = new Signal (TwentyFourGameplayProtocol .LEFT);
	public Signal <Optional <String>> attempt = new Signal (Optional .empty ());
	public Signal <Optional <TwentyFourGameplayQuestion>> roomQuestion = new Signal (Optional .empty ());
	public Signal <List <TwentyFourGameplayPlayer>> roomPlayers = new Signal (Utils .listOf ());
	
	public TwentyFourGameplayClient (TwentyFourGameplayPlayer player) throws FishException {
		this .gameplay = this;
		this .player = player;
		this .requests = new MessageSender (TwentyFourGameplayProtocol .requestQueue, TwentyFourGameplayProtocol .requestFactory);
		this .responses = new MessageReceiver (TwentyFourGameplayProtocol .responseTopic, TwentyFourGameplayProtocol .responseFactory); }

	
	
	public void waiting () throws FishException {
		gameplay .state .emit (TwentyFourGameplayProtocol .WAIT);
		gameplay .requests .send (TwentyFourGameplayProtocol .request
			(TwentyFourGameplayProtocol .WAIT, gameplay .player .id) ); }
	public void quiting () throws FishException {
		gameplay .state .emit (TwentyFourGameplayProtocol .QUIT);
		gameplay .requests .send (TwentyFourGameplayProtocol .request
			(TwentyFourGameplayProtocol .QUIT, gameplay .player .id) ); }
	public void attemptting (String attempt) throws FishException {
		gameplay .state .emit (TwentyFourGameplayProtocol .ATTEMPT);
		gameplay .requests .send (TwentyFourGameplayProtocol .request
			(TwentyFourGameplayProtocol .ATTEMPT, Utils .listOf (gameplay .player .id, attempt)) ); }
	
	

	void joined (TwentyFourGameplayQuestion question, List <TwentyFourGameplayPlayer> players) {
		if (players .contains (gameplay .player)) {
			gameplay .roomQuestion .emit (Optional .of (question));
			gameplay .roomPlayers .emit (players);
			gameplay .state .emit (TwentyFourGameplayProtocol .JOINED);	} }
	void left (TwentyFourGameplayPlayer player) {
		if (player == gameplay .player) {
			gameplay .roomQuestion .emit (Optional .empty ());
			gameplay .roomPlayers .emit (Utils .listOf ()); 
			gameplay .state .emit (TwentyFourGameplayProtocol .LEFT); }
		else {
			gameplay .roomPlayers .emit (Utils .remove (player, gameplay .roomPlayers .show ())); } }
	void failed (TwentyFourGameplayPlayer player) {
		gameplay .state .emit (TwentyFourGameplayProtocol .FAILED); }
	void won (TwentyFourGameplayPlayer player) {
		if (gameplay .roomPlayers .show () .contains (player)) {
			gameplay .roomQuestion .emit (Optional .empty ());
			gameplay .roomPlayers .emit (Utils .listOf ()); } 
		gameplay .state .emit (TwentyFourGameplayProtocol .WON); }
	

	void apply (String responseText) throws TwentyFourGameplayException {
		String [] parts = responseText .split (" ");
		TwentyFourGameplayProtocol response = TwentyFourGameplayProtocol .valueOf (parts [0]);
		TwentyFourGameplayQuestion question = response == TwentyFourGameplayProtocol .JOINED ? TwentyFourGameplayQuestion .fromString (parts [1]) : null;
		List <TwentyFourGameplayPlayer> players = Utils .map (TwentyFourGameplayPlayer ::of, Arrays .asList (parts) .subList (response == TwentyFourGameplayProtocol .JOINED ? 2 : 1, parts .length));
		TwentyFourGameplayPlayer player = players .get (0);

		if (response == TwentyFourGameplayProtocol .JOINED) {
			joined (question, players); }
		else if (response == TwentyFourGameplayProtocol .LEFT) {
			left (player); }
		else if (response == TwentyFourGameplayProtocol .FAILED) {
			failed (player); }
		else if (response == TwentyFourGameplayProtocol .WON) {
			won (player); }
		else {
			throw new TwentyFourGameplayException ("Unexpected response " + response); } }
	
	@Override
	public void run () {
		TwentyFourGameplayClient rules = this;

		while (true) {
			try {
				rules .apply (this .responses .receive ()); }
			catch (FishException | TwentyFourGameplayException e) {
				e .printStackTrace (); }
			/*catch (InterruptedException e) {
				break; }*/ } } }