package garbage.gameplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import garbage.Utils;

public class TwentyFourGameplayQuestion {
	TwentyFourGameplayCard A;
	TwentyFourGameplayCard B;
	TwentyFourGameplayCard C;
	TwentyFourGameplayCard D;
	
	boolean solutionOk (String attempt) {
		var attemptValues = Stream .of (
				attempt
				.replaceAll ("(", "") .replaceAll (")", "")
				.replaceAll ("+", "") .replaceAll ("*", "")
				.replaceAll ("-", "") .replaceAll ("/", "")
				.split ("") )
	        .sorted () .collect (Collectors .joining ());
		var cardsValues = Stream .of ((A .name + B .name + C .name + D .name) .split ("") )
	        .sorted () .collect (Collectors .joining ());
		return (attemptValues .equals (cardsValues))
			&& TwentyFourSolver .solvable (attempt); }
	
	@Override
	public String toString () {
		return String .join (";", Utils .map (card -> card .toString (), List .of (this .A, this .B, this .C, this .D))); }
	
	static TwentyFourGameplayQuestion fromString (String string) {
		var cards = Utils .map (card -> TwentyFourGameplayCard .valueOf (card), string .split (";"));
		var question = new TwentyFourGameplayQuestion ();
		question .A = cards .get (0);
		question .B = cards .get (1);
		question .C = cards .get (2);
		question .D = cards .get (3);
		return question; }
	
	static TwentyFourGameplayQuestion generate () {
		var deck = IntStream .range (0, TwentyFourGameplayCard .values () .length) .boxed ()
			.collect (Collectors .toCollection (ArrayList ::new));
		Collections .shuffle (deck);
		var cards = Utils .map (i -> TwentyFourGameplayCard .values () [i], deck .subList (0, 4));
		if (TwentyFourSolver .solvable (cards .get (0), cards .get (1), cards .get (2), cards .get (3))) {
			var question = new TwentyFourGameplayQuestion ();
			question .A = cards .get (0);
			question .B = cards .get (1);
			question .C = cards .get (2);
			question .D = cards .get (3);
			return question; }
		else {
			return TwentyFourGameplayQuestion .generate (); } } }