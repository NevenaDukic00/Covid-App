package layout;

import entity.User_Admin;
import interfaces.AdminInterface;
import interfaces.AdminLayouInterface;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class AdminLayout extends VBox{

	private TableView table;
	private TableColumn jmbg;
	private TableColumn validna_propusnica;
	private TableColumn first_name;
	private TableColumn last_name;
	private TableColumn number;
	private Menu menu;
	private MenuItem item1;
	private MenuItem item2;
	private MenuItem item3;
	
	private Menu menu1;
	private MenuItem item1_1;
	private MenuItem item1_2;
	private MenuItem item1_3;
	private MenuItem item1_4;
	
	private String prvaDoza;
	private String drugaDoza;
	private String trecaDoza;
	
	private String fajzer;
	private String sinofarm;
	private String astrazeneka;
	private String moderna;
	private ObservableList<User_Admin> date = FXCollections.observableArrayList();
	
	private AdminLayouInterface adminLayouInterface;
	
	public AdminLayout(int dim) {
		setSpacing(dim);
		init();
	}
	
	private void init() {
		initComponent();
		initAction();
		
	}
	private void initAction() {
		
		item1.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent arg0) {
				message("Prva doza vakcina", prvaDoza);	
			}
		});
		
		item2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				message("Druga doza vakcina", drugaDoza);
				
			}
		});
		item3.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				message("Treca doza vakcina", trecaDoza);
				
			}
		});
		
		item1_1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				message1("Broj ljudi koji su primili drugu dozu Fajzer", fajzer);
				
			}
		});
		item1_2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				message1("Broj ljudi koji su primili drugu dozu Sinofarm", sinofarm);
				
			}
		});
		item1_3.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				message1("Broj ljudi koji su primili drugu dozu Moderne", moderna);
				
			}
		});
		item1_4.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				message1("Broj ljudi koji su primili drugu dozu Astra-Zeneke", astrazeneka);
				
			}
		});
		
	}
	private void message1(String text, String brojljudi) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText("Druga doza vakcine:");
		alert.setContentText(text + ": "+  brojljudi);
		alert.show();
		
	}
	private void message(String text, String brojDoza) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(text);
		alert.setContentText("Broj ljudi koji su primili ovu dozu je: " + brojDoza);
		alert.show();
	}
	private void initComponent() {
		table = new TableView<User_Admin>();
		jmbg = new TableColumn<User_Admin, String>("JMBG");
		validna_propusnica= new TableColumn<User_Admin, String>("Validnost kovid propusnice");
		first_name = new TableColumn<User_Admin, String>("Ime");
		last_name = new TableColumn<User_Admin, String>("Prezime");
		number = new TableColumn<User_Admin, String>("Broj primljenih doza");
		
		menu = new Menu("Meni");
		item1 = new MenuItem("Broj vakcinisanih prvom dozom");
		item2 = new MenuItem("Broj vakcinisanih drugom dozom");
		item3 = new MenuItem("Broj vakcinisanih trecom dozom");
		
		menu1 = new Menu("Druga doza");
		item1_1 = new MenuItem("Broj vakcinisanih Fajzerom");
		item1_2 = new MenuItem("Broj vakcinisanih Sinofarmom");
		item1_3 = new MenuItem("Broj vakcinisanih Modernom");
		item1_4 = new MenuItem("Broj vakcinisanih Astra-Zenekom");
	}
	
	private void initLayout() {
		
		jmbg.setCellValueFactory(new PropertyValueFactory<User_Admin,String>("jmbg"));
		validna_propusnica.setCellValueFactory(new PropertyValueFactory<User_Admin,String>("validna_propusnica"));
		first_name.setCellValueFactory(new PropertyValueFactory<User_Admin,String>("first_name"));
		last_name.setCellValueFactory(new PropertyValueFactory<User_Admin,String>("last_name"));
		number.setCellValueFactory(new PropertyValueFactory<User_Admin,String>("number"));
		
		table.getColumns().add(jmbg);
		table.getColumns().add(first_name);
		table.getColumns().add(last_name);
		table.getColumns().add(number);
		table.getColumns().add(validna_propusnica);
		table.setItems(date);
		
		
		menu.getItems().add(item1);
		menu.getItems().add(item2);
		menu.getItems().add(item3);
		MenuBar menuBar = new MenuBar(menu);
		
		menu1.getItems().add(item1_1);
		menu1.getItems().add(item1_2);
		menu1.getItems().add(item1_3);
		menu1.getItems().add(item1_4);
		MenuBar menuBar1 = new MenuBar(menu1);
		HBox hBox = new HBox();
		hBox.setSpacing(3);
		hBox.getChildren().addAll(menuBar,menuBar1);
		 
		getChildren().addAll(hBox,table);
		adminLayouInterface.getStat();
		
	}
	
	public void fillTable(String information) {
	
		if (information.equals("")) {
			table.setPlaceholder(new Label("No rows to display"));
		}else {
		String users[] = information.split("\\* ");
		int k = 0;
		while (k<users.length) {
			String user[] = users[k].split(" ; ");
			
			k++;
			
			date.add(new User_Admin(user[0],user[4],user[1],user[2],user[3]));
		}
		}
		initLayout();
	}

	public void Stat(String stat) {
		
		String date []= stat.split("\\*");
		String num[] = date[0].split(" ; ");
		
		
		prvaDoza = num[0];
		drugaDoza = num[1];
		trecaDoza = num[2];
		
		String num1[] = date[1].split(" ; ");
		fajzer = num1[0];
		sinofarm = num1[1];
		astrazeneka = num1[2];
//		System.out.println("Astrazeneku su primili: " + astrazeneka);
		moderna = num1[3];
		
	}
	public void setAdminLayouInterface(AdminLayouInterface adminLayouInterface) {
		this.adminLayouInterface = adminLayouInterface;
	}

	
	
}
