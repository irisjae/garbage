package garbage;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Optional;

import javax.swing.JFrame;

public class TwentyFourClient implements Runnable {

	TwentyFourProtocol _protocol;
	JFrame _frame;

	Reactive <String> panel = new Reactive <String> ();
	Reactive <Optional <Session>> session = new Reactive <Optional <Session>> (Optional .empty ());
	
	
	public TwentyFourClient () {
		this ._frame = TwentyFourClientFrame .from (this);}
	
	
	
	TwentyFourProtocol protocol () throws RemoteException, MalformedURLException, NotBoundException {
		try {
			this ._protocol .ping (); }
		catch (Exception e) {
			this ._protocol = (TwentyFourProtocol) Naming .lookup("//localhost/TwentyFour"); }
		return this ._protocol; }	
	
	@Override
	public void run() {
		this ._frame .setVisible (true); } }