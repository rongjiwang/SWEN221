package minesweeper.moves;

import minesweeper.*;
import minesweeper.squares.*;

/**
 * Represents a move that exposes a square. The square may contain a bomb, or be
 * blank. However, it should not be flagged.
 * 
 * @author David J. Pearce
 * 
 */
public class ExposeMove extends Move {

	/**
	 * Construct an ExposeMove at a given position on the board.
	 * 
	 * @param position
	 */
	public ExposeMove(Position position) {
		super(position);
	}

	/**
	 * Apply this move to a given game and check it is valid. A square can only
	 * be exposed if it is not already exposed. And, if either a bomb is exposed
	 * or there are no remaining unexposed squares, then this should be an
	 * EndGame move.
	 * 
	 * @param game
	 *            --- game to which this move is applied.
	 */
	public void apply(Game game) throws SyntaxError {
		Square square = game.squareAt(position);
		System.out.println(game.getHeight()+" :"+ game.getWidth()+"000000000000000000000000000000");

		// TODO: check that square is hidden and not flagged.
		int x = position.getX();
		int y = position.getY();
		System.out.println(game.getHeight()+" :"+ game.getWidth());
		if (!square.isFlagged() && square.isHidden()) {
			BlankSquare bSquare = (BlankSquare) square;
			if (bSquare.getNumberOfBombsAround() == 0) {
				System.out.println("no bomb around");
				for (int i = 0; i < game.getHeight(); i++) {
					for (int j = 0; j < game.getWidth(); j++) {
						if (i == (x + 1) && j == y) {
							if (game.squareAt(new Position(i, j)) instanceof BlankSquare
									&& game.squareAt(new Position(i, j)).isHidden()
									&& !game.squareAt(new Position(i, j)).isFlagged()) {
								game.squareAt(new Position(i, j)).setHidden(false);
							}
						}
						if (i == (x - 1) && j == y) {
							if (game.squareAt(new Position(i, j)) instanceof BlankSquare
									&& game.squareAt(new Position(i, j)).isHidden()
									&& !game.squareAt(new Position(i, j)).isFlagged()) {
								game.squareAt(new Position(i, j)).setHidden(false);

							}
						}
						if (i == x && j == (y + 1)) {
							if (game.squareAt(new Position(i, j)) instanceof BlankSquare
									&& game.squareAt(new Position(i, j)).isHidden()
									&& !game.squareAt(new Position(i, j)).isFlagged()) {
								game.squareAt(new Position(i, j)).setHidden(false);

							}
						}
						if (i == x && j == (y - 1)) {
							if (game.squareAt(new Position(i, j)) instanceof BlankSquare
									&& game.squareAt(new Position(i, j)).isHidden()
									&& !game.squareAt(new Position(i, j)).isFlagged()) {
								game.squareAt(new Position(i, j)).setHidden(false);

							}
						}
						if (i == (x + 1) && j == (y + 1)) {
							if (game.squareAt(new Position(i, j)) instanceof BlankSquare
									&& game.squareAt(new Position(i, j)).isHidden()
									&& !game.squareAt(new Position(i, j)).isFlagged()) {
								game.squareAt(new Position(i, j)).setHidden(false);

							}
						}
						if (i == (x + 1) && j == (y - 1)) {
							if (game.squareAt(new Position(i, j)) instanceof BlankSquare
									&& game.squareAt(new Position(i, j)).isHidden()
									&& !game.squareAt(new Position(i, j)).isFlagged()) {
								game.squareAt(new Position(i, j)).setHidden(false);

							}
						}
						if (i == (x - 1) && j == (y + 1)) {
							if (game.squareAt(new Position(i, j)) instanceof BlankSquare
									&& game.squareAt(new Position(i, j)).isHidden()
									&& !game.squareAt(new Position(i, j)).isFlagged()) {
								game.squareAt(new Position(i, j)).setHidden(false);

							}
						}
						if (i == (x - 1) && j == (y - 1)) {
							if (game.squareAt(new Position(i, j)) instanceof BlankSquare
									&& game.squareAt(new Position(i, j)).isHidden()
									&& !game.squareAt(new Position(i, j)).isFlagged()) {
								game.squareAt(new Position(i, j)).setHidden(false);

							}
						}

					}
				}
			}

			square.setHidden(false); // now exposed

		}

		else {

			System.out.println("0000000000000000000");
			throw new SyntaxError("new");
		}
	}

	public String toString() {
		return "E" + position;
	}
}
