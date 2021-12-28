package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Address;
import models.Log;
import models.Result;

public class MainPaneController implements Initializable {

    @FXML
    private AnchorPane mainPane;

    @FXML
    private TextField addressTxt;

    @FXML
    private Button addButton;

    @FXML
    private ImageView uploadButton;

    @FXML
    private TableView<Address> addressTable;

    @FXML
    private TableColumn<Address, String> addressCol;

    @FXML
    private TableColumn<Address, String> delAddressCol;

    @FXML
    private Button clrButton;

    @FXML
    private Button scanButton;

    @FXML
    private ImageView excel;

    @FXML
    private TableView<Result> resultTable;

    @FXML
    private TableColumn<Result, String> addressReportCol;

    @FXML
    private TableColumn<Result, Integer>  numOfAbusesCol;

    @FXML
    private TableColumn<Result, ImageView> statusCol;

    @FXML
    private TableColumn<Result, Hyperlink> linkCol;

    @FXML
    private AnchorPane logPane;

    
    private ObservableList<Address> addressList;
    private ObservableList<Result> resultList;
    private final String TOKEN="MuEWf94Rg5K5a72FgMqUz4Me8HyBT6P9jq48FpnhN35JsyHFZWXHooPxaW3k";
    private Log log;
    private AlertPopUp defaultAlert;
    private AlertPopUp errorAlert;
    private AlertPopUp succAlert;
    @FXML
    void Add(ActionEvent event) {
    	if(!addressTxt.getText().isEmpty()) {
	    	addAddress(addressTxt.getText());
	    	if(Address.isValid(addressTxt.getText()))
	    		popUpSucc("Address Added");
	    	addressTxt.clear();
    	}
    	else 
    		popUpError("Invalid address");
    	
    }

    @FXML
    void Clear(ActionEvent event) {
    	defaultAlert.showAlert("Addresses Cleared");
    	addressList.clear();

    }

    @FXML
    void SaveToExcel(MouseEvent event) {
    	String timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss"));
    	if(resultList.isEmpty()) {
    		popUpError("No results to export!");
    		return;
    	}		
		try {
			String fileName = "Result-" + timeNow + ".csv";
			File myObj = new File("./src/excels/"+fileName);
			myObj.createNewFile();
			FileWriter excelWriter = new FileWriter(myObj, true);
			excelWriter.write("Address,Number Of Abuses,Link\n");
			for(Result e: resultList)
				excelWriter.write(e.getAddress()+","+e.getNumOfAbuses()+","+e.getLink().getText()+"\n");
			excelWriter.close();
			log.addMsgToLog(timeNow + "  Results exported to excel");
			popUpSucc("Exported to excel successfully");
			resultList.clear();
		} catch (IOException e) {
			try {
			log.addMsgToLog(timeNow + "  Failed to export to excel");
			}
			catch (Exception e2) {
				popUpError("Failed to write to log!");
			}
			popUpError("Failed to export to excel!");
			
		}
		
	}

    @FXML
    void Scan(ActionEvent event) {
    	resultList.clear();
		if(addressList.isEmpty()) {
			popUpError("No Addresses to scan...");
			return;
		}
			
		try {
			for(Address a:addressList) {
				resultList.add(new Result(a.getAddress(),getNumOfAbuses(a.getAddress())));
			}
			String timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
			log.addMsgToLog(timeNow + "  Scanned Successfully");
			popUpSucc("Scanned Succesfully");
			addressList.clear();
		}
		catch (Exception e) {
			String timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
			try {
				log.addMsgToLog(timeNow +"  Failed To Scan");
			} catch (IOException e1) {
				popUpError("Failed to write to log!");
			}
			popUpError("Failed to Scan!");
		}
    }

    @FXML
    void UploadFile(MouseEvent event) {
    	FileChooser fileChooser = new FileChooser();
    	try {
			File fileToRead = fileChooser.showOpenDialog(((Stage)mainPane.getScene().getWindow()));
			if(fileToRead==null)
				return;
			Scanner myReader;
			myReader = new Scanner(fileToRead);
	        while (myReader.hasNextLine()) {
	          String address = myReader.nextLine();
	          if(Address.isValid(address))
	        	  addAddress(address);
	        }
	        myReader.close();
			popUpSucc("Uploaded Successfully");
		} catch (FileNotFoundException e) { 	
			popUpError("File not found!"); 
		}
    }
    
    @FXML
    void changeAddress(TableColumn.CellEditEvent<Address, String> event) {
    	Address address = addressTable.getSelectionModel().getSelectedItem();
    	if(Address.isValid(event.getNewValue())) {
    		address.setAddress(event.getNewValue());
			defaultAlert.showAlert("Address Changed");
    	}
    	else 
    		popUpError("Invalid address!!");    		
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		defaultAlert=new AlertPopUp();
		errorAlert=new AlertPopUp(AlertType.ERROR);
		succAlert=new AlertPopUp(AlertType.CONFIRMATION);
		errorAlert.setStrategy(new ErrorAlertStrategy());
		succAlert.setStrategy(new SuccessAlertStrategy());
		
		addressList = FXCollections.observableArrayList();
		resultList = FXCollections.observableArrayList();
		try {
			log = new Log();
		} catch (IOException e) {
			popUpError("Failed to create log");
		}
		addressTable.setEditable(true);
		addressCol.setCellFactory(TextFieldTableCell.forTableColumn());
		addressCol.setCellValueFactory(new PropertyValueFactory<Address, String>("address"));
		addressTable.setItems(addressList);
		setDelAddressCol();
		
		addressReportCol.setCellValueFactory(new PropertyValueFactory<Result, String>("address"));
		numOfAbusesCol.setCellValueFactory(new PropertyValueFactory<Result, Integer>("numOfAbuses"));
		linkCol.setCellValueFactory(new PropertyValueFactory<Result, Hyperlink>("link"));
		
		statusCol.setCellValueFactory(new PropertyValueFactory<Result, ImageView>("status"));
		resultTable.setItems(resultList);

		ListView<String> logView = log.getLogView();
		logPane.getChildren().add(logView);
		
		AnchorPane.setTopAnchor(logView, 0.0);
		AnchorPane.setBottomAnchor(logView, 0.0);
		AnchorPane.setLeftAnchor(logView, 0.0);
		AnchorPane.setRightAnchor(logView, 0.0);
	}
	
	private void addAddress(String address) {
		if(Address.isValid(address)) {
    		addressList.add(new Address(address));
		}
    	else {
			popUpError("Invalid Address!");			
    	}
    		
	}
	private int getNumOfAbuses(String address) throws IOException {
        URL bitcoinabuse;
		bitcoinabuse = new URL("https://www.bitcoinabuse.com/api/reports/check?address="+address+"&api_token="+TOKEN);
        URLConnection yc = bitcoinabuse.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));                       
        String inputLine;
        String resString = "";
        while ((inputLine = in.readLine()) != null) 
        	resString+=inputLine;
        in.close();
        resString = resString.substring(1, resString.length()-1);
        ArrayList<String> elephantList = new ArrayList<>(Arrays.asList(resString.split(",")));
        ArrayList<String> count = new ArrayList<>(Arrays.asList(elephantList.get(1).split(":")));
        return Integer.parseInt(count.get(1));

	}
	
	private void setDelAddressCol() {

		delAddressCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
		Callback<TableColumn<Address, String>, TableCell<Address, String>> cellFactory = //
				new Callback<TableColumn<Address, String>, TableCell<Address, String>>() {

					@Override
					public TableCell<Address, String> call(TableColumn<Address, String> param) {
						final TableCell<Address, String> cell = new TableCell<Address, String>() {
							final Button btn = new Button("Delete");

							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else {
									btn.setOnAction(e -> {
										addressList.remove(getTableView().getItems().get(getIndex()));
									});
									setGraphic(btn);
									setText(null);
								}

							}
						};
						return cell;

					}

				};

		delAddressCol.setCellFactory(cellFactory);
	}
	private void popUpError(String msg) {
		errorAlert.showAlert(msg);	
	}
	private void popUpSucc(String msg) {
		succAlert.showAlert(msg);
	}

}
