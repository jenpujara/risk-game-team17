package com.risk.team.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.risk.team.model.Card;
import com.risk.team.model.Player;
import com.risk.team.services.util.GameUpdateWindow;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Card Controller Class
 *
 * @author yashgolwala
 * 
 * @version 2.0.0
 */

public class RiskCardController implements Initializable {

	/**
	 * Object for Player class
	 */
	private Player player;

	/**
	 * Object for Card class
	 */
	private Card card;

	/**
	 * Label variable for the current Player
	 */
	@FXML
	private Label currentPlayer;

	/**
	 * Variable for the exchange button
	 */
	@FXML
	private Button exchange;

	/**
	 * List of cards owned by the player
	 */
	private ArrayList<Card> playerOwnedCards;

	/**
	 * checkbox array
	 */
	private CheckBox[] checkBox;

	/**
	 * variable for VBox cardVbox
	 */
	@FXML
	private VBox cardVbox;

	/**
	 * Variable for the text
	 */
	@FXML
	private Label text;

	/**
	 * variable for the cancel view button
	 */
	@FXML
	private Button cancelView;

	/**
	 * Card Controller constructor class
	 *
	 * @param player Current Player
	 * @param card   player card
	 */
	public RiskCardController(Player player, Card card) {
		this.player = player;
		this.card = card;
	}

	/**
	 * Method to initialize player attributes for cards
	 *
	 * @param location  URL
	 * @param resources ResourceBundle
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		currentPlayer.setText("Cards of " + player.getName());
		playerOwnedCards = player.getCardList();
		if (playerOwnedCards.size() < 3) {
			exchange.setDisable(true);
		} else {
			exchange.setDisable(false);
		}
		loadCards();
	}

	/**
	 * Method to automate card exchange
	 */
	public void automaticCardInitialization() {
		automaticInitializeComponents();
		performCardTrade();
	}

	/**
	 * Method to perform card exchange
	 */
	public void performCardTrade() {
		playerOwnedCards = player.getCardList();
		List<Card> cards = card.generateValidCardCombination(playerOwnedCards);
		if (cards != null && cards.size() >= 3) {
			card.cardsToBeTraded(cards);
		}
	}

	/**
	 * Method to load cards
	 */
	public void loadCards() {
		int initialCardsCount = playerOwnedCards.size();
		checkBox = new CheckBox[initialCardsCount];
		for (int i = 0; i < initialCardsCount; i++) {
			checkBox[i] = new CheckBox(playerOwnedCards.get(i).getCardType().toString() + " -> " + playerOwnedCards.get(i).getCountry().getName());
		}
		cardVbox.getChildren().addAll(checkBox);
	}

	/**
	 * Method for cancel view action
	 *
	 * @param event Event
	 */
	@FXML
	private void cancelView(ActionEvent event) {
		GameUpdateWindow.exitWindow(cancelView);
	}

	/**
	 * Method for check exchange. Checks for the card
	 * combination and number of cards selected by the player
	 *
	 * @param event Action Event
	 */
	@FXML
	private void checkTrade(ActionEvent event) {
		exchange.setDisable(false);
		text.setText(null);
		List<Card> selectedCards = card.chooseCards(playerOwnedCards, checkBox);

		if (selectedCards.size() == 3) {
			boolean flag = card.isTradePossible(selectedCards);

			if (flag) {
				card.cardsToBeTraded(selectedCards);
				GameUpdateWindow.exitWindow(exchange);
			} else {
				text.setText("This Combination is not Valid");
				exchange.setDisable(false);
				return;
			}
		} else {
			text.setText("Select only 3 Cards");
			return;
		}
	}

	/**
	 * Method to automatically initialize components
	 */
	public void automaticInitializeComponents() {
		currentPlayer = new Label();
		exchange = new Button();
		cardVbox = new VBox();
		text = new Label();
		cancelView = new Button();
	}

}
