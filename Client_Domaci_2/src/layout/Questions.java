package layout;

import javafx.scene.control.Label;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import com.sun.openpisces.Dasher;

import enums.Vakcinisan;
import interfaces.QuestionsInterfaces;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class Questions {

	private ComboBox comboBox;
	private RadioButton vakcinisan;
	private RadioButton nevakcinisan;
	private GridPane gridPane;
	private Dialog dialog = new Dialog<>();
	private ToggleGroup toggleGroup;
	private Button ok;
	private ButtonType cancel;
	private Label first_label = new Label("Da li ste primili prvu dozu?");

	private DatePicker datum_vakcinacije;
	private LocalDate minDate = LocalDate.of(2021, 1, 1);
	private LocalDate maxDate;

	private Vakcinisan answer1 = Vakcinisan.NEVAKCINISAN;
	private String doza1 = "Nijedna";
	private Vakcinisan answer2 = Vakcinisan.NEVAKCINISAN;
	private String doza2 = "Nijedna";
	private Vakcinisan answer3 = Vakcinisan.NEVAKCINISAN;
	private String doza3 = "Nijendna";

	private LocalDate date1 = null;
	private LocalDate date2 = null;
	private LocalDate date3 = null;

	private String date_1 = "nije unesen";
	private String date_2 = "nije unesen";
	private String date_3 = "nije unesen";
	private int year = 0;
	private int month = 0;
	private int day = 0;

	private QuestionsInterfaces questionsInterfaces;

	public Questions() {
		init();
	}

	private void init() {
		initComponenets();
		initLayout();
		initProperties();
		initAction();
	}

	private void initComponenets() {

		comboBox = new ComboBox<>();
		if (first_label.getText().equals("Da li ste primili prvu dozu?")
				|| first_label.getText().equals("Da li ste vakcinisani trecom dozom?")) {
			comboBox.getItems().add("Fajzer");
			comboBox.getItems().add("Sinofarm");
			comboBox.getItems().add("Astra-Zeneka");
			comboBox.getItems().add("Moderna");

			comboBox.getSelectionModel().select(0);
		} else {
			comboBox.getItems().add(doza1);
			comboBox.getSelectionModel().select(0);
		}
		vakcinisan = new RadioButton("DA");
		nevakcinisan = new RadioButton("NE");
		toggleGroup = new ToggleGroup();

		ok = new Button("OK");
		cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

		datum_vakcinacije = new DatePicker();

		maxDate = LocalDate.of(2021, 12, 31);

		datum_vakcinacije.setDayCellFactory(d -> new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);
				setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
			}
		});

		gridPane = new GridPane();
		dialog = new Dialog();
	}

	private void initProperties() {

		vakcinisan.setToggleGroup(toggleGroup);
		nevakcinisan.setToggleGroup(toggleGroup);

		vakcinisan.setUserData(Vakcinisan.VAKCINISAN);
		nevakcinisan.setUserData(Vakcinisan.NEVAKCINISAN);

		dialog.getDialogPane().getButtonTypes().add(cancel);
		dialog.setWidth(600);
		dialog.setHeight(300);

	}

	private void Warning() {
		Alert alert = new Alert(AlertType.WARNING);
		alert.getDialogPane().setMinWidth(800);
		alert.setHeaderText("Status vakcine");
		alert.setContentText("Da biste nastavili dalje morate izabrati opciju vakcinisan ili nevakcinisan i morate izabrati datum primljene vakcine!");
		alert.show();
	}

	private void initAction() {

		ok.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				
				if ((vakcinisan.getToggleGroup().getSelectedToggle() == null
						&& nevakcinisan.getToggleGroup().getSelectedToggle() == null) || (toggleGroup.getSelectedToggle().getUserData().toString().toUpperCase().equals("VAKCINISAN") && datum_vakcinacije.getValue()==null)) {
					Warning();
					
					
				} else {
					String answer = toggleGroup.getSelectedToggle().getUserData().toString().toUpperCase();

					if (answer.equals("VAKCINISAN")) {

						String text1 = first_label.getText().toString();
						String vrsta_vakcine = comboBox.getSelectionModel().getSelectedItem().toString();

						if (text1.equals("Da li ste primili prvu dozu?")) {
							answer1 = (Vakcinisan) toggleGroup.getSelectedToggle().getUserData();
							doza1 = vrsta_vakcine;
							first_label.setText("Da li ste vakcinisani drugom dozom?");

							if (datum_vakcinacije.getValue() != null) {

								date1 = datum_vakcinacije.getValue();
								LocalDate minDate1 = calculateDate(21, date1);
								setDate(minDate1);
								date_1 = date1.toString();
							} else {
								datum_vakcinacije.setDisable(false);

							}
							datum_vakcinacije.setValue(null);
							dialog.close();
							init();
						} else {
							if (text1.equals("Da li ste vakcinisani trecom dozom?")) {
								answer3 = (Vakcinisan) toggleGroup.getSelectedToggle().getUserData();
								doza3 = vrsta_vakcine;

								if (datum_vakcinacije.getValue() != null) {
									date3 = datum_vakcinacije.getValue();
									date_3 = date3.toString();
								}
								finish();
							} else {

								answer2 = (Vakcinisan) toggleGroup.getSelectedToggle().getUserData();
								doza2 = vrsta_vakcine;

								if (datum_vakcinacije.getValue() != null) {

									date2 = datum_vakcinacije.getValue();
									LocalDate minDate1 = calculateMonths(date2);
									setDate(minDate1);
									date_2 = date2.toString();

								} else {

									if (date1 != null) {
										LocalDate minDate1 = calculateMonths(date1);
										LocalDate minDate2 = calculateDate(21, minDate1);
										minDate = LocalDate.of(minDate2.getYear(), minDate2.getMonth(),
												minDate2.getDayOfMonth());
									} else {
										minDate = LocalDate.of(2022, 1, 1);
									}

								}
								datum_vakcinacije.setValue(null);
								first_label.setText("Da li ste vakcinisani trecom dozom?");

								dialog.close();
								init();

							}
						}

					} else {
						finish();

					}

				}
			}
		});

	}

	private void setDate(LocalDate date) {
		year = date.getYear();
		month = date.getMonthValue();
		day = date.getDayOfMonth();

		minDate = LocalDate.of(year, month, day);
	}

	private void finish() {
		dialog.close();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setContentText("Hvala vam na informacijama!");
		alert.show();

		questionsInterfaces.sendInformation(answer1, doza1, answer2, doza2, answer3, doza3, date_1, date_2, date_3);
	}

	private LocalDate calculateDate(int days, LocalDate date) {

		return date.plusDays(days);

	}

	private LocalDate calculateMonths(LocalDate date) {
		return date.plusMonths(6);

	}

	private void initLayout() {

		System.out.println("USao");
		gridPane.setHgap(10);
		gridPane.setVgap(20);
		gridPane.setPadding(new Insets(15));
		gridPane.setAlignment(Pos.CENTER);

		Label label = new Label("Molimo vas da odgovorite na sledece pitanje:");
		gridPane.add(label, 0, 0);

		gridPane.add(first_label, 0, 1);
		HBox hBox = new HBox(15);
		hBox.setAlignment(Pos.CENTER);
		hBox.getChildren().addAll(vakcinisan, nevakcinisan);
		gridPane.add(hBox, 1, 1);

		Label label1 = new Label("Izabretire naziv primljene vakcine:");
		gridPane.add(label1, 0, 2);
		gridPane.add(comboBox, 1, 2);

		Label label_datum = new Label("Datum vakcinacije:");
		gridPane.add(label_datum, 0, 3);
		gridPane.add(datum_vakcinacije, 1, 3);

		gridPane.add(ok, 0, 4);
//		gridPane.add(cancel, 1, 3);
		dialog.getDialogPane().setContent(gridPane);
		dialog.show();
	}

	public void setQuestionsInterfaces(QuestionsInterfaces questionsInterfaces) {
		this.questionsInterfaces = questionsInterfaces;
	}

}
