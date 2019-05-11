package garbage;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class TwentyFourServer implements TwentyFourProtocol, Runnable {

	TwentyFourServerState state = new TwentyFourServerState ();
	
	public TwentyFourServer () {
		try {
			UnicastRemoteObject .exportObject (this, 0); }
		catch (Exception e) {
			e .printStackTrace (); } }

	
	
	@Override
	public void run() {
	    if (System .getSecurityManager () == null) {
	        System .setSecurityManager (new SecurityManager ()); }
		try {
			LocateRegistry .createRegistry (1099);
			Naming .rebind ("//localhost/TwentyFour", this); 
			log ("listening..."); }
		catch (Exception e) {
			e .printStackTrace (); } }
	
	
	@Override
	public ProtocolResult<Session> register (String loginName, String password) throws RemoteException {
		if (! state .validLoginName (loginName)) {
			log ("failed invalid login name register: " + loginName);
			return ProtocolResult .fromError ("Login name invalid!"); }
		if (state .userExists (loginName)) {
			log ("failed double register: " + loginName);
			return ProtocolResult .fromError ("Login name already used!"); }
		else {
			log ("registered: " + loginName);
			var user = state .newUser (loginName, password);
			var session = state .newSession (user);
			return ProtocolResult .fromResult (session); } }

	@Override
	public ProtocolResult<Session> login (String loginName, String password) throws RemoteException {
		if (! state .userAuthenticated (loginName, password)) {
			log ("failed unauthenticated login: " + loginName);
			return ProtocolResult .fromError ("Login name and password does not match!"); }
		else if (state .sessionExists (loginName)) {
			log ("failed double login: " + loginName);
			return ProtocolResult .fromError ("User already logged in!"); }
		else {
			log ("logged in: " + loginName);
			var user = state .newUser (loginName, password);
			var session = state .newSession (user);
			return ProtocolResult .fromResult (session); } }

	@Override
	public ProtocolResult<Void> logout (Session session) throws RemoteException {
		log ("logged out: " + session .loginName);
		state .deleteSession (session);
		return ProtocolResult .fromResult (TwentyFourProtocol .VOID); }
	
	@Override
	public void ping () throws RemoteException { }
	

	
	
	void log (String msg) {
		System .out .println (msg); } }
