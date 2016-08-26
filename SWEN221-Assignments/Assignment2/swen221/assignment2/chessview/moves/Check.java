package swen221.assignment2.chessview.moves;

import swen221.assignment2.chessview.*;
import swen221.assignment2.chessview.pieces.*;

/**
 * This represents a "check move". Note that, a check move can only be made up
 * from an underlying simple move; that is, we can't check a check move.
 * 
 * @author djp
 * 
 */
public class Check implements Move {
	private MultiPieceMove move;
	private boolean isWhite;

	public Check(MultiPieceMove move) {
		this.move = move;
		this.isWhite = move.isWhite();

	}

	public MultiPieceMove move() {
		return move;
	}

	public boolean isWhite() {
		return move.isWhite();
	}

	/**
	 * next move's king can be checked or not
	 * 
	 * @return true is valid or otherwise
	 * @param board
	 */
	public boolean isValid(Board board) {
		System.out.println("1");
		Board testMove = new Board(board);
		System.out.println("12");

		testMove.apply(move);

		if (move.isValid(board) && testMove.isInCheck(!isWhite)) {
			System.out.println("6");

			return true;
		}
		return false;
	}

	/**
	 * action move
	 */
	public void apply(Board board) {
		move.apply(board);
	}

	public String toString() {
		return move.toString() + "+";
	}
}
