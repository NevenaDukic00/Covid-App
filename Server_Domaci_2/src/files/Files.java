package files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import javax.annotation.processing.SupportedSourceVersion;

import enums.Gender;
import enums.Vakcinisan;

public class Files {
	private BufferedWriter korisnikWrite;
	private BufferedWriter registracijaWrite;
	private BufferedReader korisnikRead;
	private BufferedReader registracijaRead;

	private int signedin = 0;

	public void addClient(int id, String username, String password, String firstName, String lastName, String jmbg,
			String email, Gender gender, Vakcinisan answer1, String doza1, Vakcinisan answer2, String doza2,
			Vakcinisan answer3, String doza3,String date1,String date2,String date3) {
		try {
			korisnikWrite = new BufferedWriter(new FileWriter(new File("korisnik"), true));
			registracijaWrite = new BufferedWriter(new FileWriter(new File("registracija"), true));

			korisnikWrite.write(id + " ; " + username + " ; " + password + "\n");
			registracijaWrite.write(id + " ; " + username + " ; " + password + " ; " + firstName + " ; " + lastName
					+ " ; " + jmbg + " ; " + email + " ; " + gender.name() + " ; " + answer1.toString() + " ; " + doza1 
					+ " ; " + answer2.toString() + " ; " + doza2 + " ; " + answer3.toString() + " ; " + doza3 + " ; " + date1 + " ; " + date2 + " ; " + date3 +"\n");

			korisnikWrite.close();
			registracijaWrite.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getNumUsers() {
		
		String numbers;
		int prvaDoza = 0;
		int drugaDoza = 0;
		int trecaDoza = 0;
		
		String druga_doza;
		int fajzer = 0;
		int sinofarm = 0;
		int astrazeneka = 0;
		int moderna = 0;
		try {
			registracijaRead = new BufferedReader(new FileReader(new File("registracija")));
			String user;
			try {
				
				while ((user=registracijaRead.readLine())!=null) {
					String pom []= user.split(" ; ");
//					System.out.println("USAO U PRIKUPLJANJE ZA ASTRAZENEKU");
					if (pom[8].equals("VAKCINISAN")) {
						prvaDoza++;
					}
					if (pom[10].equals("VAKCINISAN")) {
						drugaDoza++;
						if (pom[11].equals("Fajzer")) {
							fajzer++;
						}
						if (pom[11].equals("Sinofarm")) {
							sinofarm++;
						}
						if (pom[11].equals("Astra-Zeneka")) {
//							System.out.println("Usao u astrzazenetku");
							astrazeneka++;
						}
						if (pom[11].equals("Moderna")) {
							moderna++;
						}
					}
					if (pom[12].equals("VAKCINISAN")) {
						trecaDoza++;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		numbers = prvaDoza + " ; " + drugaDoza + " ; " + trecaDoza;
		druga_doza = fajzer + " ; " + sinofarm  + " ; " + astrazeneka + " ; "  + moderna; 
		return numbers + " * " + druga_doza;
	}
	public String sendInformation() throws IOException {
		
		String information = "";
		try {
			registracijaRead = new BufferedReader(new FileReader(new File("registracija")));
			
			String registracija;
//			System.out.println("USAO I FILES SENDINFORMATION");
			while ((registracija=registracijaRead.readLine())!=null) {
//				System.out.println("USAO U PRIKUPLJANJE PODATAKA");
				String pomocni[] = registracija.split(" ; ");
				if (pomocni.length==1) {
					break;
				}
				String brojac;
				if (pomocni[8].equals("VAKCINISAN") && pomocni[10].equals("VAKCINISAN")) {
					brojac = "2";
					if (pomocni[12].equals("VAKCINISAN")) {
						brojac = "3";
					}
					information = information +pomocni[5] + " ; " +  pomocni[3] + " ; " + pomocni[4] + " ; "  + brojac +  " ; "+ " IMA validnu kovid propusnicu" + " * ";
				}else {
					if (pomocni[8].equals("VAKCINISAN")) {
						brojac = "1";
					}else {
						brojac = "0";
					}
					information = information +pomocni[5] + " ; " +  pomocni[3] + " ; " + pomocni[4] + " ; "  + brojac +  " ; "+ " NEMA validnu kovid propusnicu" + " * ";
				}
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println("INFORMATION U FILES");
		
		return information;
	}
	
	public String[] checkUser(String username, String password) {

		
		try {
			System.out.println("Username: "  + username);
			System.out.println("Password: " + password);
			String korisnik;
			korisnikRead = new BufferedReader(new FileReader(new File("korisnik")));
			try {
				while ((korisnik = korisnikRead.readLine()) != null) {
					
					String user[] = korisnik.split(" ; ");
					
					if (user.length==1) {
						break;
					}
					
					if (user[1].equals(username) && user[2].equals(password)) {
						signedin = 1;
						break;
					}
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			korisnikRead.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String answers[] = new String[10];
		answers[0] = String.valueOf(signedin);
		
		if (signedin == 1) {
			try {
				String answers1[] = findClient(username, password);
				int k = 0;
				int a = 1;
				while (k < answers1.length) {
					
					answers[a++] = answers1[k];
					k++;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		return answers;
	}

	private String[] findClient(String username, String password) throws IOException {
		String find[] = null;
		try {
			registracijaRead = new BufferedReader(new FileReader(new File("registracija")));
			String registracija;

			while ((registracija = registracijaRead.readLine()) != null) {
				String user[] = registracija.split(" ; ");
				if (user[1].equals(username) && user[2].equals(password)) {
					find = Arrays.copyOfRange(user, 8, 17);
					break;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return find;
	}

	public void changeInformation(String username, String password, String information) {
		try {
//			System.out.println("Usao u change mood");
			
			registracijaRead = new BufferedReader(new FileReader(new File("registracija")));
			String current_registracija;
			
			String pomocni = "";
			try {
				while ((current_registracija = registracijaRead.readLine()) != null) {

					
					String current_info[] = current_registracija.split(" ; ");
					if (current_info.length==1) {
						break;
					}
					
					
					if (username.equals(current_info[1]) && password.equals(current_info[2])) {
						
						String change1[] = Arrays.copyOfRange(current_info, 0, 8);
						String change2[] = Arrays.copyOfRange(current_info, 14, 17);
						int k = 0;
//						System.out.println("*****************************");
						while (k<change1.length) {
//							System.out.println(change1[k]);
							k++;
						}
						while (k<change2.length) {
//							System.out.println(change2[k]);
							k++;
						}
//						System.out.println("*****************************");
						pomocni = pomocni + (change1[0] + " ; " + change1[1] + " ; " + change1[2] + " ; " + change1[3] + " ; "
								+ change1[4] + " ; " + change1[5] + " ; " + change1[6] + " ; " + change1[7] + " ; "
								+ information + "\n");
//						break;
						
					}else {
						pomocni = pomocni + current_registracija + "\n";
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
//			System.out.println(pomocni);
			registracijaWrite = new BufferedWriter(new FileWriter(new File("registracija"),false));
			registracijaWrite.write(pomocni);
			registracijaWrite.flush();
			registracijaWrite.close();
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getCovidSertificate(String username,String password) {
		
		String sending = "";
		try {
			registracijaRead = new BufferedReader(new FileReader(new File("registracija")));
			String pomocni = "";
			try {
				while ((pomocni=registracijaRead.readLine())!=null) {
					String user[] = pomocni.split(" ; ");
					if (user[1].equals(username) && user[2].equals(password)) {
						sending = pomocni;
							break;	
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sending;
	}
}
