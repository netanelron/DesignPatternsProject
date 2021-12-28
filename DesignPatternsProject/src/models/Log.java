package models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import gui.AlertPopUp;
import javafx.scene.control.ListView;

public class Log {
	File file;
	private String fileName;
	private ListView<String> logView;

	public Log() throws IOException {
			fileName = "Log-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm-ss")) + ".txt";
			file = new File("./src/log/" + fileName);
			file.createNewFile();
			logView = new ListView<String>();		
			FileWriter logger = new FileWriter(file, true);
			String timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
			logger.write("Log file for date "+timeNow+" :\n\n");
			logger.close();
	}


	public void addMsgToLog(String msg) throws IOException {
			FileWriter logger = new FileWriter(file, true);
			String timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
			logger.write(timeNow +"  : " + msg + "\n");
			logger.close();
			logView.getItems().add(msg);
	}

	public ListView<String> getLogView() {
		return logView;
	}
}
