package garbage.gameplay;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import garbage.Reactive;
import garbage.Signal;
import garbage.fish.FishException;
import garbage.fish.MessageReceiver;
import garbage.fish.MessageSender;

public class TwentyFourGameplayServer implements Runnable {
	
	MessageReceiver requests;
	MessageSender responses;

	Signal <List <TwentyFourGameplayPlayer>> waiting = new Signal (new LinkedList ());
	Signal <List <TwentyFourGameplayPlayer>> hyperwaiting = new Signal (new LinkedList ());
	Signal <Map <TwentyFourGameplayQuestion, List <TwentyFourGameplayPlayer>>> playing = new Signal (new HashMap ());
	
	public TwentyFourGameplayServer () throws FishException {
		this .requests = new MessageReceiver (TwentyFourGameplayProtocol .requestQueue, TwentyFourGameplayProtocol .requestFactory);
		this .responses = new MessageSender (TwentyFourGameplayProtocol .responseTopic, TwentyFourGameplayProtocol .responseFactory); }
	
	@Override
	public void run () {
		TwentyFourGameplayServerRules rules = new TwentyFourGameplayServerRules (this);
		
		Reactive .watch (rules .fourWaitingThenJoinRule);
		Reactive .watch (rules .hyperwaitingAnyWaitingThenJoinRule);
	
		while (true) {
			try {
				rules .apply (this .requests .receive ()); }
			catch (FishException | TwentyFourGameplayException e) {
				e .printStackTrace (); } } } }