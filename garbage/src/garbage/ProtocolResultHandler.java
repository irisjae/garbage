package garbage;

import java.util.function.Consumer;

public interface ProtocolResultHandler <T> {
	public void success (T result);
	public void failure (ProtocolError error);
	
	public static <T> ProtocolResultHandler <T> of (Consumer <T> successHandler, Consumer <ProtocolError> failureHandler) {
		return new ProtocolResultHandler <T> () {
			public void success(T result) {
				successHandler .accept (result); }
			public void failure(ProtocolError error) {
				failureHandler .accept (error); } }; } }