package com.risk.team.view;

import com.risk.team.controller.GamePhaseController;
import com.risk.team.services.RiskMapRW;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

/**
 * This class provides the view for the user showing game screen.
 * 
 * @author Kartika Patil
 * @author yashgolwala
 * @author Harsh Mehta
 */
public class GamePhaseView implements EventHandler<ActionEvent> {

	/** Object for RiskMapRW Class */
	private RiskMapRW mapObj;

	/** HashMap to store names and type of players */
	private HashMap<String,String> playerNamesAndTypes;

	/**
	 * Constructor for GamePlayView
	 * 
	 * @param mapObj RiskMapRW object 
	 * @param hm hashmap containing player names and type
	 */
	public GamePhaseView(RiskMapRW mapObj, HashMap<String,String> hm) {
		this.mapObj = mapObj;
		this.playerNamesAndTypes = hm;
	}

	/*
	 * (non-Javadoc)
	 * This method is overridden to create a scene at UI end.
	 * @see javafx.event.EventHandler#handle(javafx.event.Event)
	 */
	@Override
	public void handle(ActionEvent event) {

		GamePhaseController controller = new GamePhaseController(this.mapObj, this.playerNamesAndTypes);


		final Stage gamePlayStage = new Stage();
		gamePlayStage.setTitle("Game Screen");

		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("MapSelectorLayout.fxml"));
		loader.setController(controller);
		Parent root = null;
		try {
			root = (Parent) loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scene scene = new Scene(root);
		gamePlayStage.setScene(scene);
		gamePlayStage.show();

		final Stage terminalStage = new Stage();
		terminalStage.setTitle("Game Update Window");

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("TerminalWindow.fxml"));
		fxmlLoader.setController(controller);

		Parent myRoot = null;
		try {
			myRoot = (Parent) fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Scene myScene = new Scene(myRoot);
		terminalStage.setScene(myScene);
		terminalStage.show();
	}

}
