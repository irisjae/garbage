package garbage;

import java.util.HashMap;
import java.util.Map;

public class TwentyFourServerState {
	
	Map<String, User> users = new HashMap <String, User> ();
	Map<User, Session> sessions = new HashMap <User, Session> ();
	
	public TwentyFourServerState () {
		TwentyFourServerStateFiles .readUserInfo (this);
		TwentyFourServerStateFiles .writeOnlineUser (this); }
	
	
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
		TwentyFourServerStateFiles .writeUserInfo (this);
		return user; }
	void deleteUser (User user) {
		var loginName = user .loginName;
		users .remove (loginName);
		TwentyFourServerStateFiles .writeUserInfo (this); }
	Session newSession (User user) {
		Session session = new Session (user);
		sessions .put (user, session);
		TwentyFourServerStateFiles .writeOnlineUser (this);
		return session; }
	void deleteSession (Session session) {
		var loginName = session .loginName;
		User user = users .get (loginName);
		sessions .remove (user);
		TwentyFourServerStateFiles .writeOnlineUser (this); } }