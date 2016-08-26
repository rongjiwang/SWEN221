package swen221.lab6.connect.rules;

import swen221.lab6.connect.Game;
import swen221.lab6.connect.Game.Status;
import swen221.lab6.connect.core.Board;
import swen221.lab6.connect.core.Rule;
import swen221.lab6.connect.util.Position;

/**
 * Checks whether or not a stale mate has been reached.
 *
 * @author David J. Pearce
 *
 */
public class StaleMateRule implements Rule {

	@Override
	public Status apply(Game g) {
		// Here, we need to check how many tokens have been played so far. Since
		// each player starts with exactly eight tokens, there can be at most
		// eight tokens played by each player. After that point, we have reached
		// a stalemate. When this happens, we need to return the appropriate
		// status signal. And, yes, it is possible to reach stalemate.
		if(g.getMovesPlayed() == 16){return Status.STALEMATE;}
		Status r = null;
		for(int x=0;x<4;x++){
			for(int y=0;y<4;y++){
				Board b = g.getBoard();
				if(b.getSquare(new Position(x,y)) == null){return Status.ONGOING;}
				
			}
		}
		return Status.STALEMATE;
	}
}
