package models;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import gui.AlertPopUp;
import gui.ErrorAlertStrategy;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Result {
	private String address;
	private int numOfAbuses;
	private ImageView status;
	Hyperlink link;

	public Result(String address, int numOfAbuses) {
		this.address = address;
		setAddress();
		this.numOfAbuses = numOfAbuses;
		status=new ImageView();
		setStatus();
	}

	public String getAddress() {
		return address;
	}
	public void setStatus() {
		if(numOfAbuses>0)
			status.setImage(new Image("gui/icons/bad.png"));
		else {
			status.setImage(new Image("gui/icons/good.png"));
		}
		status.setFitHeight(10);
		status.setFitWidth(10);
	}

	public void setAddress() {
		link = new Hyperlink("https://www.bitcoinabuse.com/reports/" + address);
		link.setOnMouseClicked(e-> {
					try {
						Desktop.getDesktop().browse(new URI(link.getText()));
					} catch (Exception e1) {} 
		});
	}

	public int getNumOfAbuses() {
		return numOfAbuses;
	}

	public void setNumOfAbuses(int numOfAbuses) {
		this.numOfAbuses = numOfAbuses;
	}
	public ImageView getStatus() {
		return status;
	}
	public Hyperlink getLink() {
		return link;
	}
}
