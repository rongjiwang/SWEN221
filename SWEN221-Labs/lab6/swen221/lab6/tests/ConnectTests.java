package swen221.lab6.tests;

import static org.junit.Assert.*;
import org.junit.*;
import org.junit.runners.MethodSorters;

import swen221.lab6.connect.Game;
import swen221.lab6.connect.Game.Status;
import swen221.lab6.connect.core.Board;
import swen221.lab6.connect.core.Board.Token;
import swen221.lab6.connect.util.Position;

import org.junit.FixMethodOrder;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConnectTests {

	@Test public void test_01() {
		String output = "|_|_|_|_|\n" +
						"|_|_|_|_|\n" +
						"|_|_|_|_|\n" +
						"|_|_|_|_|\n";

		Board board = new Board();

		assertEquals(output,board.toString());
	}
	@Test public void test_02() {
		String output = "|W|W|W|W|\n" +
						"|B|B|B|_|\n" +
						"|_|_|_|_|\n" +
						"|_|_|_|_|\n";
		Board board = new Board();
		Game game = new Game(board);

		game.placeToken(new Position(0, 0), Token.WHITE);
		game.placeToken(new Position(0,1), Token.BLACK);
		
		game.placeToken(new Position(1, 0), Token.WHITE);
		game.placeToken(new Position(1,1), Token.BLACK);
		
		game.placeToken(new Position(2, 0), Token.WHITE);
		game.placeToken(new Position(2,1), Token.BLACK);
		
		game.placeToken(new Position(3, 0), Token.WHITE);
		//game.placeToken(new Position(3,1), Token.BLACK);

		//assertEquals(output,game.getStatus()==Status.WHITEWON);
		assertEquals(output,board.toString());

	}

}
