package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;

import javax.annotation.processing.SupportedSourceVersion;

import enums.Gender;
import enums.Vakcinisan;
import files.Files;

public class UserController implements Runnable {

	private Socket socket;

	private DataInputStream inputStream;
	private DataOutputStream outputStream;

	Files files;

	private static int id = 0;

	private int signedIn = 0;

	public UserController(Socket socket) {
		this.socket = socket;
		id++;
		try {
			outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			files = new Files();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void registration() {

		try {
			String userName = inputStream.readUTF();
			String password = inputStream.readUTF();
			String firstName = inputStream.readUTF();
			String lastName = inputStream.readUTF();
			String jmbg = inputStream.readUTF();
			String email = inputStream.readUTF();
			Gender gender = Gender.valueOf(inputStream.readUTF());
			Vakcinisan answer1 = Vakcinisan.valueOf(inputStream.readUTF());
			String prvaDoza = inputStream.readUTF();
			Vakcinisan answer2 = Vakcinisan.valueOf(inputStream.readUTF());
			String drugaDoza = inputStream.readUTF();
			Vakcinisan answer3 = Vakcinisan.valueOf(inputStream.readUTF());
			String trecaDoza = inputStream.readUTF();
			String datumPrveVakcinacije = inputStream.readUTF();
			String datumDrugeVakcinacije = inputStream.readUTF();
			String datumTreceVakcinacije = inputStream.readUTF();
			files.addClient(id, userName, password, firstName, lastName, jmbg, email, gender, answer1, prvaDoza,
					answer2, drugaDoza, answer3, trecaDoza,datumPrveVakcinacije,datumDrugeVakcinacije,datumTreceVakcinacije);

			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void checkUser() {

		try {
			String username = inputStream.readUTF();
			String password = inputStream.readUTF();

			if (username.equals("admin") && password.equals("admin")) {
				outputStream.writeInt(1);
				outputStream.flush();
				return;
			}
			
			String answers[] = files.checkUser(username, password);
			int signedin = Integer.parseInt(answers[0]);
		
			outputStream.writeInt(2);
			outputStream.writeInt(signedin);

			if (signedin == 1) {
				int k = 1;
				while (k < answers.length) {
					outputStream.writeUTF(answers[k]);
					k++;
				}

			}
			outputStream.writeUTF(username);
			outputStream.writeUTF(password);
			outputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void changeInformation() {

		try {
			String username = inputStream.readUTF();
			String password = inputStream.readUTF();
			String information = inputStream.readUTF();
//			System.out.println("Usao u chane info");
//			System.out.println(information);
//			System.out.println(username);
//			System.out.println(password);
			files.changeInformation(username, password, information);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void sendInformation() {
		try {
			String information = files.sendInformation();
//			System.out.println("Informacija je: " + information);
			outputStream.writeInt(4);
//			System.out.println("Salje info");
			outputStream.writeUTF(information);
			outputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void logOff() {
		try {
			outputStream.writeInt(10);
			outputStream.flush();
			socket.close();
			inputStream.close();
			outputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void getNumUsers() {

		String num = files.getNumUsers();
		try {
//			System.out.println("Server salje num: " + num);
			outputStream.writeInt(8);
			outputStream.writeUTF(num);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private void getCovidSertificate() {
		
		try {
			String username = inputStream.readUTF();
			String password = inputStream.readUTF();
			
			String covid = files.getCovidSertificate(username, password);
			outputStream.writeInt(9);
			outputStream.writeUTF(covid);
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
				int message = inputStream.readInt();
				System.out.println("Message je: " + message);
				switch (message) {
				case 1:
					System.out.println("REGISTRATION");
					registration();
					break;
				case 2:
					System.out.println("CHECK USER");
					checkUser();
					break;
				case 3:
					System.out.println("CHANGE INFORMATION");
					changeInformation();
					break;
				case 5:
					System.out.println("SEND INFORMATION");
					sendInformation();
					break;
				case 6:
					System.out.println("LOG OFF");
					logOff();
					return;
				case 7:
					System.out.println("LOG OFF");
					logOff();
					return;
				case 8:
					System.out.println("GET INFORMATION FOR ADMIN");
					getNumUsers();
					break;
				case 9:
					getCovidSertificate();
					break;
				default:
					break;
				}

			}

		} catch (Exception e) {
			System.err.println("Connection is broken!");
		}

	}
}
