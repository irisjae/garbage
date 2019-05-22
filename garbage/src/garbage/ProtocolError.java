package garbage;

import java.io.Serializable;

public class ProtocolError implements Serializable {
	public String error;
	
	public ProtocolError (String error) {
		this .error = error; } }