package garbage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

public class TwentyFourServerStateFS implements TwentyFourServerState {
	static Path userInfo = Paths .get ("UserInfo.txt");
	static Path onlineUser = Paths .get ("OnlineUser.txt");

	Map<String, User> _users = new HashMap ();
	Map<String, UserInfo> _userInfos = new HashMap ();
	Map<User, Session> _sessions = new HashMap ();
	
	public TwentyFourServerStateFS () throws IOException {
		this .readUserInfo ();
		this .writeOnlineUser (); }
	
	void readUserInfo () throws IOException {
		try {
			this ._users =
				Files .lines (TwentyFourServerStateFS .userInfo)
				.map (line -> {
					var parts = line .split (" ");
					var loginName = parts [0];
					var password = parts [1];
					
					var user = new User (loginName, password);
					return Map .entry (loginName, user); })
				.collect (Collectors .toMap (Map .Entry ::getKey, Map .Entry ::getValue)); 
			this ._userInfos =
				Files .lines (TwentyFourServerStateFS .userInfo)
				.map (line -> {
					var parts = line .split (" ");
					var loginName = parts [0];
					var winsCount = Integer .parseInt (parts [2]);
					var gamesCount = Integer .parseInt (parts [3]);
					var winsTimesAverage = Float .parseFloat (parts [4]);
					
					var userInfo = new UserInfo (winsCount, gamesCount, winsTimesAverage);
					return Map .entry (loginName, userInfo); })
				.collect (Collectors .toMap (Map .Entry ::getKey, Map .Entry ::getValue)); }
		catch (NoSuchFileException e) {
			this ._users = new HashMap <String, User> (); } }
	void readOnlineUser () throws IOException {
		try {
			this ._sessions =
				Files .lines (TwentyFourServerStateFS .onlineUser)
				.map (line -> {
					var parts = line .split (" ");
					var loginName = parts [0];
					var uuid = UUID .fromString (parts [1]);
	
					var user = this ._users .get (loginName);
					var session = new Session (user, uuid);
					return Map .entry (user, session); })
				.collect (Collectors .toMap (Map .Entry ::getKey, Map .Entry ::getValue)); }
		catch (NoSuchFileException e) {
			this ._sessions = new HashMap <User, Session> (); } } 
	void writeUserInfo () throws IOException {
		Files .write (TwentyFourServerStateFS .userInfo
			, this ._users .values ()
				.stream ()
				.map (user -> {
					var userInfo = this ._userInfos .get (user .loginName);
					return user .loginName + " " + user .password + " " + userInfo .winsCount + " " + userInfo .gamesCount + " " + userInfo .winsTimesAverage; } )
				.collect (Collectors .toList ())
			, StandardCharsets .UTF_8); }
	void writeOnlineUser () throws IOException {
		Files .write (TwentyFourServerStateFS .onlineUser
			, this ._sessions .values ()
				.stream ()
				.map (session -> session .loginName + " " + session .uuid)
				.collect (Collectors .toList ())
			, StandardCharsets .UTF_8); }
	
	

	@Override
	public boolean userExists (String loginName) {
		return this ._users .containsKey (loginName); }
	@Override
	public User newUser (String loginName, String password) throws TwentyFourServerStateException {
		try {
			var user = new User (loginName, password);
			var userInfo = new UserInfo ();
			this ._users .put (loginName, user);
			this ._userInfos .put (loginName, userInfo);
			this .writeUserInfo ();
			return user; }
		catch (IOException e) {
			throw new TwentyFourServerStateException (e .getMessage ()); } }
	@Override
	public boolean userAuthenticated (String loginName, String password) {
		return userExists (loginName) && this ._users .get (loginName) .password .equals (password); }
	@Override
	public User getUser (String loginName) throws TwentyFourServerStateException {
		var user = _users .get (loginName);
		return user; }
	@Override
	public UserInfo getUserInfo(String loginName) throws TwentyFourServerStateException {
		var user = _userInfos .get (loginName);
		return user; }
	@Override
	public void deleteUser (User user) throws TwentyFourServerStateException {
		try {
			var loginName = user .loginName;
			_users .remove (loginName);
			_userInfos .remove (loginName);
			this .writeUserInfo (); }
		catch (IOException e) {
			throw new TwentyFourServerStateException (e .getMessage ()); } }
	
	@Override
	public boolean sessionExists (String loginName) {
		return this ._users .containsKey (loginName)
			&& this ._sessions .containsKey (this ._users .get (loginName)); }
	@Override
	public Session newSession (User user) throws TwentyFourServerStateException {
		try {
			var session = new Session (user);
			this ._sessions .put (user, session);
			this .writeOnlineUser ();
			return session; }
		catch (IOException e) {
			throw new TwentyFourServerStateException (e .getMessage ()); } }
	@Override
	public Session getSession (UUID uuid) throws TwentyFourServerStateException {
		try {
			var session = this ._sessions .values () .stream ()
				.filter (sessionB -> sessionB .uuid .equals (uuid)) .findAny () .get ();
			return session; }
		catch (NoSuchElementException e) {
			throw new TwentyFourServerStateException (e .getMessage ()); } }
	@Override
	public void deleteSession (Session session) throws TwentyFourServerStateException {
		try {
			var loginName = session .loginName;
			User user = _users .get (loginName);
			_sessions .remove (user);
			this .writeOnlineUser (); }
		catch (IOException e) {
			throw new TwentyFourServerStateException (e .getMessage ()); } } }
