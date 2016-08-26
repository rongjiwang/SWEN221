package swen221.lab2.rules;

import swen221.lab2.model.BoardView;
import swen221.lab2.model.Rule;
import swen221.lab2.util.ConwayAbstractRule2;

public class Happiness extends ConwayAbstractRule2{

	public int apply(int x, int y, int neighbours,BoardView board){

		if(neighbours == 3){

		return board.getCellState(x,y)-1;
		}
		else{
			return Rule.NOT_APPLICABLE;
		}

	}
}
