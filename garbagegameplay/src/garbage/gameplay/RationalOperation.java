package garbage.gameplay;

import java.util.function.BiFunction;

public enum RationalOperation {
	PLUS ("+", (a, b) -> Rational .of (a .numerator * b .denominator + a .denominator * b .numerator, a .denominator * b .denominator)), 
	MINUS ("-", (a, b) -> Rational .of (a .numerator * b .denominator - a .denominator * b .numerator, a .denominator * b .denominator)), 
	TIMES ("*", (a, b) -> Rational .of (a .numerator * b .numerator, a .denominator * b .denominator)), 
	DIVIDE ("/", (a, b) -> Rational .of (a .numerator * b .denominator, a .denominator * b .numerator));
	
	String name;
	BiFunction <Rational, Rational, Rational> action;
	
	RationalOperation (String name, BiFunction <Rational, Rational, Rational> action) {
		this .name = name;
		this .action = action; }
}
