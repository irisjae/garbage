package garbage;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class Emitter <T> {
	List <Consumer <T>> listeners = new LinkedList (); 
	
	public void listen (Consumer <T> listener) {
		this .listeners .add (listener); }
	public void emit (T value) {
		for (Consumer <T> listener: this .listeners) {
			listener .accept (value); } } }
