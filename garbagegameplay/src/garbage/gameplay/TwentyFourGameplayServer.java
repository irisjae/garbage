package garbage.gameplay;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeoutException;

import garbage.Emitter;
import garbage.Reactive;
import garbage.Signal;
import garbage.fish.FishException;
import garbage.fish.MessageReceiver;
import garbage.fish.MessageSender;

public class TwentyFourGameplayServer implements Runnable {
	
	MessageReceiver requests;
	MessageSender responses;

	public Signal <List <TwentyFourGameplayPlayer>> waiting = new Signal (new LinkedList ());
	public Signal <List <TwentyFourGameplayPlayer>> hyperwaiting = new Signal (new LinkedList ());
	public Signal <Map <TwentyFourGameplayQuestion, List <TwentyFourGameplayPlayer>>> playing = new Signal (new HashMap ());

	public Emitter <TwentyFourGameplayPlayer> joined = new Emitter ();
	public Emitter <TwentyFourGameplayPlayer> won = new Emitter ();
	
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