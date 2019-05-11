package garbage;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Collectors;

public class TwentyFourServerStateFiles {
	static Path userInfo = Paths .get ("UserInfo.txt");
	static Path onlineUser = Paths .get ("OnlineUser.txt");

	public static void readUserInfo (TwentyFourServerState state) {
		try {
			Files .lines (userInfo) .forEach (line -> {
				var parts = line .split (" ");
				var loginName = parts [0];
				var password = parts [1];
				
				var user = new User (loginName, password);
				state ._users .put (loginName, user); }); }
		catch (NoSuchFileException e) {}
		catch (Exception e) {
			e .printStackTrace (); } }
	public static void readOnlineUser (TwentyFourServerState state) {
		try {
			Files .lines (onlineUser) .forEach (line -> {
				var parts = line .split (" ");
				var loginName = parts [0];
				var uuid = UUID .fromString (parts [1]);

				var user = state ._users .get (loginName);
				var session = new Session (user, uuid);
				state ._sessions .put (user, session); }); }
		catch (NoSuchFileException e) {}
		catch (Exception e) {
			e .printStackTrace (); } } 
	public static void writeUserInfo (TwentyFourServerState state) {
		try {
			Files .write (userInfo
				, state ._users .values ()
					.stream ()
					.map (user -> user .loginName + " " + user .password)
					.collect (Collectors .toList ())
				, StandardCharsets .UTF_8); }
		catch (Exception e) {
			e .printStackTrace (); } }
	public static void writeOnlineUser (TwentyFourServerState state) {
		try {
			Files .write (onlineUser
				, state ._sessions .values ()
					.stream ()
					.map (session -> session .loginName + " " + session .uuid)
					.collect (Collectors .toList ())
				, StandardCharsets .UTF_8); }
		catch (Exception e) {
			e .printStackTrace (); } } }
