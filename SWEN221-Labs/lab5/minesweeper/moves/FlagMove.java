package minesweeper.moves;

import minesweeper.*;
import minesweeper.squares.Square;

/**
 * Represents a move which either flags or unflags a square on the board.
 * 
 * @author David J. Pearce
 * 
 */
public class FlagMove extends Move {

	private boolean isFlagged;

	/**
	 * Construct a FlagSquare move which either flags or unflags a given
	 * position.
	 * 
	 * @param position
	 *            --- position to be flagged/unflagged.
	 * @param isFlagged
	 *            --- true if position is being flagged; false if it is being
	 *            unflagged.
	 */
	public FlagMove(Position position, boolean isFlagged) {
		super(position);
		this.isFlagged = isFlagged;
	}

	/**
	 * Apply this move to a given game and check that it is valid. A square can
	 * only be flagged if it is currently unflagged and vice versa.
	 * 
	 * @param game
	 *            --- game to which this move is applied.
	 */
	public void apply(Game game) throws SyntaxError {
		Square square = game.squareAt(position);

		if (!square.isFlagged() && square.isHidden() && isFlagged) {
			square.setFlagged(true);
			
		} 
		else if(square.isFlagged() && square.isHidden() && !isFlagged){
			square.setFlagged(false);
		}
		else {
			throw new SyntaxError("new");
		}

	}

	public String toString() {
		if (isFlagged) {
			return "F" + position;
		} else {
			return "U" + position;
		}
	}
}
