package garbage;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class Reactive <T> {
	static Deque <Set <Reactive>> watchers = new ArrayDeque ();
	static Deque <Collection <Runnable>> cleanups = new ArrayDeque ();
	
	List <Consumer <T>> listeners = new LinkedList ();

	public abstract T show ();
	protected void invalidate () {
		for (Consumer listener : this .listeners .stream () .collect (Collectors .toList ())) {
			listener .accept (this .show ()); } }

	public void listen (Consumer <T> listener) {
		this .listeners .add (listener);
		try {
			listener .accept (this .show ()); }
		catch (RuntimeException e) {} }
	public T mark () {
		if (Reactive .watchers .isEmpty ()) {
			throw new RuntimeException ("Why are you marking this?"); }
		for (Set <Reactive> watcher : Reactive .watchers) if (! watcher .contains (this)) {
			watcher .add (this); }
		return this .show (); }
	
	public static void watch (Runnable r) {
		Set <Reactive> watcher = new HashSet ();
		Collection <Runnable> cleanups = new LinkedList ();
		LinkedList <Consumer> invalidateKnot = new LinkedList ();
		invalidateKnot .add ( (Consumer) (__ -> {
			for (Reactive x : watcher) {
				x .listeners .remove (invalidateKnot .peekFirst ()); }
			for (Runnable cleanup : cleanups) {
				cleanup .run (); }
			Reactive .watch (r); }) );
		Reactive .watchers .push (watcher);
		Reactive .cleanups .push (cleanups);
		r .run ();
		Reactive .watchers .pop ();
		Reactive .cleanups .pop ();
		for (Reactive x : watcher) {
			x .listeners .add (invalidateKnot .peekFirst ()); } }
	public static <T> Reactive <T> watching (Supplier <T> f) {
		return new PseudoSignal <T> (f); }
	
	public static void cleanup (Runnable r) {
		Reactive .cleanups .peekFirst () .add (r); }

	}