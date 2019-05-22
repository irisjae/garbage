package garbage.gameplay;

import java.util.List;
import java.util.Map.Entry;

import garbage.Utils;
import garbage.fish.FishException;
import garbage.fish.MessageInterrupt;

public class TwentyFourGameplayServerRules {
	TwentyFourGameplayServer gameplay;

	Runnable fourWaitingThenJoinRule = () -> {
		try {
			if (gameplay .waiting .mark () .size () > 4) {
				joined (gameplay .waiting .show () .subList (0, 4)); } }
		catch (FishException e) {
			e .printStackTrace (); } };
	Runnable hyperwaitingAnyWaitingThenJoinRule = () -> {
		try {
			if (gameplay .hyperwaiting .mark () .size () > 0 && gameplay .waiting .mark () .size () > 0) {
				var hyperwaits = gameplay .hyperwaiting .show ();
				var waits = gameplay .waiting .show ();
				joined (Utils .concat (hyperwaits, waits .subList (0, Math .min (4 - hyperwaits .size (), waits .size ())))); } }
		catch (FishException e) {
			e .printStackTrace (); } };

			
			
	public TwentyFourGameplayServerRules (TwentyFourGameplayServer gameplay) {
		this .gameplay = gameplay; }
	
	
	void wait (TwentyFourGameplayPlayer player) {
		gameplay .waiting .emit (Utils .cons (player, gameplay .waiting .show ()));
		MessageInterrupt .timeout
			(10 * 1000
			, gameplay .requests
			, TwentyFourGameplayProtocol .request (TwentyFourGameplayProtocol .HYPERWAIT, player .id) ); }
	void hyperwait (TwentyFourGameplayPlayer player) {
		if (gameplay .waiting .show () .contains (player)) {
			gameplay .waiting .emit (Utils .remove (player, gameplay .waiting .show ()));
			gameplay .hyperwaiting .emit (Utils .cons (player, gameplay .hyperwaiting .show ())); } }
	void quit (TwentyFourGameplayPlayer player) throws FishException {
		gameplay .waiting .emit (Utils .remove (player, gameplay .waiting .show ()));
		gameplay .hyperwaiting .emit (Utils .remove (player, gameplay .hyperwaiting .show ()));
		gameplay .playing .emit (Utils .mapMap (room -> Utils .remove (player, room), gameplay .playing .show ()));
		
		left (player); }
	void attempt (TwentyFourGameplayPlayer player, String attempt) throws FishException {
		var question = gameplay .playing .show () .entrySet () .stream ()
			.filter (entry -> entry .getValue () .contains (player)) .map (Entry ::getKey) .findAny () .get ();
		if (question .solutionOk (attempt)) {
			won (player); }
		else {
			failed (player); } }
	
	

	void joined (List <TwentyFourGameplayPlayer> players) throws FishException {
		var question = TwentyFourGameplayQuestion .generate ();
		gameplay .waiting .emit (Utils .filter (player -> ! players .contains (player), gameplay .waiting .show ()));
		gameplay .hyperwaiting .emit (Utils .filter (player -> ! players .contains (player), gameplay .hyperwaiting .show ()));
		gameplay .playing .emit (Utils .mapCons (question, players, gameplay .playing .show ()));
		gameplay .responses .send (TwentyFourGameplayProtocol .response
			( TwentyFourGameplayProtocol .JOINED
			, Utils .cons
				( question .toString ()
				, Utils .map (player -> player .id, players) )
				.toArray (String [] ::new) ) ); }
	void left (TwentyFourGameplayPlayer player) throws FishException {
		gameplay .responses .send (TwentyFourGameplayProtocol .response
			( TwentyFourGameplayProtocol .LEFT
			, player .id ) ); }
	void failed (TwentyFourGameplayPlayer player) throws FishException {
		gameplay .responses .send (TwentyFourGameplayProtocol .response
			( TwentyFourGameplayProtocol .FAILED
			, player .id ) ); }
	void won (TwentyFourGameplayPlayer player) throws FishException {
		gameplay .playing .emit (Utils .mapFilter (room -> ! room .contains (player), gameplay .playing .show ()));
		gameplay .responses .send (TwentyFourGameplayProtocol .response
			( TwentyFourGameplayProtocol .WON
			, player .id ) ); }
	

	
	void apply (String requestText) throws TwentyFourGameplayException, FishException {
		var parts = requestText .split (" ");
		var request = TwentyFourGameplayProtocol .valueOf (parts [0]);
		var player = TwentyFourGameplayPlayer .of (parts [1]);
		var attempt = request == TwentyFourGameplayProtocol .ATTEMPT ? parts [2] : null;

		if (request == TwentyFourGameplayProtocol .WAIT) {
			wait (player); }
		if (request == TwentyFourGameplayProtocol .HYPERWAIT) {
			hyperwait (player); }
		else if (request == TwentyFourGameplayProtocol .QUIT) {
			quit (player); }
		else if (request == TwentyFourGameplayProtocol .ATTEMPT) {
			attempt (player, attempt); }
		else {
			throw new TwentyFourGameplayException ("Unexpected request " + request); } } }