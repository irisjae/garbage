package garbage;

public class Continuation {
	boolean suspended = false;
	Runnable continuation = () -> {};
	
	public boolean blocking () {
		return this .suspended; }
	public void block () {
		this .block (() -> {}); }
	public void block (Runnable r) {
		this .suspended = true;
		this .continuation = r; }
	public void flush () {
		this .continuation .run ();
		this .unwind (); }
	public void unwind () {
		this .suspended = false;
		this .continuation = () -> {}; }
	
	public static Continuation empty () {
		Continuation zero = new Continuation ();
		return zero; } }
