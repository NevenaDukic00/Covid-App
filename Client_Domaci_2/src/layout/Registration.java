package layout;

import javafx.scene.control.Label;
import enums.Gender;
import interfaces.RegistratitonInterface;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Registration extends VBox{

	private TextField username;
	private PasswordField password;
	private TextField first_name;
	private TextField last_name;
	private TextField email;
	private TextField jmbg;
	private Button button;
	private GridPane gridPane;
	private RadioButton muski;
	private RadioButton zenski;
	private ToggleGroup toggleGroup;
	
	private Button buttonBack;
	
	RegistratitonInterface registratitonInterface;
	
	
	public Registration(int dim) {
		setSpacing(dim);
		init();
	}
	
	private void init() {
		initComponents();
		initLayout();
		initProperties();
		initAction();
	}
	
	private void initComponents() {
		
		username = new TextField();
		first_name = new TextField();
		last_name = new TextField();
		email = new TextField();
		jmbg = new TextField();
		password = new PasswordField();
		button = new Button("Dalje");
		zenski = new RadioButton("zenski");
		muski = new RadioButton("muski");
		toggleGroup = new ToggleGroup();
		gridPane = new GridPane();
		buttonBack = new Button("Nazad");
	}
	
	private void initProperties() {
		first_name.setPrefSize(200, 10);
		last_name.setPrefSize(200, 10);
		username.setPrefSize(200, 10);
		email.setPrefSize(200, 10);
		muski.setToggleGroup(toggleGroup);
		zenski.setToggleGroup(toggleGroup);
		
		muski.setUserData(Gender.MUSKI);
		zenski.setUserData(Gender.ZENSKI);
	}
	private void initAction() {
		button.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				
				if (username.getLength()==0 || password.getLength()==0 || first_name.getLength()==0 || last_name.getLength()==0 || email.getLength()==0 || jmbg.getLength()==0 ||  (muski.getToggleGroup().getSelectedToggle()==null || zenski.getToggleGroup().getSelectedToggle()==null) ) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setHeaderText("Greska!");
					alert.setContentText("Sva polja moraju biti popunjena!");
					alert.showAndWait();
				}else {
					registratitonInterface.showDialog(username.getText().toString(),password.getText().toString(), first_name.getText().toString(), last_name.getText().toString(), email.getText().toString(), jmbg.getText().toString(),(Gender)toggleGroup.getSelectedToggle().getUserData());
				}
				
				
				
				
			}
		});
		
		buttonBack.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				clearRegistration();
				registratitonInterface.back();
				
			}
		});
	}
	public void clearRegistration() {
		username.setText("");
		password.setText("");
		email.setText("");
		jmbg.setText("");
		first_name.setText("");
		last_name.setText("");
		muski.setSelected(false);
		zenski.setSelected(false);
	}
	private void initLayout() {
		
		gridPane.setHgap(10);
		gridPane.setVgap(20);
		gridPane.setPadding(new Insets(10));
		gridPane.setAlignment(Pos.CENTER);
		
		
		Label label = new Label("Registracija");
		label.setFont(new Font("TimesNewRoman",15));
		gridPane.add(label, 1, 0);
		
		Label label_firstName = new Label("Ime:");
		label_firstName.setFont(new Font("Verdana",12));
		gridPane.add(label_firstName, 0, 1);
		gridPane.add(first_name, 1, 1);
		
		Label label_lastName = new Label("Prezime:");
		label_lastName.setFont(new Font("Verdana",12));
		gridPane.add(label_lastName, 0, 2);
		gridPane.add(last_name, 1, 2);
		
		Label label_email = new Label("Email:");
		label_email.setFont(new Font("Verdana",12));
		gridPane.add(label_email, 0, 3);
		gridPane.add(email, 1, 3);
		
		Label label_userName = new Label("Korisnicko ime:");
		label_userName.setFont(new Font("Verdana",12));
		gridPane.add(label_userName, 0, 4);
		gridPane.add(username, 1, 4);
		
		Label label_password = new Label("Sifra:");
		label_password.setFont(new Font("Verdana",12));
		gridPane.add(label_password, 0, 5);
		gridPane.add(password, 1, 5);
		
		Label label_jmbg = new Label("JMBG:");
		label_jmbg.setFont(new Font("Verdana",12));
		gridPane.add(label_jmbg, 0, 6);
		gridPane.add(jmbg, 1, 6);
		
		Label label_gender = new Label("Pol:");
		label_gender.setFont(new Font("Verdana",12));
		gridPane.add(label_gender, 0, 7);
		HBox hBox = new HBox(15);
		hBox.setAlignment(Pos.CENTER);
	
		hBox.getChildren().addAll(muski,zenski);
		gridPane.add(hBox, 1, 7);
	
		HBox hBox2 = new HBox();
		hBox2.setSpacing(80);
		buttonBack.setFont(new Font("Arial",12));
		button.setFont(new Font("Arial",12));
		hBox2.getChildren().addAll(buttonBack,button);
		
		gridPane.add(hBox2, 1, 8);

		getChildren().add(gridPane);

		
	}
	
	
	public void setRegistratitonInterface(RegistratitonInterface registratitonInterface) {
		this.registratitonInterface = registratitonInterface;
	}

}
