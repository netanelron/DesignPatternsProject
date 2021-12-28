package gui;

import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

public class ErrorAlertStrategy implements IAlertStrategy {

	@Override
	public void soundAlert(String msg) {
		SoundFX.getInstance().playError();
	}
	@Override
	public String toString() {
		return "Error";
	}
}
