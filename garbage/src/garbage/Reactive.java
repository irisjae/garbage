package garbage;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;


public class Reactive <T> {
	static List <List <Reactive>> invalidaters = new LinkedList <List <Reactive>> ();
	
	List <Consumer <T>> sinks = new LinkedList <Consumer <T>> ();
	T value;

	public Reactive () {}
	public Reactive (T value) {
		this .emit (value); }

	void listen (Consumer <T> sink) {
		sinks .add (sink);
		if (this .value != null) {
			sink .accept (this .value); } }
	void unlisten (Consumer <T> sink) {
		sinks .remove (sink); }
	void emit (T value) {
		if (value != null) {
			this .value = value;
			for (var sink : this .sinks) {
				sink .accept (value); } } }

	static <T> T show (Reactive <T> x) {
		return Reactive .maybeShow (x) .get (); }
	static <T> Optional <T> maybeShow (Reactive <T> x) {
		return Optional .ofNullable (x .value); }
	static <T> Optional <T> maybeShowM (Reactive <Optional <T>> x) {
		return Reactive .maybeShow (x) .flatMap (y -> y); }
	
	static <T> T mark (Reactive <T> x) {
		return Reactive .maybeMark (x) .get (); }
	static <T> Optional <T> maybeMark (Reactive <T> x) {
		if (Reactive .invalidaters .isEmpty ()) {
			throw new RuntimeException ("Why are you marking this?"); }
		for (var invalidater : Reactive .invalidaters) {
			invalidater .add (x); }
		return Reactive .maybeShow (x); }
	static <T> Optional <T> maybeMarkM (Reactive <Optional <T>> x) {
		return Reactive .maybeMark (x) .flatMap (y -> y); }
	
	
	static void watch (Runnable r) {
		var invalidater = new LinkedList <Reactive> ();
		var invalidate_ = new LinkedList <Consumer> ();
		invalidate_ .add ( (Consumer) (__ -> {
			if (! Reactive .invalidaters .contains (invalidater)) {
				for (var x : invalidater) {
					x .unlisten (invalidate_ .peekFirst ()); }
				Reactive .watch (r); } }) );
		Reactive .invalidaters .add (invalidater);
		r .run ();
		for (var x : invalidater) {
			x .listen (invalidate_ .peekFirst ()); }
		Reactive .invalidaters .remove (invalidater); }
	static <T> Reactive <T> watching (Supplier <T> f) {
		var x = new Reactive <T> ();
		Reactive .watch (() -> {
			x .emit (f .get()); });
		return x; } }
