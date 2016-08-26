package swen221.assignment2.chessview;
import swen221.assignment2.chessview.moves.Move;
import swen221.assignment2.chessview.pieces.*;

public class Board {
	private Piece[][] pieces; // this is the underlying data structure for a
								// board.
	private static boolean whiteKingIsMoved;
	private static boolean blackKingIsMoved;
	private static boolean setRookWhiteLeft;

	private static boolean setRookWhiteRight;
	private static boolean setRookBlackLeft;
	private static boolean setRookBlackRight;
	private static boolean enPassantMove;



	/**
	 * Construct an initial board.
	 */
	public Board() {
		whiteKingIsMoved = false;
		blackKingIsMoved = false;
		setRookWhiteLeft = false;

		setRookWhiteRight = false;
		setRookBlackLeft = false;
		setRookBlackRight = false;
		enPassantMove = false;
		pieces = new Piece[9][9];

		for (int i = 1; i <= 8; ++i) {
			pieces[2][i] = new Pawn(true);
			pieces[7][i] = new Pawn(false);
		}

		// rooks
		pieces[1][1] = new Rook(true);
		pieces[1][8] = new Rook(true);
		pieces[8][1] = new Rook(false);
		pieces[8][8] = new Rook(false);

		// knights
		pieces[1][2] = new Knight(true);
		pieces[1][7] = new Knight(true);
		pieces[8][2] = new Knight(false);
		pieces[8][7] = new Knight(false);

		// bishops
		pieces[1][3] = new Bishop(true);
		pieces[1][6] = new Bishop(true);
		pieces[8][3] = new Bishop(false);
		pieces[8][6] = new Bishop(false);

		// king + queen
		pieces[1][4] = new Queen(true);
		pieces[1][5] = new King(true);
		pieces[8][4] = new Queen(false);
		pieces[8][5] = new King(false);
	}

	/**
	 * Construct a board which is identical to another board.
	 * 
	 * @param board
	 */
	public Board(Board board) {
		this.pieces = new Piece[9][9];
		for (int row = 1; row <= 8; ++row) {
			for (int col = 1; col <= 8; ++col) {
				this.pieces[row][col] = board.pieces[row][col];
			}
		}
	}

	/**
	 * Apply a given move to this board, returning true is successful, otherwise
	 * false.
	 * 
	 * @param move
	 * @return
	 */
	public boolean apply(Move move) {
		if (move.isValid(this)) {
			move.apply(this);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Move a piece from one position to another.
	 * 
	 * @param oldPosition
	 * @param newPosition
	 */
	public void move(Position oldPosition, Position newPosition) {
		Piece p = pieces[oldPosition.row()][oldPosition.column()];
		pieces[newPosition.row()][newPosition.column()] = p;
		pieces[oldPosition.row()][oldPosition.column()] = null;
	}

	public void setPieceAt(Position pos, Piece piece) {
		pieces[pos.row()][pos.column()] = piece;
	}

	public Piece pieceAt(Position pos) {
		return pieces[pos.row()][pos.column()];
	}

	public String toString() {
		String r = "";
		for (int row = 8; row != 0; row--) {
			r += row + "|";
			for (int col = 1; col <= 8; col++) {
				Piece p = pieces[row][col];
				if (p != null) {
					r += p + "|";
				} else {
					r += "_|";
				}
			}
			r += "\n";
		}
		return r + "  a b c d e f g h";
	}

	/**
	 * This method determines whether or not one side is in check.
	 * 
	 * @param isWhite
	 *            --- true means check whether white is in check; otherwise,
	 *            check black.
	 * @return
	 */
	public boolean isInCheck(boolean isWhite) {
		King king = null; // opposition king
		Position kingPos = null;

		// First, find my king
		outer: for (int row = 1; row <= 8; ++row) {
			for (int col = 1; col <= 8; ++col) {
				Position pos = new Position(row, col);
				Piece p = pieceAt(pos);
				if (p instanceof King && p.isWhite() == isWhite) {
					// found him.
					king = (King) p;
					kingPos = pos;
					// The following will break out of the entire loop, not
					// just the innermost loop. This isn't exactly great
					// style, but it is pretty convenient here.
					break outer;
				}
			}
		}

		// Second, check opposition pieces to see whether they can take
		// my king or not. If one can, we're in check!
		for (int row = 1; row <= 8; ++row) {
			for (int col = 1; col <= 8; ++col) {
				Position pos = new Position(row, col);
				Piece p = pieceAt(pos);
				// If this is an opposition piece, and it can take my king,
				// then we're definitely in check.
				if (p != null && p.isWhite() != isWhite && p.isValidMove(pos, kingPos, king, this)) {
					// System.out.println("5");
					// p can take opposition king, so we're in check.
					return true;
				}
			}
		}

		// couldn't find any piece in check.
		return false;
	}

	/**
	 * The following method checks whether the given diaganol is completely
	 * clear, except for a given set of pieces. Observe that this doesn't
	 * guarantee a given diaganol move is valid, since this method does not
	 * ensure anything about the relative positions of the given pieces.
	 * 
	 * @param startPosition
	 *            - start of diaganol
	 * @param endPosition
	 *            - end of diaganol
	 * @param exceptions
	 *            - the list of pieces allowed on the diaganol
	 * @return
	 */
	public boolean clearDiaganolExcept(Position startPosition, Position endPosition, Piece... exceptions) {
		int startCol = startPosition.column();
		int endCol = endPosition.column();
		int startRow = startPosition.row();
		int endRow = endPosition.row();
		int diffCol = Math.max(startCol, endCol) - Math.min(startCol, endCol);
		int diffRow = Math.max(startRow, endRow) - Math.min(startRow, endRow);

		if (diffCol != diffRow && diffCol == 0) {
			return false;
		}

		int row = startRow;
		int col = startCol;
		while (row != endRow && col != endCol) {
			Piece p = pieces[row][col];
			if (p != null && !contains(p, exceptions)) {
				return false;
			}
			col = col <= endCol ? col + 1 : col - 1;
			row = row <= endRow ? row + 1 : row - 1;
		}

		return true;
	}

	/**
	 * The following method checks whether the given column is completely clear,
	 * except for a given set of pieces. Observe that this doesn't guarantee a
	 * given column move is valid, since this method does not ensure anything
	 * about the relative positions of the given pieces.
	 * 
	 * @param startPosition
	 *            - start of column
	 * @param endPosition
	 *            - end of column
	 * @param exceptions
	 *            - the list of pieces allowed on the column
	 * @return
	 */
	public boolean clearColumnExcept(Position startPosition, Position endPosition, Piece... exceptions) {
		int minCol = Math.min(startPosition.column(), endPosition.column());
		int maxCol = Math.max(startPosition.column(), endPosition.column());
		int minRow = Math.min(startPosition.row(), endPosition.row());
		int maxRow = Math.max(startPosition.row(), endPosition.row());
		int diffCol = maxCol - minCol;
		int diffRow = maxRow - minRow;

		if (diffCol == 0 || diffRow != 0) {
			return false;
		}

		int col = minCol;
		while (col <= maxCol) {
			Piece p = pieces[minRow][col];
			if (p != null && !contains(p, exceptions)) {
				return false;
			}
			col++;
		}

		return true;
	}

	/**
	 * The following method checks whether the given row is completely clear,
	 * except for a given set of pieces. Observe that this doesn't guarantee a
	 * given row move is valid, since this method does not ensure anything about
	 * the relative positions of the given pieces.
	 * 
	 * @param startPosition
	 *            - start of row
	 * @param endPosition
	 *            - end of row
	 * @param exceptions
	 *            - the list of pieces allowed on the row
	 * @return
	 */
	public boolean clearRowExcept(Position startPosition, Position endPosition, Piece... exceptions) {
		int minCol = Math.min(startPosition.column(), endPosition.column());
		int maxCol = Math.max(startPosition.column(), endPosition.column());
		int minRow = Math.min(startPosition.row(), endPosition.row());
		int maxRow = Math.max(startPosition.row(), endPosition.row());
		int diffCol = maxCol - minCol;
		int diffRow = maxRow - minRow;

		if (diffCol != 0 || diffRow == 0) {
			return false;
		}

		int row = minRow;
		while (row <= maxRow) {
			Piece p = pieces[row][minCol];
			if (p != null && !contains(p, exceptions)) {
				return false;
			}
			row++;
		}

		return true;
	}

	// Helper method for the clear?????Except methods above.
	private static boolean contains(Piece p1, Piece... pieces) {
		for (Piece p2 : pieces) {
			if (p1 == p2) {
				return true;
			}
		}

		return false;
	}
	/**
	 * 
	 * @param boolean
	 */
	public void whiteKingIsMoved(boolean b) {
		this.whiteKingIsMoved = b;
	}
	/**
	 * 
	 * @param boolean
	 */
	public void blackKingIsMoved(boolean b) {
		this.blackKingIsMoved = b;

	}
	/**
	 * 
	 * @param boolean
	 */
	public void setRookWhiteLeft(boolean b) {
		this.setRookWhiteLeft = b;
	}
	/**
	 * 
	 * @param boolean
	 */
	public void setRookWhiteRight(boolean b) {
		this.setRookWhiteRight = b;

	}
	/**
	 * 
	 * @param boolean
	 */
	public void setRookBlackLeft(boolean b) {
		this.setRookBlackLeft = b;

	}
	/**
	 * 
	 * @param boolean
	 */
	public void setRookBlackRight(boolean b) {
		this.setRookBlackRight = b;

	}
	/**
	 * 
	 * @return  true white left rook has been moved
	 */
	public boolean isSetRookWhiteLeft() {
		return setRookWhiteLeft;
	}
	/**
	 * 
	 * @return true white right rook has been moved
	 */
	public boolean isSetRookWhiteRight() {
		return setRookWhiteRight;
	}
	/**
	 * 
	 * @return true black left rook has been moved
	 */
	public boolean isSetRookBlackLeft() {
		return setRookBlackLeft;
	}
	/**
	 * 
	 * @return true black right rook has been moved
	 */
	public boolean isSetRookBlackRight() {
		return setRookBlackRight;
	}
	/**
	 * 
	 * @return true white king rook has been moved
	 */
	public boolean isWhiteKingIsMoved() {
		return whiteKingIsMoved;
	}
	/**
	 * 
	 * @return true black king has been moved
	 */
	public boolean isBlackKingIsMoved() {
		return blackKingIsMoved;
	}
	/**
	 * 
	 * @return true now its the only chance that you can use en passant
	 */
	public static boolean isEnPassantMove() {
		return enPassantMove;
	}
	/**
	 * 
	 * @param enPassantMove true or false
	 */
	public static void setEnPassantMove(boolean enPassantMove) {
		Board.enPassantMove = enPassantMove;
	}
	
}
