package garbage.connector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import garbage.Session;
import garbage.TwentyFourServerState;
import garbage.TwentyFourServerStateException;
import garbage.User;
import garbage.UserInfo;

public class TwentyFourServerStateJDBC implements TwentyFourServerState {
	Connection _connection;

	public TwentyFourServerStateJDBC () throws SQLException {
		this ("sophia.cs.hku.hk", "wjchen", "wjchen", "BVuJBYMF"); }
	public TwentyFourServerStateJDBC (String host, String database, String userName, String password) throws SQLException {
		this ._connection = DriverManager .getConnection ( "jdbc:mysql://" + host + "/" + database, userName, password ); }
	
	
	@Override
	public boolean userExists (String loginName) throws TwentyFourServerStateException {
		try (
		PreparedStatement query = this ._connection .prepareStatement
			( "SELECT 1 FROM `user` WHERE `loginName` = ?" )
		) {
			query .setString (1, loginName);
			ResultSet results = query .executeQuery ();
			return results .next (); }
		catch (SQLException e) {
			throw new TwentyFourServerStateException (e .getMessage ()); } }
	@Override
	public User newUser (String loginName, String password) throws TwentyFourServerStateException {
		try (
		PreparedStatement query = this ._connection .prepareStatement
			( "INSERT INTO `user` (`loginName`, `password`) VALUES (?, ?)" ) 
		) {
			query .setString (1, loginName);
			query .setString (2, password);
			query .execute ();
			User user = new User (loginName, password);
			return user; }
		catch (SQLException e) {
			throw new TwentyFourServerStateException (e .getMessage ()); } }
	@Override
	public boolean userAuthenticated (String loginName, String password) throws TwentyFourServerStateException {
		try (
		PreparedStatement query = this ._connection .prepareStatement
			( "SELECT 1 FROM `user` WHERE `loginName` = ? AND `password` = ?" )
		) {
			query .setString (1, loginName);
			query .setString (2, password);
			ResultSet results = query .executeQuery ();
			return results .next (); }
		catch (SQLException e) {
			throw new TwentyFourServerStateException (e .getMessage ()); } }
	@Override
	public User getUser (String loginName) throws TwentyFourServerStateException {
		try (
		PreparedStatement query = this ._connection .prepareStatement
			( "SELECT `password` FROM `user` WHERE `loginName` = ?" )
		) {
			query .setString (1, loginName);
			ResultSet results = query .executeQuery ();
			if (results .next ()) {
				String password = results .getString (1);
				User user = new User (loginName, password);
				return user; }
			else {
				throw new TwentyFourServerStateException ("User does not exist!"); } }
		catch (SQLException e) {
			throw new TwentyFourServerStateException (e .getMessage ()); } }
	@Override
	public UserInfo getUserInfo (String loginName) throws TwentyFourServerStateException {
		try (
		PreparedStatement query = this ._connection .prepareStatement
			( "SELECT `winsCount`, `gamesCount`, `winTimesAverage` FROM `userInfo` WHERE `loginName` = ?" )
		) {
			query .setString (1, loginName);
			ResultSet results = query .executeQuery ();
			if (results .next ()) {
				int winsCount = results .getInt (1);
				int gamesCount = results .getInt (2);
				float winTimesAverage = results .getFloat (3);
				UserInfo userInfo = new UserInfo (winsCount, gamesCount, winTimesAverage);
				return userInfo; }
			else {
				throw new TwentyFourServerStateException ("User does not exist!"); } }
		catch (SQLException e) {
			throw new TwentyFourServerStateException (e .getMessage ()); } }

	@Override
	public void deleteUser (User user) throws TwentyFourServerStateException {
		try (
		PreparedStatement query = this ._connection .prepareStatement
			( "DELETE FROM `user` WHERE `loginName` = ?" )
		) {
			String loginName = user .loginName;
			query .setString (1, loginName);
			query .execute (); }
		catch (SQLException e) {
			throw new TwentyFourServerStateException (e .getMessage ()); } }

	@Override
	public boolean sessionExists (String loginName) throws TwentyFourServerStateException {
		try (
		PreparedStatement query = this ._connection .prepareStatement
			( "SELECT 1 FROM `session` WHERE `loginName` = ?" )
		) {
			query .setString (1, loginName);
			ResultSet results = query .executeQuery ();
			return results .next (); }
		catch (SQLException e) {
			throw new TwentyFourServerStateException (e .getMessage ()); } }
	@Override
	public Session newSession (User user) throws TwentyFourServerStateException {
		try (
		PreparedStatement query = this ._connection .prepareStatement
			( "INSERT INTO `session` (`loginName`, `uuid`) VALUES (?, ?)" )
		) {
			Session session = new Session (user);
			query .setString (1, session .loginName);
			query .setString (2, session .uuid .toString());
			query .execute ();
			return session; }
		catch (SQLException e) {
			throw new TwentyFourServerStateException (e .getMessage ()); } }
	@Override
	public Session getSession (UUID uuid) throws TwentyFourServerStateException {
		try (
		PreparedStatement query = this ._connection .prepareStatement
			( "SELECT `loginName` FROM `session` WHERE `uuid` = ?" )
		) {
			query .setString (1, uuid .toString ());
			ResultSet results = query .executeQuery ();
			if (results .next ()) {
				String loginName = results .getString (1);
				Session session = new Session (this .getUser (loginName), uuid);
				return session; }
			else {
				throw new TwentyFourServerStateException ("Session does not exist!"); } }
		catch (SQLException e) {
			throw new TwentyFourServerStateException (e .getMessage ()); } }
	@Override
	public void deleteSession (Session session) throws TwentyFourServerStateException {
		try (
		PreparedStatement query = this ._connection .prepareStatement
			( "DELETE FROM `session` WHERE `loginName` = ?" )
		) {
			String loginName = session .loginName;
			query .setString (1, loginName);
			query .execute (); } 
		catch (SQLException e) {
			throw new TwentyFourServerStateException (e .getMessage ()); } } }
