package garbage.gameplay;

import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import garbage.Utils;

public class TwentyFourSolver {
	static Pattern parentheses = Pattern .compile ("^(.*)\\(([^)]+)\\)(.*)$");
	static Pattern literal = Pattern .compile ("^(-?\\d+)$");	
	static Pattern addition = Pattern .compile ("^(.+)\\+(.+)$");
	static Pattern subtraction = Pattern .compile ("^(.+?)-(.+)$");
	static Pattern multiplication = Pattern .compile ("^(.+)\\*(.+)$");
	static Pattern division = Pattern .compile ("^(.+)/(.+)$");
	static Rational evaluate (String expression) {
		Matcher matcher = null;
		if ((matcher = parentheses .matcher (expression)) .matches ()) {
			return evaluate (matcher .group (1) + evaluate (matcher .group (2)) + matcher .group (3)); }
		else if ((matcher = literal .matcher (expression)) .matches ()) {
			return Rational .of (Integer .parseInt (matcher .group (1))); }
		else if ((matcher = addition .matcher (expression)) .matches ()) {
			return RationalOperation .PLUS .action .apply (evaluate (matcher .group (1)), evaluate (matcher .group (2))); }
		else if ((matcher = subtraction .matcher (expression)) .matches ()) { try {
			return RationalOperation .MINUS .action .apply (evaluate (matcher .group (1)), evaluate (matcher .group (2))); }
		catch (IllegalArgumentException e) { } }
		if ((matcher = multiplication .matcher (expression)) .matches ()) {
			return RationalOperation .TIMES .action .apply (evaluate (matcher .group (1)), evaluate (matcher .group (2))); }
		else if ((matcher = division .matcher (expression)) .matches ()) {
			return RationalOperation .DIVIDE .action .apply (evaluate (matcher .group (1)), evaluate (matcher .group (2))); }
		else {
			throw new IllegalArgumentException ("Cannot parse expression " + expression + "!"); } }
	
	static boolean solvable (TwentyFourGameplayCard a, TwentyFourGameplayCard b, TwentyFourGameplayCard c, TwentyFourGameplayCard d) {
		return TwentyFourSolver .solvable (Rational .of (24), Utils .map (card -> Rational .of (card .value), Utils .listOf (a, b, c, d))); }
	static boolean solvable (Rational result, List <Rational> terms) {
		if (terms .size () == 0) {
			return false; }
		else if (terms .size () == 1) {
			return result .equals (terms .get (0)); }
		else {
			return Utils .indexPairs (terms .size ()) 
				.flatMap ((Function <List <Integer>, Stream <Rational>>) (pair -> { 
					List <Rational> rest = Utils .unpick (pair, terms);
					Rational a = terms .get (pair .get (0));
					Rational b = terms .get (pair .get (1));
					return Stream .of (RationalOperation .values ())
						.filter (op -> ! (op == RationalOperation .DIVIDE && b .equals (Rational .of (0))))
						.map (op -> op .action .apply (a, b))
						.filter (c -> solvable (result, Utils .cons (c, rest))); } ))
				.findAny () .isPresent (); } }
	static boolean solvable (String expression) {
		return TwentyFourSolver .solvable (Rational .of (24), expression); } 
	static boolean solvable (Rational result, String expression) {
		try {
			return result .equals (TwentyFourSolver .evaluate (
				expression
				.replaceAll ("J", "11")
				.replaceAll ("Q", "12")
				.replaceAll ("K", "13") ) ); }
		catch (IllegalArgumentException | ArithmeticException e) {
			return false; } } }