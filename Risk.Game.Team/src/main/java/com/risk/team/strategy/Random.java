package com.risk.team.strategy;

import com.risk.team.controller.GamePhaseController;
import com.risk.team.controller.RiskDiceController;
import com.risk.team.model.Country;
import com.risk.team.model.Dice;
import com.risk.team.model.Player;
import com.risk.team.model.TournamentModel;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Random class contains methods for the Player Behavior
 * Strategies of computer player.
 * <p>
 * A random computer player strategy that reinforces random a
 * random country, attacks a random number of times a random
 * country, and fortifies a random country, all following the
 * standard rules for each phase.
 *
 * @author yashgolwala
 * @author Harsh Mehta
 * 
 * @version 1.0.0
 */
public class Random extends PlayerBehavior {

	/**
	 * Object of GamePlayController, control various activities during the game play.
	 */
	private GamePhaseController gamePhaseController;

	/** Constructor for Random class */
	public Random() {
	}

	/**
	 * Constructor method for Random class.
	 *
	 * @param gamePhaseController Attaching with observer.
	 */
	public Random(GamePhaseController gamePhaseController) {
		this.gamePhaseController = gamePhaseController;
		this.addObserver(gamePhaseController);
	}

	/**
	 * Method for Random class for reinforcement phase.
	 * Start and end of the reinforcement phase.
	 *
	 * @param countryList   List of countries owned by the player.
	 * @param country       Country to which reinforcement armies are to be assigned.
	 * @param currentPlayer Current player.
	 */
	@Override
	public void reinforcementPhase(ObservableList<Country> countryList, Country country, Player currentPlayer) {
		System.out.println("Beginning Reinforcement phase for random player " + currentPlayer.getName() + ".\n");
		setChanged();
		notifyObservers("Beginning Reinforcement phase for random player " + currentPlayer.getName() + ".\n");
		Country randomCountry = countryList.get(new java.util.Random().nextInt(countryList.size()));
		int armies = currentPlayer.getArmyCount();
		if (armies > 0) {
			randomCountry.setNoOfArmies(randomCountry.getNoOfArmies() + armies);
			currentPlayer.setArmyCount(currentPlayer.getArmyCount() - armies);
			System.out.println("Country " + randomCountry.getName() + " has been assigned " + armies + " armies.\n");
			setChanged();
			notifyObservers("Country " + randomCountry.getName() + " has been assigned " + armies + " armies.\n");
		}
		System.out.println("Ended Reinforcement phase for random player " + currentPlayer.getName() + ".\n");
		setChanged();
		notifyObservers("Ended Reinforcement phase for random player " + currentPlayer.getName() + ".\n");
	}

	/**
	 * Method for Random class for attack phase.
	 * Start and end of the attack phase.
	 *
	 * @param attackingCountryList List of countries attacking.
	 * @param defendingCountryList List of countries defending.
	 * @param currentPlayer        Current player.
	 */
	@Override
	public void attackPhase(ListView<Country> attackingCountryList, ListView<Country> defendingCountryList,
			Player currentPlayer) {
		System.out.println("Beginning attack phase for random player " + currentPlayer.getName() + ".\n");
		setChanged();
		notifyObservers("Beginning attack phase for random player " + currentPlayer.getName() + ".\n");
		ObservableList<Country> attackableCountries = attackingCountryList.getItems();
		System.out.println("Attackable country list=" + attackableCountries.toString());
		setChanged();
		notifyObservers("Attackable country list=" + attackableCountries.toString());
		Country attackingCountry;
		while ((attackingCountry = attackableCountries.get(new java.util.Random().nextInt(attackableCountries.size()))).getNoOfArmies() < 2) {
		}
		System.out.println("Attacking country = " + attackingCountry.getName() + " , no of armies=" + attackingCountry.getNoOfArmies());
		setChanged();
		notifyObservers("Attacking country = " + attackingCountry.getName() + " , no of armies=" + attackingCountry.getNoOfArmies());
		List<Country> defendingCountries;

		while ((defendingCountries = getDefendingCountryList(attackingCountry)).isEmpty()) {
			attackingCountry = attackableCountries.get(new java.util.Random().nextInt(attackableCountries.size()));
		}

		Country defendingCountry = defendingCountries.get(new java.util.Random().nextInt(defendingCountries.size()));

		System.out.println("Attacking from random country " + attackingCountry.getName() + " to random country " + defendingCountry.getName() + ".\n");
		setChanged();
		notifyObservers("Attacking from random country " + attackingCountry.getName() + " to random country " + defendingCountry.getName() + ".\n");
		attack(attackingCountry, defendingCountry, currentPlayer);

		System.out.println("Ended Attack phase for random player " + currentPlayer.getName() + ".\n");
		setChanged();
		notifyObservers("Ended Attack phase for random player " + currentPlayer.getName() + ".\n");
	}

	/**
	 * Method for Random class for fortification phase.
	 * Start and end of the fortification phase.
	 *
	 * @param selectedCountryList List of countries selected by the player.
	 * @param adjCountryList      List of adjacent countries.
	 * @param currentPlayer       Current player.
	 * @return true
	 * If the fortification successful; other wise false.
	 */
	@Override
	public boolean fortificationPhase(ListView<Country> selectedCountryList, ListView<Country> adjCountryList,
			Player currentPlayer) {
		System.out.println("Beginning Fortification phase for random player " + currentPlayer.getName() + ".\n");
		setChanged();
		notifyObservers("Beginning Fortification phase for random player " + currentPlayer.getName() + ".\n");
		setChanged();
		notifyObservers("List of countries owned: " + selectedCountryList.getItems() + "\n");
		System.out.println("List of countries owned: " + selectedCountryList.getItems() + "\n");
		ObservableList<Country> selectedCountry = selectedCountryList.getItems();
		Country countryToFortify = selectedCountry.get(new java.util.Random().nextInt(selectedCountry.size()));
		List<Country> adjacentOwnedCountryList;
		while ((adjacentOwnedCountryList = getAdjacentOwnedCountryList(countryToFortify)).isEmpty()) {
			countryToFortify = selectedCountry.get(new java.util.Random().nextInt(selectedCountry.size()));
		}
		Country countryFromFortify = adjacentOwnedCountryList.get(new java.util.Random().nextInt(adjacentOwnedCountryList.size()));

		if (countryFromFortify.getNoOfArmies() >= 2) {
			int randomArmies = new java.util.Random().nextInt(countryFromFortify.getNoOfArmies() - 1) + 1;
			countryToFortify.setNoOfArmies(countryToFortify.getNoOfArmies() + randomArmies);
			countryFromFortify.setNoOfArmies(countryFromFortify.getNoOfArmies() - randomArmies);
			System.out.println("Fortified " + randomArmies + " from random country " + countryFromFortify.getName()
			+ " to random country " + countryToFortify.getName() + ".\n");
			setChanged();
			notifyObservers("Fortified " + randomArmies + " from random country " + countryFromFortify.getName()
			+ " to random country " + countryToFortify.getName() + ".\n");
		}
		System.out.println("Ended Fortification phase for random player " + currentPlayer.getName() + ".\n");
		setChanged();
		notifyObservers("Ended Fortification phase for random player " + currentPlayer.getName() + ".\n");
		return true;
	}

	/**
	 * Method for Random class for if player can attack.
	 *
	 * @param countries List of countries owned by the player.
	 * @return true
	 * If player can attack; other wise false.
	 */
	@Override
	public boolean playerCanAttack(ListView<Country> countries) {
		boolean canAttack = false;
		for (Country country : countries.getItems()) {
			if (country.getNoOfArmies() > 1 && getDefendingCountryList(country).size() > 0) {
				canAttack = true;
			}
		}

		if (!canAttack) {
			System.out.println("Random player cannot continue with attack phase, move to fortification phase.\n");
			System.out.println("Attack phase ended for random player.\n");
			setChanged();
			notifyObservers("Random player cannot continue with attack phase, move to fortification phase.\n");
			setChanged();
			notifyObservers("Attack phase ended for random player.\n");
		}
		return canAttack;
	}

	/**
	 * Method for Random class for attack.
	 *
	 * @param attacking     Country attacking.
	 * @param defending     Country defending.
	 * @param currentPlayer Current player.
	 */
	private void attack(Country attacking, Country defending, Player currentPlayer) {
		Dice diceModel = new Dice(attacking, defending);
		if (currentPlayer != null) {
			diceModel.addObserver(currentPlayer);
		}

		if (TournamentModel.isTournamentMode) {
			RiskDiceController diceController = new RiskDiceController(diceModel, this);
			diceController.automateDiceRoll();
		} else {
			RiskDiceController diceController = new RiskDiceController(diceModel, this, this.gamePhaseController);
			diceController.automateDiceRoll();
		}

	}

	/**
	 * Method to get list of adjacent countries owned.
	 *
	 * @param attackingCountry List of countries attacking.
	 * @return List
	 * List of adjacent countries owned.
	 */
	public List<Country> getAdjacentOwnedCountryList(Country attackingCountry) {
		List<Country> adjacentOwnedCountries = attackingCountry.getAdjacentCountries().stream()
				.filter(t -> ((attackingCountry.getPlayer() == t.getPlayer()) && t.getNoOfArmies() > 1)).collect(Collectors.toList());

		return adjacentOwnedCountries;

	}

}
