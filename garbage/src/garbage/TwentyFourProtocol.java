package garbage;

import java.rmi.RemoteException;

public interface TwentyFourProtocol extends java.rmi.Remote {
	public Void VOID = (Void) null;
	public ProtocolResult <Session> register (String loginName, String password) throws RemoteException;
	public ProtocolResult <Session> login (String loginName, String password) throws RemoteException;
	public ProtocolResult <Void> logout (Session session) throws RemoteException;
	
	public void ping () throws RemoteException;}