package layout;


import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;

import java.util.Optional;

import javax.swing.JOptionPane;

import org.w3c.dom.events.EventException;

import interfaces.SigninInterface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Sigin extends VBox{

	private TextField userName;
	private PasswordField password;
	private Button button;
	private Button button_registracija;
	private GridPane gridPane;
	
	private SigninInterface signinInterface;
	
	private boolean register = false;
	public Sigin(int dim) {
		setSpacing(dim);
		init();
	}
	
	private void init() {
		initComponents();
		initLayout();
		initAction();
	}
	
	private void initComponents() {
		userName = new TextField();
		password = new PasswordField();
		button = new Button("Prijavi se");
		button_registracija = new Button("Registruj se");
		gridPane = new GridPane();
	}
	
	private void clearForm() {
		userName.setText("");
		password.setText("");
	}
	private void initAction() {
		
		button_registracija.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				clearForm();
				signinInterface.changeScreen();
				
			}
		});
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				signinInterface.checkSignIn(userName.getText().toString(),password.getText().toString() );
				
				
			}
		});
	}
	private void initLayout() {
		
		gridPane.setHgap(10);
		gridPane.setVgap(20);
		gridPane.setPadding(new Insets(15));
		gridPane.setAlignment(Pos.CENTER);
		
		Label label = new Label("Prijavi se!");
		label.setFont(new Font("TimesNewRoman",20));
		gridPane.add(label, 1, 0);
		
		Label label_userName = new Label("Korisnicko ime:");
		label_userName.setFont(new Font("Verdana",15));
		gridPane.add(label_userName, 0, 1);
		gridPane.add(userName, 1, 1);
		
		Label label_password = new Label("Sifra:");
		label_password.setFont(new Font("Verdana",15));
		gridPane.add(label_password, 0, 2);
		gridPane.add(password, 1, 2);
		
		VBox vBox = new VBox();
		vBox.setPrefWidth(100);
		vBox.setSpacing(10);
		button.setFont(new Font("Arial",12));
		button.setMinWidth(vBox.getPrefWidth());
		button_registracija.setFont(new Font("Arial",12));
		button_registracija.setMinWidth(vBox.getPrefWidth());
		vBox.getChildren().addAll(button,button_registracija);
		
	
		gridPane.add(vBox, 1, 3);
		
		getChildren().add(gridPane);
		
	}
	
	public void errorServer() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setHeaderText("Greska!");
		alert.setContentText("Server nije povezan!");
		Optional<ButtonType> result = alert.showAndWait();
		
	}

	public void showAlert() {
		JOptionPane.showMessageDialog(null, "Vasa sifra ili korisnicko ime nije ispravno!");
		
		
	}
	public void setSigninInterface(SigninInterface signinInterface) {
		this.signinInterface = signinInterface;
	}
	

}
