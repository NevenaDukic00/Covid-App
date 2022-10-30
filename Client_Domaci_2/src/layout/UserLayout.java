package layout;

import java.awt.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import entity.User;
import enums.Vakcinisan;
import interfaces.UserLayoutInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.skin.ChoiceBoxSkin;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class UserLayout extends VBox{

	private TableView table;
	private TableColumn broj_vakcine;
	private TableColumn statu_primljena;
	private TableColumn naziv;
	private TableColumn datumPrimanjVakcine;
	
	private String information;
	private Button changeAnswer ;
	private Button validna_propusnica;
	private Menu menu;
	private MenuItem item1;
	private MenuItem item2;
	private MenuItem item3;
	private String username;
	private String password;
	private UserLayoutInterface userLayoutInterface;
	public final ObservableList<User> date = FXCollections.observableArrayList();
	
	private DatePicker datum_vakcinacije;
	private LocalDate minDate = LocalDate.of(2021, 1, 1);
	private LocalDate maxDate;
	
	private Button ok;
	private ButtonType cancel;
	private Dialog dialog;
	
	private LocalDate date1;
	
	
	public UserLayout(int dim) {
		setSpacing(dim);
		init();
	}
	
	private void init() {
		initComponents();
		
	}
	
	private void initComponents() {
		table = new TableView<User>();
		broj_vakcine = new TableColumn<User,String>("Doza vakcine:");
		statu_primljena = new TableColumn<User,String>("Status: ");
		naziv = new TableColumn<>("Naziv vakcine:");
		datumPrimanjVakcine = new TableColumn<>("Datum vakcinacije:");
		menu = new Menu("Meni");
		item1 = new MenuItem("Promena statusa vakcina");
		item2 = new MenuItem("Validnost kovid propusnice");
		item3 = new MenuItem("Kovid propusnica");
		
		ok = new Button("OK");
		cancel = new ButtonType("Cancel",ButtonData.CANCEL_CLOSE);
		
		datum_vakcinacije = new DatePicker();
		maxDate = LocalDate.of(2021, 12, 31);

		datum_vakcinacije.setDayCellFactory(d -> new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);
				setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
			}
		});
		
	}
	
	
	private void initLayout() {
		
		broj_vakcine.setMinWidth(100);
		broj_vakcine.setCellValueFactory(new PropertyValueFactory<User,String>("doza"));
	
	
		statu_primljena.setMinWidth(108);
	
		statu_primljena.setCellValueFactory(new PropertyValueFactory<User,String>("answer"));
		
		
		naziv.setMinWidth(100);
	
		naziv.setCellValueFactory(new PropertyValueFactory<User,String>("vakcina"));
	
		datumPrimanjVakcine.setMinWidth(100);
		datumPrimanjVakcine.setCellValueFactory(new PropertyValueFactory<User,String>("datum"));
		
	
		table.getColumns().add(broj_vakcine);
		table.getColumns().add(statu_primljena);
		table.getColumns().add(naziv);
		table.getColumns().add(datumPrimanjVakcine);
		table.setItems(date);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		
		
		menu.getItems().addAll(item1,item2,item3);
		MenuBar menuBar = new MenuBar(menu);
		
		getChildren().addAll(menuBar,table);
		

		
	}
	private void initAction() {
		item1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				User user1 = date.get(0);
				if (user1.getAnswer().equals("NEVAKCINISAN")) {
					
					DialogAlert("Promenite status prve vakcine u primljenu i izaberite koju ste primili:",0,null);
					datum_vakcinacije.setValue(null);
					
//					System.out.println(information);
				}
				User user2 = date.get(1);
			
				if (user1.getAnswer().equals("VAKCINISAN") &&user2.getAnswer().equals("NEVAKCINISAN")) {
					
					minDate = calculateDate(21, LocalDate.parse(date.get(0).getDatum()));
					DialogAlert1("Promenite status druge vakcine u primljenu i izaberite koju ste primili:",1,user1.getVakcina(),date.get(1).getDatum());
					datum_vakcinacije.setValue(null);
					
				}
				User user3 = date.get(2);
				if (user1.getAnswer().equals("VAKCINISAN") && user2.getAnswer().equals("VAKCINISAN") && user3.getAnswer().equals("NEVAKCINISAN")) {
					minDate = calculateMonths(LocalDate.parse(date.get(1).getDatum()));
					DialogAlert("Promenite status trece vakcine u primljenu i izaberite koju ste primili:",2,date.get(2).getDatum());
					datum_vakcinacije.setValue(null);
				
				}
				if (user1.getAnswer().equals("VAKCINISAN") && user2.getAnswer().equals("VAKCINISAN") && user3.getAnswer().equals("VAKCINISAN")) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setHeaderText("Primljene doze");
					alert.setContentText("Primili ste sve tri doze vakcine!");
					alert.show();
				}
//				System.out.println(information);
				
			}
			
			
		});
		
		item2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				User user1 = date.get(0);
				User user2 = date.get(1);
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.getDialogPane().setMinWidth(500);
				alert.setHeaderText("Posedovanje validne kovid propusnice");
				if (user1.getAnswer().equals("VAKCINISAN") && user2.getAnswer().equals("VAKCINISAN")) {
					alert.setContentText("Posedujete validnu kovid propusnicu!");
				}else {
					alert.setContentText("Ne posedujete validnu kovid propusnicu!");
				}
				alert.show();
			}
		});
		
		item3.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (date.get(0).getAnswer().equals("VAKCINISAN") && date.get(1).getAnswer().equals("VAKCINISAN")) {
					userLayoutInterface.getCovidSertificate(username, password);
				}else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.getDialogPane().setMinWidth(500);
					alert.setHeaderText("Kovid sertifikat");
					alert.setContentText("Ne mozete odstampati kovid sertifikat jer nemate validnu kovid propusnicu!");
					alert.show();
				}
				
			}
		});
		 
	}
	private void DialogAlert1(String poruka,int index,String choice, String datum) {
		
		dialog = new Dialog();
		
		ComboBox comboBox = new ComboBox();
		comboBox.getItems().add(choice);
		comboBox.getSelectionModel().select(0);
		
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(20);
		gridPane.setPadding(new Insets(15));
		gridPane.setAlignment(Pos.CENTER);
		
		Label label1 = new Label(poruka);
		gridPane.add(label1, 0, 0);
		gridPane.add(comboBox, 1, 0);

		Label label2 = new Label("Unesite datum vakcinacije:");
		gridPane.add(label2, 0, 1);
		gridPane.add(datum_vakcinacije, 1, 1);
		
		gridPane.add(ok, 0, 2);
		dialog.getDialogPane().setContent(gridPane);
		dialog.getDialogPane().getButtonTypes().add(cancel);
		dialog.show();
		
		ok.setOnAction(new EventHandler<ActionEvent>() {

			
			@Override
			public void handle(ActionEvent arg0) {
				System.out.println("USAO U OK BUTTON");
				if (datum_vakcinacije.getValue()!=null) {
//					System.out.println("USAO U IF U OK");
					date.get(index).setAnswer("VAKCINISAN");
					date.get(index).setVakcina(comboBox.getSelectionModel().getSelectedItem().toString());
					date.get(index).setDatum(datum_vakcinacije.getValue().toString());
					table.refresh();
					information = date.get(0).getAnswer() + " ; " + date.get(0).getVakcina() +" ; " +  date.get(1).getAnswer()  + " ; " + date.get(1).getVakcina() + " ; " + date.get(2).getAnswer() + " ; " + date.get(2).getVakcina() + " ; " + date.get(0).getDatum() + " ; " + date.get(1).getDatum() + " ; " + date.get(2).getDatum();
//					System.out.println("INFORMACIJA JE: " + information);
					userLayoutInterface.changeInformation(information,username,password);
					dialog.close();
				}else if(datum_vakcinacije.getValue()==null) {
//					System.out.println(minDate);
//					System.out.println("USAO U ELSE");
//					System.out.println(maxDate);
					if (minDate.isAfter(maxDate)) {
						System.out.println();
						AlertDrugaDoza();
					}else
						WarningDate();
					
				}
				
			}
		});
		
		
	
		
	}
	private void AlertDrugaDoza() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setHeaderText("Druga doza vakcine");
		alert.setContentText("Datum druge primljene doze mora biti minimalno 21 dan od datuma prijema prve doze!");
		alert.getDialogPane().setPrefWidth(500);
		alert.showAndWait();
	}
	private void AlertTrecaDoza() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setHeaderText("Treca doza vakcine");
		alert.setContentText("Datum trece primljene doze mora biti minimalno 6 meseci od datuma prijema druge doze!");
		alert.getDialogPane().setPrefWidth(500);
		alert.showAndWait();
	}
	private LocalDate calculateDate(int days, LocalDate date) {
		return date.plusDays(days);
	}
	private LocalDate calculateMonths(LocalDate date) {
		return date.plusMonths(6);

	}
	private void WarningDate() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText("Datum vakcinacije");
		alert.setContentText("Morate uneti datum vakcinacije!");
		alert.showAndWait();
	}
	
	private void DialogAlert(String poruka,int index, String datum) {
		dialog = new Dialog();
		
		ComboBox comboBox = new ComboBox();
		comboBox.getItems().add("Fajzer");
		comboBox.getItems().add("Sinofarm");
		comboBox.getItems().add("Astra-Zeneka");
		comboBox.getItems().add("Moderna");
		comboBox.getSelectionModel().select(0);
		
		GridPane gridPane = new GridPane();
		gridPane.setHgap(10);
		gridPane.setVgap(20);
		gridPane.setPadding(new Insets(15));
		gridPane.setAlignment(Pos.CENTER);
		
		Label label1 = new Label(poruka);
		gridPane.add(label1, 0, 0);
		gridPane.add(comboBox, 1, 0);

		Label label2 = new Label("Unesite datum vakcinacije:");
		gridPane.add(label2, 0, 1);
		gridPane.add(datum_vakcinacije, 1, 1);
		
		gridPane.add(ok, 0, 2);
		dialog.getDialogPane().setContent(gridPane);
		dialog.getDialogPane().getButtonTypes().add(cancel);
		dialog.show();
		
		ok.setOnAction(new EventHandler<ActionEvent>() {

			
			@Override
			public void handle(ActionEvent arg0) {
//				System.out.println("USAO U OK BUTTON");
				if (datum_vakcinacije.getValue()!=null) {
//					System.out.println("USAO U IF U OK");
					date.get(index).setAnswer("VAKCINISAN");
					date.get(index).setVakcina(comboBox.getSelectionModel().getSelectedItem().toString());
					date.get(index).setDatum(datum_vakcinacije.getValue().toString());
					table.refresh();
					information = date.get(0).getAnswer() + " ; " + date.get(0).getVakcina() +" ; " +  date.get(1).getAnswer()  + " ; " + date.get(1).getVakcina() + " ; " + date.get(2).getAnswer() + " ; " + date.get(2).getVakcina() + " ; " + date.get(0).getDatum() + " ; " + date.get(1).getDatum() + " ; " + date.get(2).getDatum();
//					System.out.println("INFORMACIJA JE: " + information);
					userLayoutInterface.changeInformation(information,username,password);
					dialog.close();
				}else if(datum_vakcinacije.getValue()==null) {
//					System.out.println(minDate);
//					System.out.println("USAO U ELSE");
//					System.out.println(maxDate);
					if (minDate.isAfter(maxDate)) {
						System.out.println();
						AlertTrecaDoza();
					}else
						WarningDate();
					
				}
				
			}
		});
		
	}
	public void fillTable(String[] information,String username, String password) {
		User user1 = new User(Arrays.copyOfRange(information, 1, 3));
		user1.setDoza("1");
		user1.setDatum(information[7]);
//		System.out.println("Prvi datum je: " + information[7]);
		date.add(user1);
		User user2 = new User(Arrays.copyOfRange(information, 3, 5));
		user2.setDoza("2");
		user2.setDatum(information[8]);
//		System.out.println("Prvi datum je: " + information[8]);
		date.add(user2);
		User user3 = new User(Arrays.copyOfRange(information, 5, 7));
		user3.setDoza("3");
		user3.setDatum(information[9]);
//		System.out.println("Prvi datum je: " + information[9]);
		date.add(user3);
//		System.out.println("U tri je: " + user3.getDatum());
		this.username = username;
		this.password = password;
		this.information = date.get(0).getAnswer() + " ; " + date.get(0).getVakcina() + " ; "  +  date.get(1).getAnswer()  + " ; " + date.get(1).getVakcina() + " ; "  + date.get(2).getAnswer() + " ; " + date.get(2).getVakcina() +" ; " + date.get(0).getDatum() + " ; " + date.get(1).getDatum() + " ; "  + date.get(2).getDatum();
		System.out.println("Podaci o korisniku: " + this.information);
		
		initLayout();
		initAction();
	}

	public void covidSertificate(String information) {
		
//		System.out.println("Text fajl sadrzi: " + information);
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(username)));
			String user[] = information.split(" ; ");
			
			bufferedWriter.write("Ime: " + user[3] + "\n");
			bufferedWriter.write("Prezime: " + user[4] + "\n");
			bufferedWriter.write("JMBG: " + user[5] + "\n");
			bufferedWriter.write("Prva primljena vakcina: " + user[9] + "\n");
			if (!user[14].equals("nije unesen")) {
				bufferedWriter.write("Datum primanja prve doze: " + user[14] + "\n");
			}
			bufferedWriter.write("Druga primljena vakcina: " + user[11] + "\n");
			if (!user[15].equals("nije unesen")) {
				bufferedWriter.write("Datum primanja druge doze: " + user[15] + "\n");
			}
			if (user[12].equals("VAKCINISAN")) {
				bufferedWriter.write("Treca primljena vakcina: " + user[13] + "\n");
				if (!user[16].equals("nije unesen")) {
					bufferedWriter.write("Datum primanja trece doze: " + user[16] + "\n");
				}
			}
//			System.out.println("Dosao dovde");
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void setUserLayoutInterface(UserLayoutInterface userLayoutInterface) {
		this.userLayoutInterface = userLayoutInterface;
	}

	
	
	
}
