package swen221.assignment2.chessview.moves;

import swen221.assignment2.chessview.*;
import swen221.assignment2.chessview.pieces.King;
import swen221.assignment2.chessview.pieces.Piece;
import swen221.assignment2.chessview.pieces.Rook;

public class SinglePieceTake extends SinglePieceMove {
	private Piece isTaken;

	public SinglePieceTake(Piece piece, Piece isTaken, Position oldPosition, Position newPosition) {
		super(piece, oldPosition, newPosition);
		this.isTaken = isTaken;
	}

	/**
	 * @return true meet the rules
	 */
	public boolean isValid(Board board) {
		if (piece.isValidMove(oldPosition, newPosition, isTaken, board)){
			System.out.println(oldPosition+" "+newPosition+" "+isTaken+" ");

			System.out.println("4");

			return true;//
		}
		return false;

	}

	public String toString() {
		return pieceChar(piece) + oldPosition + "x" + pieceChar(isTaken) + newPosition;
	}
}
