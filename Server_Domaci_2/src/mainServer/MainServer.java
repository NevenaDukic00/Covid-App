package mainServer;

import java.net.ServerSocket;
import java.net.Socket;

import controller.UserController;

public class MainServer {

	public static void main(String[] args) {
		
		try(ServerSocket serverSocket = new ServerSocket(8888)) {
			while (true) {
				Socket socket = serverSocket.accept();
				UserController userController = new UserController(socket);
				new Thread(userController).start();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
