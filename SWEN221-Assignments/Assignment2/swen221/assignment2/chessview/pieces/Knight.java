package swen221.assignment2.chessview.pieces;

import swen221.assignment2.chessview.*;

public class Knight extends PieceImpl implements Piece {
	public Knight(boolean isWhite) {
		super(isWhite);
	}

	/**
	 * check all the valid or invalid move for Knight
	 * 
	 * @param oldPosition
	 *            old location
	 * @param newPosition
	 *            new location
	 * @param isTaken
	 *            simple move or take a piece down
	 * @param board
	 * @return true meet all the condition
	 * 
	 * 
	 */
	public boolean isValidMove(Position oldPosition, Position newPosition, Piece isTaken, Board board) {
		int diffCol = Math.max(oldPosition.column(), newPosition.column())
				- Math.min(oldPosition.column(), newPosition.column());
		int diffRow = Math.max(oldPosition.row(), newPosition.row()) - Math.min(oldPosition.row(), newPosition.row());
		Piece p = board.pieceAt(oldPosition);
		Piece t = board.pieceAt(newPosition);
		// taken codition
		if (this.equals(p) && (diffRow == 2 && diffCol == 1 || diffRow == 1 && diffCol == 2)) {
			if (isTaken != null && t != null) {

				if (isTaken.equals(t) && (this.isWhite != t.isWhite()) && t != null) {

					return true;
				}

				return false;
				// move codition
			} else if (t == null && isTaken == null) {

				return true;
			}

			return false;
		}

		return false;

	}

	public String toString() {
		if (isWhite) {
			return "N";
		} else {
			return "n";
		}
	}
}
