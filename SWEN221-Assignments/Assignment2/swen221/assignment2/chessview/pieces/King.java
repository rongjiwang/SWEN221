package swen221.assignment2.chessview.pieces;

import swen221.assignment2.chessview.*;

public class King extends PieceImpl implements Piece {
	private int oldRow;
	private int oldCol;
	private int newRow;
	private int newCol;

	public King(boolean isWhite) {
		super(isWhite);
	}

	/**
	 * check all the valid or invalid move for King
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

		oldRow = oldPosition.row();
		oldCol = oldPosition.column();
		newRow = newPosition.row();
		newCol = newPosition.column();

		int diffCol = Math.max(oldPosition.column(), newPosition.column())
				- Math.min(oldPosition.column(), newPosition.column());
		int diffRow = Math.max(oldPosition.row(), newPosition.row()) - Math.min(oldPosition.row(), newPosition.row());
		Piece p = board.pieceAt(oldPosition);
		Piece t = board.pieceAt(newPosition);
		// taken codition
		if (this.equals(p) && (diffCol == 1 || diffRow == 1 || (diffRow == 1 && diffCol == 1)) && diffCol <= 1
				&& diffRow <= 1) {
			if (isTaken != null && t != null) {
				if (isTaken.equals(t) && (this.isWhite != t.isWhite())) {
					return true;
				}
				return false;
				// move condition
			} else if (t == null && isTaken == null) {
				return true;
			}

			return false;
		}

		return false;
	}

	/**
	 * check king is in check or not
	 * 
	 * @param tempPosition
	 *            king's position
	 * @param b
	 *            board
	 * @return true king is in check
	 */
	public boolean isOnCheck(Position tempPosition, Board b) {
		Position kingPos = tempPosition;
		for (int row = 1; row <= 8; ++row) {
			for (int col = 1; col <= 8; ++col) {
				Position pos = new Position(row, col);
				Piece p = b.pieceAt(pos);
				if (p != null && p.isWhite() != this.isWhite && p.isValidMove(pos, kingPos, p, b)) {
					System.out.println("2");
					return true;
				}
			}
		}
		return false;
	}

	public int getOldRow() {
		return oldRow;
	}

	public int getOldCol() {
		return oldCol;
	}

	public int getNewRow() {
		return newRow;
	}

	public int getNewCol() {
		return newCol;
	}

	public String toString() {
		if (isWhite) {
			return "K";
		} else {
			return "k";
		}
	}
}
