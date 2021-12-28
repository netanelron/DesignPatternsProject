package gui;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertPopUp extends Alert{
	private IAlertStrategy strategy;
	public AlertPopUp() {
		super(AlertType.INFORMATION);	
		strategy=new NullAlertStrategy();
	}
	public AlertPopUp(AlertType type) {
		super(type);
		strategy=new NullAlertStrategy();
	}
	public void showAlert(String msg) {
		if(!this.isShowing()) {
			strategy.soundAlert(msg);
			Platform.runLater(() -> { 
				setTitle(strategy.toString());
				setHeaderText("");
				setContentText(msg);
				showAndWait();				
			});	
		}
	}
	public void setStrategy(IAlertStrategy strategy) {
		this.strategy=strategy;
	}

}
