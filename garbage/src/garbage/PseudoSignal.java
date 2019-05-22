package garbage;

import java.util.function.Supplier;

public class PseudoSignal <T> extends Reactive <T> {
	T cache;
	
	public PseudoSignal (Supplier <T> f) {
		Reactive .watch (() -> {
			T newValue = f .get ();
			if (newValue != null) {
				if (newValue != this .cache) {
					this .cache = newValue;
					this .invalidate (); } } }); }

	@Override
	public T show () {
		return cache; } }
