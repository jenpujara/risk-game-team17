package com.risk.team.view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

import com.risk.team.services.RiskMapEdit;
import com.risk.team.services.RiskMapRW;
import com.risk.team.services.RiskMapVerify;

/**
 * Launches the Game and provides the main window and the view for the user,
 * either to load or create a new map.
 *
 * @author yashgolwala
 * @author Harsh Mehta
 * 
 * @version 3.0.0
 */
public class LaunchGameDriver extends Application {
	/**
	 * The Main Method which Launches the Game and drives by providing
	 * the options.
	 *
	 * @param args main arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * The main entry point for all JavaFX applications.The start method is called after the
	 * init method has returned,and after the system is ready for the application to begin running.
	 * NOTE: This method is called on the JavaFX Application Thread.
	 *
	 * @param primaryStage the primary stage for this application, onto which the application scene
	 *                     can be set. The primary stage will be embedded in the browser if the application was launched
	 *                     as an applet.Applications may create other stages, if needed, but they will not be primary
	 *                     stages and will not be embedded in the browser.
	 * @see Application#start(Stage)
	 */
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle(" RISK game - Strategy Conquest Game (Build 3)");
		try {

			VBox vbox = new VBox();

			Label gameLabel = new Label("Risk Play");
			gameLabel.setStyle("-fx-font-weight: bold");
			gameLabel.setFont(new Font("Arial", 20));
			gameLabel.setTextFill(Color.RED);

			Label optionLabel = new Label("Please select below options");
			optionLabel.setFont(new Font("Monospaced", 20));
			optionLabel.setTextFill(Color.RED);

			vbox.setStyle("-fx-background-color: BLUE");
			vbox.setSpacing(20);
			vbox.setAlignment(Pos.CENTER);

			vbox.getChildren().addAll(gameLabel, optionLabel, loadMapButton(), createMapButton(), startGameButton(), startSavedGameButton(), startTournamentButton());
			Scene scene = new Scene(vbox, 500, 500);
			scene.getStylesheets().add("application.css");

			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Map Create Button
	 *
	 * @return {@link Button} wherein button opens up a create map screen.
	 */
	public static Button createMapButton() {
		Button createMapButton = new Button("Create a New Map");
		createMapButton.setOnAction(e -> {
			createMapButton.getScene().getWindow().hide();
			RiskMapEdit mapEditor = new RiskMapEdit();
			mapEditor.createEditMap(true);
		});
		createMapButton.setMaxWidth(200);
		return createMapButton;
	}

	/**
	 * Load map Button
	 *
	 * @return {@link Button} wherein button opens up a new screen to load or edit a map.
	 */
	public static Button loadMapButton() {
		Button loadMapButton = new Button("Edit existing Map File");
		loadMapButton.setOnAction(e -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select a Map File");
			fileChooser.getExtensionFilters()
			.add(new FileChooser.ExtensionFilter("Map File Extensions (*.map or *.MAP)", "*.map", "*.MAP"));
			File selectedFile = fileChooser.showOpenDialog(null);
			if (selectedFile != null) {
				loadMapButton.getScene().getWindow().hide();
				String fileName = selectedFile.getAbsolutePath();
				System.out.println("File location: " + fileName);
				RiskMapVerify mapValidate = new RiskMapVerify();
				if (mapValidate.validateMapFile(fileName)) {
					RiskMapRW readMap = new RiskMapRW(mapValidate);
					new RiskMapEdit(readMap.readFile()).createEditMap(false);
				}
			}
		});
		loadMapButton.setMaxWidth(200);

		return loadMapButton;
	}

	/**
	 * Start Game Button
	 *
	 * @return {@link Button} wherein button loads chosen map and starts the game.
	 */
	public static Button startGameButton() {
		Button startGameButton = new Button("Load Map & Play Game");
		startGameButton.setOnAction(new PlayerDetailsView());
		startGameButton.setMaxWidth(200);
		return startGameButton;
	}

	/**
	 * Start saved game Button
	 * 
	 * @return {@link Button} wherein button loads chosen map and starts the game.
	 */
	public static Button startSavedGameButton() {
		Button startSavedGameButton = new Button("Load Saved Game");
		startSavedGameButton.setOnAction(e -> LoadGame.openLoadGame());
		startSavedGameButton.setMaxWidth(200);
		return startSavedGameButton;
	}

	/**
	 * Start tournament Button
	 * 
	 * @return {@link Button} wherein button loads chosen map and starts the game.
	 */
	public static Button startTournamentButton() {
		Button startTournamentButton = new Button("Start Tournament");
		startTournamentButton.setOnAction(new TournamentView());
		startTournamentButton.setMaxWidth(200);
		return startTournamentButton;
	}

}


