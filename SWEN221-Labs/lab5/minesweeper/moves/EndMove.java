package minesweeper.moves;

import minesweeper.*;
import minesweeper.squares.*;

/**
 * Represents a move which ends the game. If all blank squares are exposed, then
 * the player has one. Or, if a bomb is exposed, then the player has lost.
 * 
 * @author David J. Pearce
 * 
 */
public class EndMove extends ExposeMove {
	private boolean isWinner;

	/**
	 * Construct a EndGame object which represents a successful or unsuccessul
	 * outcome.
	 * 
	 * @param position
	 *            --- position to which this move applies.
	 * @param isWinner
	 *            --- true if the player has won the game; false if a bomb is
	 *            exposed.
	 */
	public EndMove(Position position, boolean isWinner) {
		super(position);
		this.isWinner = isWinner;
	}

	/**
	 * Apply this move to a given game and check that it is valid. An end move
	 * must either expose a bomb or expose the last remaining blank square.
	 * 
	 * @param game
	 *            --- game to which this move is applied.
	 */
	public void apply(Game game) throws SyntaxError {
		// TODO
		Square square = game.squareAt(position);

		if (!isWinner && square instanceof BombSquare) {
			// System.out.println("win");

			square.setHidden(false);
			// game.getStatus();
		} else if (isWinner && square instanceof BlankSquare && !square.isFlagged() && square.isHidden() && game.getF()==0) {
			System.out.println("winnnnnnnnnnnnnnnnnn");
			if (game.getStatus() == 0) {
				System.out.println(game.getStatus());
//				if(game.squareAt(new Position(1,0)) instanceof BombSquare && game.squareAt(new Position(1,0)).isFlagged()
//						&& game.squareAt(new Position(2,0)) instanceof BlankSquare &&
//						game.squareAt(new Position(2,0)).isHidden()){throw new SyntaxError("");}
				
				square.setHidden(false);
				
			} else {
				throw new SyntaxError("old");
			}
			//
		} else {

			throw new SyntaxError("new");
		}
	}

	// public boolean check(Game game) {
	// int count =0;
	// for (int i = 0; i < game.getHeight(); i++) {
	// for (int j = 0; j < game.getWidth(); j++) {
	// Square square = game.squareAt(new Position(i, j));
	// if (square instanceof BlankSquare && square.isHidden()) {
	// count++;
	// }
	// }
	// }
	// if(count == 1)return true;
	// return false;
	// }

	public String toString() {
		if (isWinner) {
			return "W" + position;
		} else {
			return "L" + position;
		}
	}
}
