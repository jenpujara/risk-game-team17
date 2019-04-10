package com.risk.team.controller;

import com.risk.team.model.IPlayerType;
import com.risk.team.services.RiskMapRW;
import com.risk.team.services.util.GameUpdateWindow;
import com.risk.team.view.GamePhaseView;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * This class provides the Controller options to handle the initial player details.
 *
 * @author Kartika Patil
 * @author yashgolwala
 * @author Harsh Mehta
 * 
 * @version 1.0.0
 */
public class PlayerDetailsController implements Initializable {

	static int numberOfPlayers;

	/**
	 * Map Object
	 */
	private RiskMapRW mapObj;

	/** Layout component for player details*/
	@FXML
	private VBox playerDetails;

	/** Layout component for player types */
	@FXML
	private VBox playerTypes;

	/**
	 * Button to start the Game
	 */
	@FXML
	private Button startGame;

	/**
	 * A JAVA FX feature which provides choice boxes to select the number of players.
	 */
	@FXML
	private ChoiceBox<Integer> playerCount;

	/**
	 * Initial Selected Players
	 */
	private int numOfPlayersSelected;

	/**
	 * Player Details Constructor
	 *
	 * @param mapObj Map Object
	 */
	public PlayerDetailsController(RiskMapRW mapObj) {
		this.mapObj = mapObj;
	}

	/**
	 * This method is called to initialize the player detail controller.
	 *
	 * @param location  Location
	 * @param resources resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		PlayerDetailsController.initializePlayerCountValues(playerCount);
		startGame.setDisable(true);
		playerCountListener();
	}

	/**
	 * Listener to count players
	 */
	public void playerCountListener() {
		playerCount.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
				numberOfPlayers=0;
				numOfPlayersSelected = playerCount.getSelectionModel().getSelectedItem();
				refreshPlayerList();
				refreshPlayerTypes();
				startGame.setDisable(false);
			}
		});
	}

	/**
	 * Method to refresh list of players
	 */
	public void refreshPlayerList() {
		playerDetails.getChildren().clear();
		playerDetails.setSpacing(10);

		for (int i = 0; i < numOfPlayersSelected; i++) {
			HBox getBox = getPlayersDetailsBox();
			playerDetails.getChildren().addAll(getBox);
		}

	}

	/**
	 * Method to refresh player
	 */
	public void refreshPlayerTypes(){
		playerTypes.getChildren().clear();
		playerTypes.setSpacing(10);
		for (int i = 0; i < numOfPlayersSelected; i++) {
			HBox getBox = getPlayerTypesBox();
			playerTypes.getChildren().addAll(getBox);
		}
	}

	/**
	 * Method to get plater details
	 * @return hBox		player details
	 */
	public HBox getPlayersDetailsBox() {
		PlayerDetailsController.numberOfPlayers++;

		TextField textField = new TextField();

		HBox hBox = new HBox();
		hBox.setSpacing(10);

		Label playerIdShow = new Label();
		playerIdShow.setText(String.valueOf(PlayerDetailsController.numberOfPlayers));
		hBox.getChildren().addAll(playerIdShow, textField);
		hBox.setAlignment(Pos.CENTER_RIGHT);
		return hBox;
	}

	/**
	 * Method to get player types
	 * @return hBox		players types
	 */
	public HBox getPlayerTypesBox(){
		String playerTypes[] = {IPlayerType.HUMAN, IPlayerType.AGGRESSIVE, IPlayerType.BENEVOLENT, IPlayerType.RANDOM,
				IPlayerType.CHEATER};
		ChoiceBox<String> playerType = new ChoiceBox<>();
		playerType.getItems().addAll(playerTypes);
		playerType.getSelectionModel().selectFirst();

		HBox hBox = new HBox();
		hBox.getChildren().addAll(playerType);
		return hBox;
	}

	/**
	 * Choice Box to select the number of players
	 *
	 * @param playerCount number of players
	 * @return player count
	 */
	public static ChoiceBox<Integer> initializePlayerCountValues(ChoiceBox<Integer> playerCount) {
		playerCount.getItems().removeAll(playerCount.getItems());
		playerCount.getItems().addAll(2,3, 4, 5, 6);

		return playerCount;
	}

	/**
	 * Start Method provides the Initial Window of any Game Play which uses JavaFX.
	 *
	 * @param event StartPlay Action Event
	 */
	@FXML
	private void startPlay(ActionEvent event) {
		ObservableList<Node> hBoxList = playerDetails.getChildren();
		ObservableList<Node> hBoxList1 = playerTypes.getChildren();
		List<String> playerNames = new LinkedList<>();
		List<String> playerTypes = new LinkedList<>();
		HashMap<String,String> playerDetailsAndType = new HashMap<>();
		if(validatePlayerDetails(hBoxList)){
			for(Node n : hBoxList){
				HBox hBox = (HBox) n;
				ObservableList<Node> node = hBox.getChildren();
				if(node.get(1) instanceof  TextField){
					playerNames.add(((TextField) node.get(1)).getText());
				}
			}
			for(Node n : hBoxList1){
				HBox hBox = (HBox) n;
				ObservableList<Node> node = hBox.getChildren();
				if(node.get(0) instanceof  ChoiceBox<?>){
					playerTypes.add((String)((ChoiceBox<?>) node.get(0)).getSelectionModel().getSelectedItem());
				}
			}

			for(int i=0;i<numOfPlayersSelected;i++){
				playerDetailsAndType.put(playerNames.get(i),playerTypes.get(i));
			}
		}
		else {
			GameUpdateWindow.popUpWindow("Player names warning", "Alert", "Please enter appropriate player names.");
		}



		startGame.setOnAction(new GamePhaseView(this.mapObj, playerDetailsAndType));
	}

	/**
	 * Method to validate details of player
	 * 
	 * @param hBoxList		hBoxList
	 * @return true or false
	 */
	public boolean validatePlayerDetails(ObservableList<Node> hBoxList) {
		for (Node n : hBoxList) {
			HBox box = (HBox) n;
			for (Node node : box.getChildren()) {
				if (node instanceof TextField) {
					if ((TextField) node == null || ((TextField) node).getText().trim().isEmpty()) {
						return false;
					}
				}
			}
		}
		return true;
	}

}
