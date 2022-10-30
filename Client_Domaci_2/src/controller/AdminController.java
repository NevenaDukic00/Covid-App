package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

import interfaces.AdminInterface;

public class AdminController implements Runnable {

	private Socket socket;
	private DataOutputStream outputStream;
	private DataInputStream inputStream;

	private AdminInterface adminInterface;

	public AdminController(Socket socket, DataOutputStream outputStream, DataInputStream inputStream) {
		this.socket = socket;
		this.outputStream = outputStream;
		this.inputStream = inputStream;
		getInformation();
		
	}

	

	@Override
	public void run() {
		try {
			while (true) {
				
				int message = inputStream.readInt();
				System.out.println("Message admin: " + message);
				
				switch (message) {
				case 4:
					String information = inputStream.readUTF();
					System.out.println("Admin information: " + information);
					adminInterface.sendInformation(information);
					break;
				case 8:
					String num = inputStream.readUTF();
					adminInterface.sendStat(num);
					break;
				case 10:
					return;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void logOff() {

		try {
			outputStream.writeInt(7);
			outputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getNumUsers() {
		try {
			outputStream.writeInt(8);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getInformation() {
		try {
			outputStream.writeInt(5);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setAdminInterface(AdminInterface adminInterface) {
		this.adminInterface = adminInterface;
	}

}
