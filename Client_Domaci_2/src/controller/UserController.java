package controller;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDate;

import javax.annotation.processing.SupportedSourceVersion;

import enums.Gender;
import enums.Vakcinisan;
import interfaces.ClientInterface;
import javafx.application.Platform;

public class UserController extends Thread{

	private Socket socket;

	private DataOutputStream outputStream;
	private DataInputStream inputStream;

	private ClientInterface clientInterface;
	
	private boolean connected = false;

	public UserController() {
	}

	public void connect() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					socket = new Socket("localhost", 8888);
					initStreams();
					connected = true;
					
					UserController.this.start();
					
				} catch (IOException e) {
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							clientInterface.error();

						}
					});

				}

			}
		}).start();
		
	}

	private void initStreams() throws IOException {
		outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		inputStream = new DataInputStream(new DataInputStream(socket.getInputStream()));
	}

	public boolean isConnected() {
		return connected;
	}

	public void registration(String username, String password, String firstName, String lastName, String jmbg,
			String email, Gender gender) {

		try {
			outputStream.writeInt(1);
			outputStream.writeUTF(username);
			outputStream.writeUTF(password);
			outputStream.writeUTF(firstName);
			outputStream.writeUTF(lastName);
			outputStream.writeUTF(jmbg);
			outputStream.writeUTF(email);
			outputStream.writeUTF(gender.name());
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void registraton2(Vakcinisan prvaDoza, String prvaD, Vakcinisan drugaDoza, String drugiD,
			Vakcinisan trevaDoza, String trecaD, String date1, String date2, String date3) {
		try {
			outputStream.writeUTF(prvaDoza.name());
			outputStream.writeUTF(prvaD);
			outputStream.writeUTF(drugaDoza.name());
			outputStream.writeUTF(drugiD);
			outputStream.writeUTF(trevaDoza.name());
			outputStream.writeUTF(trecaD);
			outputStream.writeUTF(date1.toString());
			outputStream.writeUTF(date2.toString());
			outputStream.writeUTF(date3.toString());

			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void checkUser(String username, String password) {
		try {
			outputStream.writeInt(2);
			outputStream.writeUTF(username);
			outputStream.writeUTF(password);
			outputStream.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void changeInformation(String information, String username, String password) {

		try {
			outputStream.writeInt(3);
			outputStream.writeUTF(username);
			outputStream.writeUTF(password);
			outputStream.writeUTF(information);
			outputStream.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getCovidSertificate(String username, String password) {

		try {
			outputStream.writeInt(9);
			outputStream.writeUTF(username);
			outputStream.writeUTF(password);
			outputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setClientInterface(ClientInterface clientInterface) {
		this.clientInterface = clientInterface;
	}

	public void logOff() {
		try {
			outputStream.writeInt(6);
			outputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {

		try {
			while (true) {

				int messageID = inputStream.readInt();
				System.out.println("Message id je: " + messageID);
				switch (messageID) {
				case 1:
					System.out.println("ADMIN");
					clientInterface.createAdmin(socket, outputStream, inputStream);
					return;
				case 2:
					int sign = inputStream.readInt();
					String answers[] = new String[10];
					answers[0] = String.valueOf(sign);
					if (sign == 1) {
						int k = 1;
						while (k <= 9) {
							try {
								answers[k] = inputStream.readUTF();
								k++;

							} catch (Exception e) {
								e.printStackTrace();
							}

						}

					}
					String username = inputStream.readUTF();
					String password = inputStream.readUTF();
					clientInterface.signin(answers, username, password);
					break;
				case 9:
					String info = inputStream.readUTF();
					clientInterface.cvidSertificate(info);
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
}
