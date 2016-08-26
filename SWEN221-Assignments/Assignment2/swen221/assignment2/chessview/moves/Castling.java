package swen221.assignment2.chessview.moves;

import swen221.assignment2.chessview.*;
import swen221.assignment2.chessview.pieces.*;

public class Castling implements MultiPieceMove {
	private boolean kingSide;
	private boolean isWhite;
	private Position rookPosition;
	private Position castlingKing;
	private Position castlingRook;
	private Position kingPosition;
	private King currentKing;
	private Rook currentRook;

	/**
	 * 
	 * @param isWhite,
	 *            white or black
	 * @param kingSide,
	 *            kingSide rook or queen side rook
	 */
	public Castling(boolean isWhite, boolean kingSide) {
		this.kingSide = kingSide;
		this.isWhite = isWhite;

		// get start position for Kings and Rooks
		if (isWhite) {
			kingPosition = new Position(1, 5);
			if (kingSide) {
				rookPosition = new Position(1, 8);
				castlingKing = new Position(1, 7);
				castlingRook = new Position(1, 6);
			} else {
				rookPosition = new Position(1, 1);
				castlingKing = new Position(1, 3);
				castlingRook = new Position(1, 4);
			}
		} else {
			kingPosition = new Position(8, 5);
			if (kingSide) {
				rookPosition = new Position(8, 8);
				castlingKing = new Position(8, 7);
				castlingRook = new Position(8, 6);
			} else {
				rookPosition = new Position(8, 1);
				castlingKing = new Position(8, 3);
				castlingRook = new Position(8, 4);
			}
		}

	}

	public boolean isWhite() {
		return false;
	}

	/**
	 * iff isValid return true, this method will become active
	 */
	public void apply(Board b) {
		b.move(kingPosition, castlingKing);
		b.move(rookPosition, castlingRook);
	}

	/**
	 * rules for castling to work
	 * 
	 * @param b
	 *            board
	 * @return true is valid or false is not valid
	 */
	public boolean isValid(Board b) {
		// get the boolean value from board class
		boolean whiteKing = b.isWhiteKingIsMoved();
		boolean whiteLRook = b.isSetRookWhiteLeft();
		boolean whiteRRook = b.isSetRookWhiteRight();

		boolean blackKing = b.isBlackKingIsMoved();
		boolean blackLRook = b.isSetRookBlackLeft();
		boolean blackRRook = b.isSetRookBlackRight();

		// make sure the positions are right, both king and rook
		if (b.pieceAt(kingPosition) instanceof King && b.pieceAt(rookPosition) instanceof Rook) {
			this.currentKing = (King) b.pieceAt(kingPosition);
			this.currentRook = (Rook) b.pieceAt(rookPosition);
		}
		boolean ans = pathOnCheck(kingPosition, castlingKing, kingSide, b);
		boolean kingCheck = ((this.isWhite && b.isInCheck(this.isWhite))
				|| (!this.isWhite && b.isInCheck(!this.isWhite)));
		// make sure either side of king not beening checked
		if (ans && !kingCheck) {
			// return true if the choosen pieces have been moved at all
			if (this.isWhite && (whiteKing != true)) {
				if (this.kingSide) {

					return !whiteRRook;
				} else {
					return !whiteLRook;
				}
			} else if (!this.isWhite && (blackKing != true)) {
				if (this.kingSide) {
					return !blackRRook;
				} else {
					return !blackLRook;
				}
			}

			return false;
		}

		return false;
	}

	// queen 1,4 8,5 king 1,5 8,5
	/**
	 * 
	 * @param from
	 * @param to
	 * @param kingSide
	 * @param bboard
	 * @return true, between king and rook are clear, and king and next 2
	 *         position not been checked
	 */
	public boolean pathOnCheck(Position from, Position to, boolean kingSide, Board b) {
		int kingCol = from.column();
		int kingCastlingCol = to.column();

		if (kingSide) {

			while (kingCol <= kingCastlingCol) {
				// king and next 2 position not being checked
				if (this.currentKing.isOnCheck(new Position(from.row(), kingCol), b)) {
					return false;
				}
				kingCol += 1;
				// path clear between king and rook
				if (b.pieceAt(new Position(from.row(), kingCol)) != null && kingCol <= kingCastlingCol) {
					return false;
				}

			}
		} else {

			while (kingCol >= kingCastlingCol) {
				if (this.currentKing.isOnCheck(new Position(from.row(), kingCol), b)) {
					return false;
				}
				kingCol -= 1;
				if (b.pieceAt(new Position(from.row(), kingCol)) != null) {
					return false;
				}
			}
		}
		return true;
	}

	public String toString() {
		if (kingSide) {
			return "O-O";
		} else {
			return "O-O-O";
		}
	}
}
