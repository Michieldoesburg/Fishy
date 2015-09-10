package main;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

/**
 * This class contains all the event handlers of the buttons on the options screen.
 * 
 * @author Group 6
 *
 */
public class OptionsController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button OnButton;

	@FXML
	private Button OffButton;

	@FXML
	private Button BackButton;

	@FXML
	void MusicOnEvent(MouseEvent event) {

	}

	@FXML
	void MusicOffEvent(MouseEvent event) {

	}

	@FXML
	void BackButtonEvent(MouseEvent event) {

	}

    /**
     * setting up the screen with a button which returns to the main screen
     * and on and off buttons which controls the background music
     */
	@FXML
	void initialize() {
		assert OnButton != null : "fx:id=\"OnButton\" was not injected: check your FXML file 'OptionsScreen.fxml'.";
		assert OffButton != null : "fx:id=\"OffButton\" was not injected: check your FXML file 'OptionsScreen.fxml'.";
		assert BackButton != null : "fx:id=\"BackButton\" was not injected: check your FXML file 'OptionsScreen.fxml'.";

		OnButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				System.out.println("Music is on");

			}
		});

		OffButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				System.out.println("Music is off");

			}
		});
		
		BackButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				Game.switchScreen("FXML/MainScreen.fxml");

			}
		});
	}
}