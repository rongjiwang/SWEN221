package swen221.assignment2.chessview.pieces;

import swen221.assignment2.chessview.*;

public class Pawn extends PieceImpl implements Piece {

	private boolean pawnHasTwoSteps = false;

	public Pawn(boolean isWhite) {
		super(isWhite);
	}

	/**
	 * extend method over PieceImpl takes 2 params
	 * 
	 * @param isWhite
	 *            color of Pawn
	 * @param hasTwoSteps
	 *            used double move or not
	 */
	public Pawn(boolean isWhite, boolean hasTwoSteps) {
		super(isWhite);
		pawnHasTwoSteps = true;
	}

	/**
	 * check all the valid or invalid move for Pawn
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

		if (this.equals(p)) {
			// taken codition
			if ((t != null && isTaken != null && isTaken.equals(t))) {

				if ((diffCol == 1 && diffRow == 1) && (this.isWhite != t.isWhite())
						&& (((oldRow + dir) == newRow && (oldCol + dir) == newCol)
								|| ((oldRow + dir) == newRow && (oldCol - dir) == newCol))) {
					return true;
				}
				return false;
			}
			// 1 step codition
			else {
				if ((oldRow + dir) == newRow && oldCol == newCol && t == null) {
					return true;
				}
				// 2 step codition
				else if ((oldRow + dir + dir) == newRow && oldCol == newCol

						&& ((dir == 1 && oldRow == 2) || (dir == -1 && oldRow == 7))
						&& board.pieceAt(new Position((oldRow + dir), oldCol)) == null && t == null) {
					return true;
				}
				return false;
			}
		}
		return false;
	}

	/**
	 * 
	 * @return true pawn did double move
	 */
	public boolean isPawnHasTwoSteps() {
		return this.pawnHasTwoSteps;
	}

	/**
	 * 
	 * @param twoSteps
	 *            boolean 2 steps have been active or not
	 */
	public void setPawnHasTwoSteps(boolean twoSteps) {
		this.pawnHasTwoSteps = twoSteps;
	}

	public String toString() {
		if (isWhite) {
			return "P";
		} else {
			return "p";
		}
	}
}
