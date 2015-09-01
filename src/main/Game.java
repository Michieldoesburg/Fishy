package main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Game extends Application {

	static Stage stage;
	static Pane pane;
    
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * @param firstStage is de stage waar het start.
	 */
	@Override
	public void start(Stage firstStage) {
		try {

			stage = firstStage;

			firstStage.setTitle("Fishy");
			FXMLLoader loader = new FXMLLoader(getClass().getResource(
					"FXML/MainScreen.fxml"));

			pane = (Pane) loader.load();

			Scene scene = new Scene(pane);
			firstStage.setHeight(715);
	        firstStage.setWidth(1160);
			firstStage.setScene(scene);
			firstStage.show();

		} catch (Exception e) {
			System.out.println("File is not found " + e);
		}
	}

	/**
	 * Deze methode wordt gebruikt voor het veranderen tussen scenes
	 * 
	 * @param bestand is de naam van de FXML bestand van de scene die je wilt laden.
	 */
	public static void switchScreen(String bestand) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Game.class.getResource(bestand));
			stage.getScene().setRoot((Parent) loader.load());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

