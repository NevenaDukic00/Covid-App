package layout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.Optional;

import com.sun.javafx.scene.control.skin.Utils;

import controller.AdminController;
import controller.UserController;
import entity.User;
import enums.Gender;
import enums.Vakcinisan;
import interfaces.AdminInterface;
import interfaces.AdminLayouInterface;
import interfaces.ClientInterface;
import interfaces.QuestionsInterfaces;
import interfaces.RegistratitonInterface;
import interfaces.SigninInterface;
import interfaces.UserLayoutInterface;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Layout {

	private Stage primaryStage;
	private Scene scene;
	private Registration registration;
	private Sigin sigin;
	private Questions questions;

	private UserController client;
	private UserLayout userLayout;

	private AdminLayout adminLayout;
	private AdminController adminController;

	public Layout(Stage primaryStage) {
		this.primaryStage = primaryStage;
		init();
	}

	private void init() {
		initComponenets();
		initStageAction();
		client = new UserController();
		client.connect();
		initAction();
	}

	private void initComponenets() {
		registration = new Registration(10);
		sigin = new Sigin(5);
		scene = new Scene(sigin, 400, 400);
		userLayout = new UserLayout(10);
		adminLayout = new AdminLayout(10);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Kovid app");
		primaryStage.show();
					
			
		

		
	}

	private void initStageAction() {

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				if (client != null && client.isConnected()) {
					client.logOff();
				} else if (adminController != null) {
					adminController.logOff();
				}else {
					Platform.exit();
				}

			}
		});
	}

	private void initAction() {

		sigin.setSigninInterface(new SigninInterface() {

			@Override
			public void changeScreen() {
				scene.setRoot(registration);

			}

			@Override
			public void checkSignIn(String username, String password) {
				client.checkUser(username, password);

			}

		});

		registration.setRegistratitonInterface(new RegistratitonInterface() {

			@Override
			public void showDialog(String username, String password, String first_name, String last_name, String email,
					String jmbg, Gender gender) {
				questions = new Questions();
				questions.setQuestionsInterfaces(new QuestionsInterfaces() {

					@Override
					public void sendInformation(Vakcinisan answer1, String doza1, Vakcinisan answer2, String doza2,
							Vakcinisan answer3, String doza3, String date1, String date2, String date3) {

						client.registration(username, password, first_name, last_name, jmbg, email, gender);
						client.registraton2(answer1, doza1, answer2, doza2, answer3, doza3, date1, date2, date3);
						registration.clearRegistration();
					}

				});

			}

			@Override
			public void back() {
				scene.setRoot(sigin);

			}

		});

		client.setClientInterface(new ClientInterface() {

			@Override
			public void signin(String[] register, String username, String password) {
				if (Integer.parseInt(register[0]) == 1) {
					userLayout.fillTable(register, username, password);
					scene.setRoot(userLayout);
				} else {
					sigin.showAlert();
				}

			}

			@Override
			public void createAdmin(Socket socket, DataOutputStream outputStream, DataInputStream inputStream) {
				adminController = new AdminController(socket, outputStream, inputStream);
				

				adminController.setAdminInterface(new AdminInterface() {

					@Override
					public void sendInformation(String information) {
						System.out.println("FILL TABLE");
						adminLayout.fillTable(information);
						scene.setRoot(adminLayout);
					}

					@Override
					public void sendStat(String stat) {
						adminLayout.Stat(stat);
					}
				});

				adminLayout.setAdminLayouInterface(new AdminLayouInterface() {

					@Override
					public void getStat() {
						adminController.getNumUsers();
					}
				});
				new Thread(adminController).start();
				

			}

			@Override
			public void cvidSertificate(String information) {
				userLayout.covidSertificate(information);

			}

			@Override
			public void error() {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setHeaderText("Greska!");
				alert.setContentText("Server nije povezan!");
				Optional<ButtonType> result = alert.showAndWait();
				Platform.exit();
				
			}
		});

		userLayout.setUserLayoutInterface(new UserLayoutInterface() {

			@Override
			public void changeInformation(String information, String username, String passsword) {
				client.changeInformation(information, username, passsword);

			}

			@Override
			public void getCovidSertificate(String username, String password) {
				client.getCovidSertificate(username, password);

			}
		});

	}
}
