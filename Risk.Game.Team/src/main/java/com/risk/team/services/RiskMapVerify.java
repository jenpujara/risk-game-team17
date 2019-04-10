package com.risk.team.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.risk.team.model.Continent;
import com.risk.team.model.Country;

/**
 * Map Validation class contains methods for the verification of a already map
 * file, that the user will load for starting a game.
 *
 * @author Kartika Patil
 * 
 * @version 1.0.0
 * 
 */
public class RiskMapVerify implements Serializable {

	/**
	 * Containing set of all the continents
	 */
	private HashMap<String, Continent> continentSetOfContinents;

	/**
	 * Continent set generated from the territories data
	 */
	private HashMap<String, Continent> continentSetOfTerritories;

	/**
	 * Containing set of all the countries
	 */
	private HashMap<String, Country> countrySet;

	/**
	 * Containing set of all the adjacent countries to a particular country
	 */
	private HashMap<Country, ArrayList<Country>> adjacentCountries;

	/**
	 * HashMap containing continent as key and countries belonging to continent
	 * as value
	 */
	private HashMap<Continent, HashSet<Country>> countriesInContinent;

	/**
	 * Name of the file to be validated
	 */
	private String fileName;

	/**
	 * List containing all the mapTag data, like name of author, scroll etc.
	 */
	private ArrayList<String> mapTagData;

	/**
	 * A no argument constructor for initializing all the data fields of the
	 * MapValidate class.
	 */
	public RiskMapVerify() {
		this.continentSetOfContinents = new HashMap<>();
		this.continentSetOfTerritories = new HashMap<>();
		this.adjacentCountries = new HashMap<>();
		this.countriesInContinent = new HashMap<>();
		this.mapTagData = new ArrayList<>();
		this.countrySet = new HashMap<>();
	}

	/**
	 * Method to get continent set generated from territories data.
	 *
	 * @return HashMap which is continentSetOfTerritories
	 */
	public HashMap<String, Continent> getContinentSetOfTerritories() {
		return continentSetOfTerritories;
	}

	/**
	 * Method to set continent set generated from territories data.
	 *
	 * @param continentSetOfTerritories which is continent set generated from territories data.
	 */
	public void setContinentSetOfTerritories(HashMap<String, Continent> continentSetOfTerritories) {
		this.continentSetOfTerritories = continentSetOfTerritories;
	}

	/**
	 * Method to get Containing set of continent.
	 *
	 * @return HashMap which is continentSetOfTerritories
	 */
	public HashMap<String, Continent> getContinentSetOfContinents() {
		return continentSetOfContinents;
	}

	/**
	 * Method to get adjacent countries to a country.
	 *
	 * @return HashMap which contains adjacent
	 * countries corresponding each country.
	 */
	public HashMap<Country, ArrayList<Country>> getAdjacentCountries() {
		return adjacentCountries;
	}

	/**
	 * Method to get countries in a continent.
	 *
	 * @return HashMap which contains countries in
	 * a continent.
	 */
	public HashMap<Continent, HashSet<Country>> getCountriesInContinent() {
		return countriesInContinent;
	}

	/**
	 * Method to get file name.
	 *
	 * @return String fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Method to get mapTagData.
	 *
	 * @return ArrayList Contents of map details
	 */
	public ArrayList<String> getMapTagData() {
		return mapTagData;
	}

	/**
	 * Method for validation of a map file. At first it check for validity of
	 * map tag data. Then it checks for continent and control value. After
	 * receiving the the continent and control value it moves to check
	 * countries.
	 * <p>
	 * In territories it reads all the countries one by one, along with its x, y
	 * coordinates and continent. Then it reads the list of adjacent countries
	 * and checks for the validation, that whether the two countries are present
	 * in each others list of adjacent countries or not
	 *
	 * @param mapFile String that contains name of the file to be validated.
	 * @return true if map is validated; otherwise false
	 */
	public boolean validateMapFile(String mapFile) {
		this.fileName = mapFile;

		if (mapFile != null) {
			try (BufferedReader read = new BufferedReader(new FileReader(mapFile))) {
				String inputText = new String(Files.readAllBytes(Paths.get(mapFile)), StandardCharsets.UTF_8);
				if (!checkAllTags(inputText)) {
					System.out.println("Missing tags or wrong tags.");
					return false;
				}

				for (String line; (line = read.readLine()) != null; ) {
					if (line.trim().equals("[Map]")) {
						while (!((line = read.readLine()).equals("[Continents]"))) {
							if (!line.trim().isEmpty() && !(line.contains("="))) {
								System.out.print("Invalid map configuration");
								return false;
							}
							mapTagData.add(line);
						}
					}
					if (line.equals("[Continents]")) {
						while (!((line = read.readLine()).equals("[Territories]"))) {
							Pattern pattern = Pattern.compile("[a-z, A-Z]+=[0-9]+");
							if (!line.trim().isEmpty()) {
								Matcher match = pattern.matcher(line.trim());
								if (!match.matches()) {
									if (line.trim().equals("[Territories]")) {
										break;
									}
									System.out.print("Invalid continent configuration");
									return false;
								}

								if (continentSetOfContinents.containsKey(line.split("=")[0])) {
									System.out.println("Continent is already defined.");
									return false;
								}
								Continent continent = new Continent(line.split("=")[0],
										Integer.parseInt(line.split("=")[1]));
								continentSetOfContinents.put(continent.getName(), continent);
							}
						}
					}
					if (line.equals("[Territories]")) {
						while ((line = read.readLine()) != null) {
							if (!line.trim().isEmpty()) {
								String input[] = line.split(",");
								if (continentSetOfTerritories.get(input[3].trim()) == null) {
									Continent continent = new Continent(input[3].trim(), 0);
									continent.setControlValue(
											continentSetOfContinents.get(input[3].trim()).getControlValue());
									continentSetOfTerritories.put(continent.getName(), continent);
								}
								Country country = null;
								if (!countriesInContinent.keySet().contains(continentSetOfTerritories.get(input[3]))) {
									HashSet<Country> countries = new HashSet<>();
									if (!countrySet.containsKey(input[0].trim())) {
										country = new Country(input[0].trim());
										country.setContinent(continentSetOfTerritories.get(input[3]).getName());
										country.setxValue(input[1]);
										country.setyValue(input[2]);
										countrySet.put(input[0].trim(), country);
									} else {
										country = countrySet.get(input[0].trim());
										country.setContinent(continentSetOfTerritories.get(input[3]).getName());
										country.setxValue(input[1]);
										country.setyValue(input[2]);
									}
									country.setPartOfContinent(continentSetOfTerritories.get(input[3]));
									countries.add(country);
									for (Continent continent : countriesInContinent.keySet()) {
										if (continent.getListOfCountries().contains(country)) {
											continent.getListOfCountries().remove(country);
											countriesInContinent.get(continent).remove(country);
										}
									}
									for (Continent continent : continentSetOfTerritories.values()) {
										if (continent.getListOfCountries().contains(country)) {
											continent.getListOfCountries().remove(country);
										}
									}

									if (!continentSetOfTerritories.get(input[3]).getListOfCountries()
											.contains(country)) {
										continentSetOfTerritories.get(input[3]).getListOfCountries().add(country);
									}
									if (countriesInContinent.get(continentSetOfTerritories.get(input[3])) == null) {
										countriesInContinent.put(continentSetOfTerritories.get(input[3]), countries);
									} else {
										if (!countriesInContinent.get(continentSetOfTerritories.get(input[3])).contains(country)) {
											countriesInContinent.get(continentSetOfTerritories.get(input[3])).add(country);
										}
									}

								} else {
									HashSet<Country> countries = getCountriesFromContinent(input[3].trim(),
											countriesInContinent);
									if (!countrySet.containsKey(input[0].trim())) {
										country = new Country(input[0].trim());
										country.setxValue(input[1]);
										country.setyValue(input[2]);
										country.setContinent(continentSetOfTerritories.get(input[3]).getName());
										countrySet.put(input[0].trim(), country);
									} else {
										country = countrySet.get(input[0].trim());
										country.setContinent(continentSetOfTerritories.get(input[3]).getName());
										country.setxValue(input[1]);
										country.setyValue(input[2]);
									}
									country.setPartOfContinent(continentSetOfTerritories.get(input[3]));
									countries.add(country);
									for (Continent continent : countriesInContinent.keySet()) {
										if (continent.getListOfCountries().contains(country)) {
											continent.getListOfCountries().remove(country);
											countriesInContinent.get(continent).remove(country);
										}
									}
									for (Continent continent : continentSetOfTerritories.values()) {
										if (continent.getListOfCountries().contains(country)) {
											continent.getListOfCountries().remove(country);
										}
									}

									if (!continentSetOfTerritories.get(input[3]).getListOfCountries()
											.contains(country)) {
										continentSetOfTerritories.get(input[3]).getListOfCountries().add(country);
									}
									if (countriesInContinent.get(continentSetOfTerritories.get(input[3])) == null) {
										countriesInContinent.put(continentSetOfTerritories.get(input[3]), countries);
									} else {
										if (!countriesInContinent.get(continentSetOfTerritories.get(input[3])).contains(country)) {
											countriesInContinent.get(continentSetOfTerritories.get(input[3])).add(country);
										}
									}
								}
								ArrayList<Country> countries = new ArrayList<>();
								for (int i = 4; i < input.length; ++i) {
									Country adjacentCountry;
									if (!countrySet.containsKey(input[i].trim())) {
										adjacentCountry = new Country(input[i].trim());
										adjacentCountry.getAdjacentCountries().add(country);
										adjacentCountry.setPartOfContinent(continentSetOfTerritories.get(input[3]));
										adjacentCountry.setContinent(continentSetOfTerritories.get(input[3]).getName());
										countrySet.put(adjacentCountry.getName(), adjacentCountry);
										if (!continentSetOfTerritories.get(input[3]).getListOfCountries().contains(adjacentCountry)) {
											continentSetOfTerritories.get(input[3]).getListOfCountries().add(adjacentCountry);
										}
										if (!countriesInContinent.get(continentSetOfTerritories.get(input[3])).contains(adjacentCountry)) {
											countriesInContinent.get(continentSetOfTerritories.get(input[3])).add(adjacentCountry);
										}
										if (adjacentCountries.get(adjacentCountry) == null) {
											adjacentCountries.put(adjacentCountry, adjacentCountry.getAdjacentCountries());
										} else {
											if (!adjacentCountries.get(adjacentCountry).contains(country))
												adjacentCountries.get(adjacentCountry).add(country);
										}
									} else {
										adjacentCountry = countrySet.get(input[i].trim());
										if (!adjacentCountry.getAdjacentCountries().contains(country)) {
											adjacentCountry.getAdjacentCountries().add(country);
										}
									}
									countries.add(adjacentCountry);
								}
								for (Country country1 : countries) {
									if (!country.getAdjacentCountries().contains(country1)) {
										country.getAdjacentCountries().add(country1);
									}
								}
								if (adjacentCountries.get(country) == null) {
									adjacentCountries.put(country, countries);
								} else {
									for (Country country1 : countries) {
										if (!adjacentCountries.get(country).contains(country1)) {
											adjacentCountries.get(country).add(country1);
										}
									}
								}
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			for (Map.Entry<Continent, HashSet<Country>> countries : countriesInContinent.entrySet()) {
				if (countries.getValue().size() < 1) {
					System.out.println(countries);
					System.out.println("Number of countries in a continent is less");
					return false;
				}
			}

			for (Map.Entry<Country, ArrayList<Country>> countries : adjacentCountries.entrySet()) {
				Country checkCountry = countries.getKey();
				ArrayList<Country> neighbours = countries.getValue();
				for (Country adjacent : neighbours) {
					if (adjacentCountries.get(adjacent) == null) {
						System.out.println("Adjacency list of country " + adjacent.getName() + " is not present.");
						return false;
					}
					if (!adjacentCountries.get(adjacent).contains(checkCountry)) {
						System.out.println(adjacent.getName() + " is not adjacent to " + checkCountry.getName());
						System.out.println("Countries are not adjacent");
						return false;
					}
				}
			}
		}

		if (continentSetOfContinents.size() != continentSetOfTerritories.size()) {
			System.out.println("Number of continents defined in continents tag does not match "
					+ " with the continents defined in the territories tag");
			return false;
		}

		RiskGraphConnected connected = new RiskGraphConnected(new HashSet<Country>(countrySet.values()));

		if (!connected.isConnected()) {
			return false;
		}

		for (Continent continent : continentSetOfTerritories.values()) {
			RiskGraphConnected connectedGraph = new RiskGraphConnected(new HashSet<>(continent.getListOfCountries()));
			if (!connectedGraph.isConnectedSubGraph()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Method for getting countries corresponding to a country.
	 *
	 * @param continent             String that is the name of the continent.
	 * @param countriesInAContinent HashMap consisting of continents and their countries.
	 * @return HashSet countries inside a continent.
	 */
	public HashSet<Country> getCountriesFromContinent(String continent,
			HashMap<Continent, HashSet<Country>> countriesInAContinent) {
		for (Map.Entry<Continent, HashSet<Country>> pair : countriesInAContinent.entrySet()) {
			if (pair.getKey().getName().equals(continent)) {
				return pair.getValue();
			}
		}
		return null;
	}

	/**
	 * Method for checking whether all the attributes are present in a map file
	 * or not, these attributes are mapTag data, continents and territories.
	 *
	 * @param fileData String that has the particular tags.
	 * @return true if mapTagData is present; otherwise false.
	 */
	public boolean checkAllTags(String fileData) {
		if (!fileData.contains("[Map]") || countOccurrences(fileData, "[Map]") != 1) {
			return false;
		} else if (!fileData.contains("[Continents]") || countOccurrences(fileData, "[Continents]") != 1) {
			return false;
		} else if (!fileData.contains("[Territories]") || countOccurrences(fileData, "[Territories]") != 1) {
			return false;
		}

		return true;
	}

	/**
	 * Method for checking that all tags occur only once or not.
	 *
	 * @param input  String
	 * @param search String
	 * @return int is not number of occurrences.
	 */
	public int countOccurrences(String input, String search) {
		int count = 0, startIndex = 0;
		Pattern pattern = Pattern.compile(search, Pattern.LITERAL);
		Matcher match = pattern.matcher(input);

		while (match.find(startIndex)) {
			count++;
			startIndex = match.start() + 1;
		}
		return count;
	}

	/**
	 * Method for getting all the countries in the country set.
	 *
	 * @return HashMap countrySet.
	 */
	public HashMap<String, Country> getCountrySet() {
		return countrySet;
	}

	/**
	 * Method for setting continents in the containing set of continents.
	 *
	 * @param continentSetOfContinents HashMap containing, continents of the map.
	 */
	public void setContinentSetOfContinents(HashMap<String, Continent> continentSetOfContinents) {
		this.continentSetOfContinents = continentSetOfContinents;
	}

}
