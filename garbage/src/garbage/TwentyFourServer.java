package garbage;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class TwentyFourServer implements TwentyFourProtocol, Runnable {
	static Path userInfo = Paths .get ("UserInfo.txt");
	static Path onlineUser = Paths .get ("OnlineUser.txt");
	
	Map<String, User> users = new HashMap <String, User> ();
	Map<User, Session> sessions = new HashMap <User, Session> ();

	
	public TwentyFourServer () {
		try {
			readUserInfo ();
			writeOnlineUser ();
			UnicastRemoteObject .exportObject (this, 0); }
		catch (Exception e) {
			e .printStackTrace (); } }

	
	void readUserInfo () {
		try {
			Files .lines (userInfo) .forEach (line -> {
				var parts = line .split (" ");
				var loginName = parts [0];
				var password = parts [1];
				
				var user = new User (loginName, password);
				users .put (loginName, user); }); }
		catch (NoSuchFileException e) {}
		catch (Exception e) {
			e .printStackTrace (); } }
	void readOnlineUser () {
		try {
			Files .lines (onlineUser) .forEach (line -> {
				var parts = line .split (" ");
				var loginName = parts [0];
				var uuid = UUID .fromString (parts [1]);

				var user = users .get (loginName);
				var session = new Session (user, uuid);
				sessions .put (user, session); }); }
		catch (NoSuchFileException e) {}
		catch (Exception e) {
			e .printStackTrace (); } } 
	void writeUserInfo () {
		try {
			Files .write (userInfo, 
				this .users .values ()
					.stream ()
					.map (user -> user .loginName + " " + user .password)
					.collect (Collectors .toList ())
				, StandardCharsets .UTF_8); }
		catch (Exception e) {
			e .printStackTrace (); } }
	void writeOnlineUser () {
		try {
			Files .write (onlineUser, 
				this .sessions .values ()
					.stream ()
					.map (session -> session .loginName + " " + session .uuid)
					.collect (Collectors .toList ())
				, StandardCharsets .UTF_8); }
		catch (Exception e) {
			e .printStackTrace (); } }
	
	
	boolean validLoginName (String loginName) {
		return ! loginName .contains (" "); }
	boolean userAuthenticated (String loginName, String password) {
		return userExists (loginName) && users .get (loginName) .password .equals (password); }
	boolean userExists (String loginName) {
		return users .containsKey (loginName); }
	boolean sessionExists (String loginName) {
		return users .containsKey (loginName)
			&& sessions .containsKey (users .get (loginName)); }
	
	User newUser (String loginName, String password) {
		var user = new User (loginName, password);
		users .put (loginName, user);
		writeUserInfo ();
		return user; }
	void deleteUser (User user) {
		var loginName = user .loginName;
		users .remove (loginName);
		writeUserInfo (); }
	Session newSession (User user) {
		Session session = new Session (user);
		sessions .put (user, session);
		writeOnlineUser ();
		return session; }
	void deleteSession (Session session) {
		var loginName = session .loginName;
		User user = users .get (loginName);
		sessions .remove (user);
		writeOnlineUser (); }
	
	
	void log (String msg) {
		System .out .println (msg); }

	
	
	@Override
	public ProtocolResult<Session> register (String loginName, String password) throws RemoteException {
		if (! this .validLoginName (loginName)) {
			log ("failed invalid login name register: " + loginName);
			return ProtocolResult .fromError ("Login name invalid!"); }
		if (this .userExists (loginName)) {
			log ("failed double register: " + loginName);
			return ProtocolResult .fromError ("Login name already used!"); }
		else {
			log ("registered: " + loginName);
			var user = this .newUser (loginName, password);
			var session = this .newSession (user);
			return ProtocolResult .fromResult (session); } }

	@Override
	public ProtocolResult<Session> login (String loginName, String password) throws RemoteException {
		if (! this .userAuthenticated (loginName, password)) {
			log ("failed unauthenticated login: " + loginName);
			return ProtocolResult .fromError ("Login name and password does not match!"); }
		else if (this .sessionExists (loginName)) {
			log ("failed double login: " + loginName);
			return ProtocolResult .fromError ("User already logged in!"); }
		else {
			log ("logged in: " + loginName);
			var user = this .newUser (loginName, password);
			var session = this .newSession (user);
			return ProtocolResult .fromResult (session); } }

	@Override
	public ProtocolResult<Void> logout (Session session) throws RemoteException {
		log ("logged out: " + session .loginName);
		deleteSession (session);
		return ProtocolResult .fromResult (TwentyFourProtocol .VOID); }
	
	@Override
	public void ping () throws RemoteException { }
	
	
	@Override
	public void run() {
	    if (System .getSecurityManager () == null) {
	        System .setSecurityManager (new SecurityManager ()); }
		try {
			LocateRegistry .createRegistry (1099);
			Naming .rebind ("//localhost/TwentyFour", this); 
			log ("listening..."); }
		catch (Exception e) {
			e .printStackTrace (); } } }
