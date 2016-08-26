package swen221.assignment2.chessview.moves;

import java.util.*;

import swen221.assignment2.chessview.*;
import swen221.assignment2.chessview.pieces.*;

public class SinglePieceMove implements MultiPieceMove {
	protected Piece piece;
	protected Position oldPosition;
	protected Position newPosition;

	public SinglePieceMove(Piece piece, Position oldPosition, Position newPosition) {
		this.piece = piece;
		this.oldPosition = oldPosition;
		this.newPosition = newPosition;
	}

	public Piece piece() {
		return piece;
	}

	public boolean isWhite() {
		return piece.isWhite();
	}

	public Position oldPosition() {
		return oldPosition;
	}

	public Position newPosition() {
		return newPosition;
	}

	/**
	 * {@link #apply(Board)} to move a piece
	 * 
	 * @return true meet the rules
	 */
	public boolean isValid(Board board) {
		if (piece.isValidMove(oldPosition, newPosition, null, board)) {
			System.out.println("3");
			return true;
			}
		return false;
	}

	/**
	 * {@link #isValid(Board)} move a piece set conditions for enPassant and
	 * castling
	 * 
	 * @param board
	 */
	public void apply(Board b) {
		// b.setEnPassantMove(false);
		if (piece instanceof King) {
			if (piece.isWhite()) {
				b.whiteKingIsMoved(true);
			} else {
				b.blackKingIsMoved(true);
			}
		} else if (piece instanceof Rook) {
			if (piece.isWhite()) {
				if ((oldPosition.row() == 1) && (oldPosition.column() == 1)) {

					b.setRookWhiteLeft(true);
				} else if ((oldPosition.row() == 1) && (oldPosition.column() == 8)) {

					b.setRookWhiteRight(true);
				}

			} else {

				if (oldPosition.row() == 8 && oldPosition.column() == 1) {
					b.setRookBlackLeft(true);
				} else if (oldPosition.row() == 8 && oldPosition.column() == 8) {
					b.setRookBlackRight(true);
				}
			}
		}
		if (piece instanceof Pawn) {
			int steps = Math.abs(oldPosition.row() - newPosition.row());
			if (steps == 2) {
				((Pawn) b.pieceAt(oldPosition)).setPawnHasTwoSteps(true);
				b.setEnPassantMove(true);
			} else {
				((Pawn) b.pieceAt(oldPosition)).setPawnHasTwoSteps(false);
			}
		}

		b.move(oldPosition, newPosition);
	}

	public String toString() {
		return pieceChar(piece) + oldPosition + "-" + newPosition;
	}

	protected static String pieceChar(Piece p) {
		if (p instanceof Pawn) {
			return "";
		} else if (p instanceof Knight) {
			return "N";
		} else if (p instanceof Bishop) {
			return "B";
		} else if (p instanceof Rook) {
			return "R";
		} else if (p instanceof Queen) {
			return "Q";
		} else {
			return "K";
		}
	}
}
