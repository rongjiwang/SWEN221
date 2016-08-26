package swen221.monopoly.testing;

import org.junit.*;
import static org.junit.Assert.*;

import swen221.monopoly.*;
import swen221.monopoly.GameOfMonopoly.InvalidMove;
import swen221.monopoly.locations.ColourGroup;
import swen221.monopoly.locations.Location;
import swen221.monopoly.locations.Property;
import swen221.monopoly.locations.SpecialArea;
import swen221.monopoly.locations.Station;
import swen221.monopoly.locations.Street;
import swen221.monopoly.locations.Utility;

public class MonopolyTests {
	// this is where you must write your tests; do not alter the package, or the
	// name of this file. An example test is provided for you.

	@Test
	public void testValidRules_1() {
		// Construct a "mini-game" of Monopoly and with a single player. The
		// player attempts to buy a property. We check that the right amount has
		// been deducted from his/her balance, and that he/she now owns the
		// property and vice-versa.
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			Player player = setupMockPlayer(game, "Park Lane", 1500);
			game.buyProperty(player);
			assertEquals(1150, player.getBalance());
			assertEquals("Park Lane", player.iterator().next().getName());
			// Test Street
			Street street = (Street) game.getBoard().findLocation("Park Lane");
			assertEquals(player, street.getOwner());
			game.sellProperty(player, game.getBoard().findLocation("Park Lane"));
			assertEquals(1500, player.getBalance());
			assertEquals(0, street.getHotels());
			assertEquals(0, street.getHouses());
			assertEquals("Park Lane", street.getName());
			assertEquals(null, street.getOwner());
			street.setOwner(player);
			assertEquals("Dave", street.getOwner().getName());
			assertEquals(350, street.getPrice());
			assertEquals(false, street.equals(null));
			assertEquals(false, street.isMortgaged());
			assertEquals(true, street.hasOwner());
			street.setHouses(1);
			street.setHotels(1);

			// Test ColorGroup
			assertEquals("Blue", street.getColourGroup().getColour());
			street.setColourGroup(new ColourGroup("Red", 350, new Street("Os Street", 350, 100)));
			assertEquals("Red", street.getColourGroup().getColour());
			// Test SpecialArea
			SpecialArea spe = new SpecialArea("post office");
			assertEquals("post office", spe.getName());
			assertEquals(false, spe.hasOwner());
			Location ll = (Location) spe;
			try {
				spe.getOwner();
			}

			catch (RuntimeException e) {
				System.out.print(e);
			}
			try {
				spe.getRent();
			} catch (RuntimeException e) {
				System.out.println(e);
			}
			// Test Station
			Property sta = new Station("Rail", 50);
			Station st1 = new Station("rail", 100);
			sta.setOwner(player);
			st1.setOwner(player);
			assertEquals(0, sta.getRent(6));
			// Test Utility
			Property uti = new Utility("Uti", 100);
			uti.setOwner(player);
			assertEquals(0, uti.getRent(2));

			street.getRent(2);
			// Test GameOfMonopoly
			game.movePlayer(player, 2);
			assertEquals(1500, player.getBalance());
			game.buyProperty(player);
			assertEquals(1100, player.getBalance());
			game.movePlayer(player, 0);
			game.mortgageProperty(player, player.getLocation());
			game.unmortgageProperty(player, player.getLocation());
			game.sellProperty(player, player.getLocation());
			game.buyProperty(player);
			game.buildHouses(player, player.getLocation(), 5);
			player.credit(10000);
			assertEquals(10080, player.getBalance());
			game.buildHotel(player, player.getLocation());
			assertEquals(9880, player.getBalance());
			// Error checking for Buy Property
			try {

				game.buyProperty(player);
			} catch (InvalidMove e) {
				System.out.println(e);
			}
			game.sellProperty(player, player.getLocation());
			player.deduct(10280);
			assertEquals(0, player.getBalance());
			try {
				game.buyProperty(player);
			} catch (InvalidMove e) {
				System.out.println(e);
			}
			// Error checking for Exceptions
			Board board = game.getBoard();
			Location kingCross = board.findLocation("Kings Cross Station");
			Player pp = new Player("Galliani", Player.Token.Iron, 200, kingCross);
			try {
				game.buyProperty(pp);
			} catch (GameOfMonopoly.InvalidMove e) {
				fail(e.getMessage());
			}

			try {
				game.sellProperty(player, player.getLocation());
			} catch (InvalidMove e) {
				System.out.println(e);
			}
			try {
				game.buyProperty(player);

			} catch (InvalidMove e) {
				System.out.println(e);
			}
			try {
				game.mortgageProperty(player, player.getLocation());

			} catch (InvalidMove e) {
				System.out.println(e);
			}
			try {
				game.unmortgageProperty(player, player.getLocation());

			} catch (InvalidMove e) {
				System.out.println(e);
			}
			try {
				game.mortgageProperty(player, player.getLocation());
			} catch (InvalidMove e) {
				System.out.println(e);
			}
			try {
				game.buildHouses(player, player.getLocation(), 6);
			} catch (InvalidMove e) {
				System.out.println(e);
			}
			try {
				game.buildHotel(player, player.getLocation());
			} catch (InvalidMove e) {
				System.out.println(e);
			}
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testUtility() throws InvalidMove {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Player player = setupMockPlayer(game, "Park Lane", 1500);
		Utility electricCompany = (Utility) board.findLocation("Electric Company");
		Utility waterWorks = (Utility) board.findLocation("Water Works");
		Property oxford = (Property) board.findLocation("Oxford Street");

		try {
			player.buy(electricCompany);
		} catch (IllegalArgumentException e) {
		}

		try {
			player.buy(waterWorks);
		} catch (IllegalArgumentException e) {
		}

		try {
			player.buy(oxford);
		} catch (IllegalArgumentException e) {
		}
		assertTrue(electricCompany.getRent(3) == 24);
		assertTrue(waterWorks.getRent(3) == 24);
	}

	@Test
	public void testStation() throws InvalidMove {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Player player = setupMockPlayer(game, "Park Lane", 1500);
		Station kingsCross = (Station) board.findLocation("Kings Cross Station");
		Station maryleboneStation = (Station) board.findLocation("Marylebone Station");
		Property waterWorks = (Property) board.findLocation("Water Works");

		try {
			player.buy(kingsCross);
		} catch (IllegalArgumentException e) {
		}

		try {
			player.buy(maryleboneStation);
		} catch (IllegalArgumentException e) {
		}

		try {
			player.buy(waterWorks);
		} catch (IllegalArgumentException e) {
		}

		assertFalse(kingsCross.getRent(3) == 100);
		assertFalse(maryleboneStation.getRent(3) == 100);
	}

	// Trying to buy a non-property location.
	@Test
	public void testInvalidBuyProperty() throws InvalidMove {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location chance = board.findLocation("Chance");
		Player player = new Player("Galliani", Player.Token.Iron, 200, chance);
		try {
			game.buyProperty(player);
			fail("Can't buy a non-property location.");
		} catch (GameOfMonopoly.InvalidMove e) {
		}
	}

	// Trying to sell a location that's not a property
	@Test
	public void testInvalidSellProperty() {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location chance = board.findLocation("Chance");
		Player player = new Player("Galliani", Player.Token.Iron, 200, chance);
		try {
			game.sellProperty(player, chance);
			fail("Can't sell a location that's not a property.");
		} catch (GameOfMonopoly.InvalidMove e) {
		}
	}

	// Trying to sell a property that is mortgaged
	@Test
	public void testInvalidSellProperty1() {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Property electricCompany = (Property) board.findLocation("Electric Company");
		Player player = new Player("Galliani", Player.Token.Iron, 200, electricCompany);
		electricCompany.setOwner(player);
		electricCompany.mortgage();
		try {
			game.sellProperty(player, electricCompany);
			fail("Can't sell a property that is mortgaged.");
		} catch (GameOfMonopoly.InvalidMove e) {
		}
	}

	// Trying to mortgage a location that's not a property
	@Test
	public void testInvalidMortgageProperty() {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location chance = board.findLocation("Chance");
		Player player = new Player("Galliani", Player.Token.Iron, 200, chance);
		try {
			game.mortgageProperty(player, chance);
			fail("Can't mortgage a location that's not a property.");
		} catch (GameOfMonopoly.InvalidMove e) {
		}
	}

	// Trying to mortgage a property that is already mortgaged
	@Test
	public void testInvalidMortgageProperty1() {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Property waterWorks = (Property) board.findLocation("Water Works");
		Player player = new Player("Galliani", Player.Token.Iron, 200, waterWorks);
		waterWorks.setOwner(player);
		waterWorks.mortgage();
		try {
			game.mortgageProperty(player, waterWorks);
			fail("Can't mortgage a property that is already mortgaged.");
		} catch (GameOfMonopoly.InvalidMove e) {
		}
	}

	// Trying to unmortgage a location that's not a property
	@Test
	public void testInvalidUnmortgageProperty() {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location chance = board.findLocation("Chance");
		Player player = new Player("Galliani", Player.Token.Iron, 200, chance);
		try {
			game.unmortgageProperty(player, chance);
			fail("Can't unmortgage a location that's not a property.");
		} catch (GameOfMonopoly.InvalidMove e) {
		}
	}

	// Trying to unmortgage a property that is not mortgaged
	@Test
	public void testInvalidUnmortgageProperty1() {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Property waterWorks = (Property) board.findLocation("Water Works");
		Player player = new Player("Galliani", Player.Token.Iron, 200, waterWorks);
		waterWorks.setOwner(player);
		try {
			game.unmortgageProperty(player, waterWorks);
			fail("Can't unmortgage a property that is not mortgaged.");
		} catch (GameOfMonopoly.InvalidMove e) {
		}
	}
	// Trying to unmortgage a property when player doesn't have enough funds

	@Test
	public void testValidUnmortgageProperty() {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Property waterWorks = (Property) board.findLocation("Water Works");
		Player player = new Player("Galliani", Player.Token.Iron, 200, waterWorks);
		waterWorks.setOwner(player);
		waterWorks.mortgage();
		try {
			game.unmortgageProperty(player, waterWorks);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Setup a mock game of monopoly with a player located at a given location.
	 */
	private Player setupMockPlayer(GameOfMonopoly game, String locationName, int balance)
			throws GameOfMonopoly.InvalidMove {
		Board board = game.getBoard();
		Location location = board.findLocation(locationName);
		return new Player("Dave", Player.Token.ScottishTerrier, balance, location);
	}
}
