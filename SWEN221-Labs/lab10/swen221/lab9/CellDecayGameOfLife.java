package swen221.lab9;

import swen221.lab9.model.*;
import swen221.lab9.util.Pair;
import swen221.lab9.util.Point;
import swen221.lab9.view.BoardFrame;

import static swen221.lab9.GameOfLife.*;

public class CellDecayGameOfLife {
	public final static int ALIVE = 0;
	public final static int DEAD = 9;

	/**
	 * The standard rule set for Conway's "Game of Life".
	 */
	public static final Rule[] CellDecayRules = {
			// TODO: The underproduction rule
			(Pair<Point, Board> p) -> neighbours(p) < 2 ? helpPlus(p) : null,
			// TODO: The reproduction rule
			(Pair<Point, Board> p) -> neighbours(p) == 3 ? helpMinus(p) : null,
			// TODO: The overpopulation rule
			(Pair<Point, Board> p) -> neighbours(p) > 3 ? helpPlus(p) : null,

	};

	public static int helpPlus(Pair<Point, Board> p) {
		int i = p.second().getCellState(p.first().getX(), p.first().getY());
		i++;
		if (i >= 9) {
			return i = 9;
		}
		return i;
	}

	public static int helpMinus(Pair<Point, Board> p) {
		int i = p.second().getCellState(p.first().getX(), p.first().getY());
		i--;
		if (i <= 0)
			return i = 0;
		return i;
	}

	public static int neighbours(Pair<Point, Board> pair) {
		Point p = pair.first();
		Board board = pair.second();
		int count = 0;

		for (int dx = -1; dx <= 1; ++dx) {
			for (int dy = -1; dy <= 1; ++dy) {
				if (dx != 0 || dy != 0) {
					count += getNumAlive(p.getX() + dx, p.getY() + dy, board);
				}
			}
		}

		return count;
	}

	private static int getNumAlive(int x, int y, BoardView board) {
		if (x < 0 || x >= board.getWidth()) {
			return 0;
		} else if (y < 0 || y >= board.getWidth()) {
			return 0;
		} else if (board.getCellState(x, y) == DEAD) {
			return 0;
		} else {
			return 1;
		}
	}

	/**
	 * The entry point for the GameOfLife application.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		Board board = new Board(50, 50);
		Simulation sim = new Simulation(board, CellDecayRules);
		new BoardFrame(sim);
	}
}
