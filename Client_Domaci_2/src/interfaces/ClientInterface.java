package interfaces;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public interface ClientInterface {

	public void signin(String [] register,String username, String password);
	public void createAdmin(Socket socket, DataOutputStream outputStream, DataInputStream inputStream);
	public void cvidSertificate(String information);
	public void error();
}
