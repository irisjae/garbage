package garbage.server;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

import garbage.ProtocolResult;
import garbage.Session;
import garbage.TwentyFourProtocol;
import garbage.TwentyFourServerState;
import garbage.TwentyFourServerStateException;
import garbage.TwentyFourServerStateFS;
import garbage.User;
import garbage.UserInfo;
import garbage.gameplay.TwentyFourGameplayServer;

public class TwentyFourServer implements TwentyFourProtocol, Runnable {

	TwentyFourServerState state;
	
	public TwentyFourServer () throws IOException {
		this .state = new TwentyFourServerStateFS ();
		UnicastRemoteObject .exportObject (this, 0); }

	static boolean validLoginName (String loginName) {
		return ! loginName .contains (" "); }

	
	
	@Override
	public void run() {
	    if (System .getSecurityManager () == null) {
	        System .setSecurityManager (new SecurityManager ()); }
		try {
			log ("listening...");
			LocateRegistry .createRegistry (1099);
			Naming .rebind ("//localhost/TwentyFour", this); 
			
			log ("gameplay listening...");
			(new Thread (new TwentyFourGameplayServer ())) .start (); }
		catch (Exception e) {
			throw new RuntimeException (e); } }
	
	
	@Override
	public ProtocolResult <Session> register (String loginName, String password) throws RemoteException {
		try {
			if (! TwentyFourServer .validLoginName (loginName)) {
				log ("failed invalid login name register: " + loginName);
				return ProtocolResult .fromError ("Login name invalid!"); }
			if (state .userExists (loginName)) {
				log ("failed double register: " + loginName);
				return ProtocolResult .fromError ("Login name already used!"); }
			else {
				log ("registered: " + loginName);
				User user = state .newUser (loginName, password);
				Session session = state .newSession (user);
				return ProtocolResult .fromResult (session); } }
		catch (TwentyFourServerStateException e) {
			throw new RemoteException (e .getMessage ()); } }

	@Override
	public ProtocolResult<Session> login (String loginName, String password) throws RemoteException {
		try {
			if (! state .userAuthenticated (loginName, password)) {
				log ("failed unauthenticated login: " + loginName);
				return ProtocolResult .fromError ("Login name and password does not match!"); }
			else if (state .sessionExists (loginName)) {
				log ("failed double login: " + loginName);
				return ProtocolResult .fromError ("User already logged in!"); }
			else {
				log ("logged in: " + loginName);
				User user = state .getUser (loginName);
				Session session = state .newSession (user);
				return ProtocolResult .fromResult (session); } }
		catch (TwentyFourServerStateException e) {
			throw new RemoteException (e .getMessage ()); } }

	@Override
	public ProtocolResult <Void> logout (Session session) throws RemoteException {
		try {
			log ("logged out: " + session .loginName);
			state .deleteSession (session);
			return ProtocolResult .fromResult (TwentyFourProtocol .VOID); }
		catch (TwentyFourServerStateException e) {
			throw new RemoteException (e .getMessage ()); } }

	@Override
	public ProtocolResult <UserInfo> stats (String loginName) throws RemoteException {
		try {
			if (! state .userExists (loginName)) {
				log ("failed stat: " + loginName);
				return ProtocolResult .fromError ("UserInfo not found!"); }
			else {
				log ("stat: " + loginName);
				return ProtocolResult .fromResult (state .getUserInfo (loginName)); } }
		catch (TwentyFourServerStateException e) {
			throw new RemoteException (e .getMessage ()); } }
	
	@Override
	public void ping () { }
	

	
	
	void log (String msg) {
		System .out .println (msg); } }
