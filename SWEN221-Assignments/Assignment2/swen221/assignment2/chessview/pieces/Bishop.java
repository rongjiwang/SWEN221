package swen221.assignment2.chessview.pieces;

import swen221.assignment2.chessview.*;

public class Bishop extends PieceImpl implements Piece {
	public Bishop(boolean isWhite) {
		super(isWhite);
	}

	/**
	 * check all the valid or invalid move for Bishop
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

		int dir = isWhite ? 1 : -1;

		int oldRow = oldPosition.row();
		int oldCol = oldPosition.column();
		int newRow = newPosition.row();
		int newCol = newPosition.column();

		int diffCol = Math.max(oldPosition.column(), newPosition.column())
				- Math.min(oldPosition.column(), newPosition.column());
		int diffRow = Math.max(oldPosition.row(), newPosition.row()) - Math.min(oldPosition.row(), newPosition.row());

		Piece p = board.pieceAt(oldPosition);
		Piece t = board.pieceAt(newPosition);
		// taken condition
		if (this.equals(p) && (diffCol == diffRow) && board.clearDiaganolExcept(oldPosition, newPosition, p, t)) {
			if (isTaken != null && t != null && isTaken.equals(t) && this.isWhite != t.isWhite()) {
				return true;
			}
			// move condition
			else if (t == null && isTaken == null) {

				return true;
			}
			return false;
		}
		return false;
	}

	public String toString() {
		if (isWhite) {
			return "B";
		} else {
			return "b";
		}
	}
}
