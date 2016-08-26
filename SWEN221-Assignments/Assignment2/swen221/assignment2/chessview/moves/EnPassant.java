package swen221.assignment2.chessview.moves;

import swen221.assignment2.chessview.*;
import swen221.assignment2.chessview.pieces.*;

/**
 * This represents an "en passant move" ---
 * http://en.wikipedia.org/wiki/En_passant.
 * 
 * @author djp
 * 
 */
public class EnPassant implements MultiPieceMove {

	private SinglePieceMove move;
	private boolean isWhite;

	public EnPassant(SinglePieceMove move) {
		this.move = move;
		this.isWhite = move.isWhite();
	}

	public boolean isWhite() {

		return isWhite;
	}

	/**
	 * 
	 * check the condition for a valid move of en passant
	 * 
	 * @return true en passant move follows the rule
	 */
	public boolean isValid(Board b) {
		// if(!b.isEnPassantMove()){return false;}
		Position newP = getEnPassantP();
		if (!(b.pieceAt(newP) instanceof Pawn)) {
			return false;
		} else if (((Pawn) b.pieceAt(newP)).isPawnHasTwoSteps() == false) {
			return false;
		}

		return true;
	}

	/**
	 * move the pawn to en passant position and set regular position as null
	 */
	public void apply(Board board) {

		Position newP = getEnPassantP();
		board.setPieceAt(newP, null);
		move.apply(board);
	}

	/**
	 * on the top of regular move, find out where is the en passant target and
	 * return the location of the new position after en passant move
	 * 
	 * @return Position en passant position
	 */
	public Position getEnPassantP() {
		Position regularNextMove = move.newPosition(); // next position
		Position newP = null;
		if (isWhite) {
			newP = new Position(regularNextMove.row() - 1, regularNextMove.column());
		} else {
			newP = new Position(regularNextMove.row() + 1, regularNextMove.column());
		}

		return newP;
	}

	public String toString() {
		return "ep";
	}
}
