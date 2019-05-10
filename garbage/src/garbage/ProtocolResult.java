package garbage;

import java.io.Serializable;

public class ProtocolResult <T> implements Serializable {
	boolean success;
	T result;
	ProtocolError error;
	
	public ProtocolResult (T result) {
		this .success = true;
		this .result = result; }
	public ProtocolResult (ProtocolError error) {
		this .success = false;
		this .error = error; }
	
	public void handle (ProtocolResultHandler <T> handler) {
		if (this .success) {
			handler .success (this .result); }
		else {
			handler .failure (this .error); } }

	public static <T> ProtocolResult<T> fromResult (T result) {
		return new ProtocolResult<T> (result); }
	public static <T> ProtocolResult<T> fromError (String error) {
		return new ProtocolResult<T> (new ProtocolError (error)); } }