package garbage;

public class Signal <T> extends Reactive <T> {
	T value;

	public Signal () {}
	public Signal (T value) {
		this .emit (value); }
	
	public void emit (T value) {
		if (value == null) {
			throw new NullPointerException ("null is not a valid value!"); }
		this .value = value;
		this .invalidate (); }

	@Override
	public T show () {
		if (this .value == null) {
			throw new RuntimeException ("value not initialized!"); }
		return this .value; } }