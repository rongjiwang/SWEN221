package swen221.assignment2.chessview.moves;

import swen221.assignment2.chessview.*;
import swen221.assignment2.chessview.pieces.*;

/**
 * This represents a pawn promotion.
 * 
 * @author djp
 *
 */
public class PawnPromotion implements MultiPieceMove {
	private Piece promotion;
	private SinglePieceMove move;

	public PawnPromotion(SinglePieceMove move, Piece promotion) {
		this.move = move;
		this.promotion = promotion;
	}

	public boolean isWhite() {
		return promotion.isWhite();
	}

	/**
	 * return true if next pawn move can reach the end of other side of board
	 * 
	 * @return true Pawn meet the rules
	 */
	public boolean isValid(Board b) {
		Position from = move.oldPosition();
		Position to = move.newPosition();

		if (b.pieceAt(from) instanceof Pawn) {
			if (this.isWhite() && to.row() == 8) {
				return true;
			} else if (!this.isWhite() && to.row() == 1) {
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Use {@link #isValid(Board)} to move a piece act the move and and promote
	 * the piece to something else
	 */
	public void apply(Board b) {
		System.out.println("10");

		b.apply(move);
		b.setPieceAt(move.newPosition(), promotion);
	}

	public String toString() {
		return super.toString() + "=" + SinglePieceMove.pieceChar(promotion);
	}
}
