package garbage;

import java.io.Serializable;

public class ProtocolError implements Serializable {
	String error;
	
	public ProtocolError (String error) {
		this .error = error; } }