package com.risk.team.strategy;

import com.risk.team.controller.GamePhaseController;
import com.risk.team.model.Country;
import com.risk.team.model.Dice;
import com.risk.team.model.Player;
import com.risk.team.services.Util.GameUpdateWindow;
import com.risk.team.view.DiceView;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

/**
 * Human class contains methods for the Player Behavior
 * Strategies of computer player.
 * <p>
 * A human player that requires user interaction to make decisions.
 *
 * @author yashgolwala
 * @author Harsh Mehta
 */
public class Human extends PlayerBehavior {

	/** Constructor for Human class */
	public Human() {

	}

	/**
	 * Object of GamePlayController, control various activities during the game play.
	 */
	private GamePhaseController gamePhaseController;

	/**
	 * Constructor method for Human class.
	 *
	 * @param gamePhaseController Attaching with observer.
	 */
	public Human(GamePhaseController gamePhaseController) {
		this.gamePhaseController = gamePhaseController;
		this.addObserver(gamePhaseController);
	}

	/**
	 * Method for Human class for reinforcement phase.
	 * Start and end of the reinforcement phase.
	 *
	 * @param countryList List of countries owned by the player.
	 * @param country Country to which reinforcement armies are to be assigned.
	 * @param currentPlayer Current player.
	 */
	@Override
	public void reinforcementPhase(ObservableList<Country> countryList, Country country, Player currentPlayer) {
		System.out.println("Beginning Reinforcement phase for human player " + currentPlayer.getName() + ".\n");
		setChanged();
		notifyObservers("Beginning Reinforcement phase for human player " + currentPlayer.getName() + ".\n");
		System.out.println("List of countries owned: " + countryList.toString() + "\n");
		setChanged();
		notifyObservers("List of countries owned: " + countryList.toString() + "\n");
		if (currentPlayer.getArmyCount() > 0) {
			if (country == null) {
				GameUpdateWindow.popUpWindow("No Country Selected", "popUp", "Please select a country");
				return;
			}

			int reinforcementArmies = Integer.valueOf(GameUpdateWindow.userInput());
			if (currentPlayer.getArmyCount() < reinforcementArmies) {
				GameUpdateWindow.popUpWindow("Insufficient Armies", "popUp", currentPlayer.getName() + " don't have enough armies");
				return;
			}
			country.setNoOfArmies(country.getNoOfArmies() + reinforcementArmies);
			currentPlayer.setArmyCount(currentPlayer.getArmyCount() - reinforcementArmies);
			System.out.println(country.getName() + " was assigned " + reinforcementArmies + " armies.\n");
			setChanged();
			notifyObservers(country.getName() + " was assigned " + reinforcementArmies + " armies.\n");

		}
	}

	/**
	 * Method for Human class for attack phase.
	 *
	 * @param attackingCountryList List of countries attacking.
	 * @param defendingCountryList List of countries defending.
	 * @param currentPlayer Current player.
	 */
	@Override
	public void attackPhase(ListView<Country> attackingCountryList, ListView<Country> defendingCountryList,
			Player currentPlayer) {
		Country attackingCountry = attackingCountryList.getSelectionModel().getSelectedItem();
		Country defendingCountry = defendingCountryList.getSelectionModel().getSelectedItem();
		if (attackingCountry != null && defendingCountry != null) {

			boolean playerCanAttack = isAttackMoveValid(attackingCountry, defendingCountry);

			if (playerCanAttack) {
				Dice dice = new Dice(attackingCountry, defendingCountry);
				dice.addObserver(currentPlayer);
				DiceView.openDiceWindow(dice, currentPlayer, this.gamePhaseController);
			}

		} else {
			GameUpdateWindow.popUpWindow("Country selection problem", "Country selection pop", currentPlayer.getName() + " please select attacking and defending countries.");

		}

	}

	/**
	 * Method for Human class for fortification phase.
	 * Start and end of the fortification phase.
	 *
	 * @param selectedCountryList List of countries selected by the player.
	 * @param adjCountryList List of adjacent countries.
	 * @param playerPlaying Current player.
	 * @return true If the fortification successful; other wise false.
	 */
	@Override
	public boolean fortificationPhase(ListView<Country> selectedCountryList, ListView<Country> adjCountryList,
			Player playerPlaying) {
		Country selectedCountry = selectedCountryList.getSelectionModel().getSelectedItem();
		Country adjCountry = adjCountryList.getSelectionModel().getSelectedItem();
		if (selectedCountry == null) {
			GameUpdateWindow.popUpWindow("Please choose Selected Country as source.", "Message", "");
			return false;
		} else if (adjCountry == null) {
			GameUpdateWindow.popUpWindow("Please choose Adjacent Country as destination.", "Message", "");
			return false;
		} else if (!(adjCountry.getPlayer().equals(playerPlaying))) {
			GameUpdateWindow.popUpWindow("Adjacent Country does not belong to you.", "Message", "");
			return false;
		}

		Integer armies = Integer.valueOf(GameUpdateWindow.userInput());
		if (armies > 0) {
			if (selectedCountry.getNoOfArmies() == armies) {
				GameUpdateWindow.popUpWindow("All armies selected to move", "Fortification Phase popup", "You have to leave at least one army behind.");
				return false;
			} else if (selectedCountry.getNoOfArmies() < armies) {
				GameUpdateWindow.popUpWindow("You do not have sufficient armies", "Fortification Phase popup", "Please select less number of armies");
				return false;
			} else {
				selectedCountry.setNoOfArmies(selectedCountry.getNoOfArmies() - armies);
				adjCountry.setNoOfArmies(adjCountry.getNoOfArmies() + armies);
				System.out.println(armies + " armies placed on " + adjCountry.getName() + " country.\n");
				System.out.println("Fortification phase ended.\n");
				setChanged();
				notifyObservers(armies + " armies placed on " + adjCountry.getName() + " country.\n");
				setChanged();
				notifyObservers("Fortification phase ended.\n");
				setChanged();
				notifyObservers("Reinforcement");
				return true;
			}
		} else {
			GameUpdateWindow.popUpWindow("Invalid number", "Fortification Phase popup", "Please enter a valid number");
			return false;
		}
	}

	/**
	 * Method to check if the attack move is valid or not.
	 *
	 * @param attacking Country attacking.
	 * @param defending Country under attack.
	 * @return true If the attack move is valid; other wise false.
	 */
	public boolean isAttackMoveValid(Country attacking, Country defending) {
		boolean isValidAttackMove = false;
		if (defending.getPlayer() != attacking.getPlayer()) {
			if (attacking.getNoOfArmies() > 1) {
				isValidAttackMove = true;
			} else {
				GameUpdateWindow.popUpWindow("Select a country with more armies.", "Invalid game move", "There should be more than one army on the country which is attacking.");
			}
		} else {
			GameUpdateWindow.popUpWindow("You have selected your own country", "Invalid game move", "Select another player's country to attack");
		}
		return isValidAttackMove;
	}

	/**
	 * Method to check if the player can attack or not.
	 *
	 * @param countries List view of all the countries of the player.
	 * @return true If the player can attack; other wise false.
	 */
	@Override
	public boolean playerCanAttack(ListView<Country> countries) {
		boolean canAttack = false;
		for (com.risk.team.model.Country Country : countries.getItems()) {
			if (Country.getNoOfArmies() > 1) {
				canAttack = true;
			}
		}
		if (!canAttack) {
			System.out.println("Player cannot continue with attack phase, move to fortification phase.\n");
			System.out.println("Attack phase ended\n");
			setChanged();
			notifyObservers("Player cannot continue with attack phase, move to fortification phase.\n");
			setChanged();
			notifyObservers("Attack phase ended\n");
		}
		return canAttack;
	}

}
