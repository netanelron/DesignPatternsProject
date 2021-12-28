package gui;

import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;

public class SuccessAlertStrategy implements IAlertStrategy{
	@Override
	public void soundAlert(String msg) {
		SoundFX.getInstance().playSucc();
	}
	@Override
	public String toString() {
		return "Success";
	}
}
