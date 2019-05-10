package garbage;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;


public class Reactive <T> {
	List <Consumer <T>> sinks = new LinkedList <Consumer <T>> ();
	T value;

	public Reactive () {}
	public Reactive (T value) {
		this .emit (value); }
	
	void listen (Consumer <T> sink) {
		sinks .add (sink);
		if (this .value != null) {
			sink .accept (this .value); } }
	void emit (T value) {
		if (value != null) {
			this .value = value;
			for (var sink : this .sinks) {
				sink .accept (value); } } } }
