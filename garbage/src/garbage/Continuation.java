package garbage;

public class Continuation {
	boolean suspended = false;
	Runnable continuation = () -> {};

	public boolean lock () {
		if (this .blocking ()) {
			return false; }
		else {
			this .block ();
			return true; } }
	public boolean blocking () {
		return this .suspended; }
	public void block () {
		this .block (() -> {}); }
	public void block (Runnable r) {
		this .suspended = true;
		this .continuation = r; }
	public void flush () {
		Runnable continuation = this .continuation;
		this .unwind (); 
		continuation .run (); }
	public void unwind () {
		this .suspended = false;
		this .continuation = () -> {}; }
	
	public static Continuation empty () {
		Continuation zero = new Continuation ();
		return zero; } }
