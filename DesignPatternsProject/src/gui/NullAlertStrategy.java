package gui;

import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

public class NullAlertStrategy implements IAlertStrategy {
	@Override
	public void soundAlert(String msg) {		
	}
	@Override
	public String toString() {
		return "";
	}
}
