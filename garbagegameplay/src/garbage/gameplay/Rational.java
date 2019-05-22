package garbage.gameplay;

public class Rational {
	int numerator;
	int denominator;
	

	@Override
	public boolean equals (Object obj) {
		return (obj != null)
			&& (Rational .class .isAssignableFrom (obj .getClass ()))
			&& (((Rational) obj) .numerator == this .numerator)
			&& (((Rational) obj) .denominator == this .denominator); }
	@Override
	public String toString () {
		return this .numerator + "/" + this .denominator; }

	static int gcd (int a, int b) {
	    if (b == 0) {
	       return a; }
	    else {
	       return Rational .gcd (b, a % b); } }

	static Rational of (int n) {
		return Rational .of (n, 1); }
	static Rational of (int numerator, int denominator) {
		var rational = new Rational ();
		if (numerator == 0) {
			rational .numerator = 0;
			rational .denominator = 1; }
		else {
			var gcd = Rational .gcd (numerator, denominator);
			rational .numerator = numerator / gcd;
			rational .denominator = denominator / gcd; }
		return rational; } }