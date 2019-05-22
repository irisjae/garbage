package garbage;

import java.util.UUID;

public interface TwentyFourServerState {	
	
	boolean userExists (String loginName) throws TwentyFourServerStateException;	
	User newUser (String loginName, String password) throws TwentyFourServerStateException;
	boolean userAuthenticated (String loginName, String password) throws TwentyFourServerStateException;
	User getUser (String loginName) throws TwentyFourServerStateException;
	UserStat getUserStat (String loginName) throws TwentyFourServerStateException;
	void userJoinedGame (String loginName) throws TwentyFourServerStateException;
	void userWonGame (String loginName) throws TwentyFourServerStateException;
	void deleteUser (User user) throws TwentyFourServerStateException;
	
	boolean sessionExists (String loginName) throws TwentyFourServerStateException;
	Session newSession (User user) throws TwentyFourServerStateException;
	Session getSession (UUID uuid) throws TwentyFourServerStateException;
	void deleteSession (Session session) throws TwentyFourServerStateException; }