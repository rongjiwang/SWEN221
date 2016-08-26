package swen221.lab2.rules;

import swen221.lab2.model.*;
import swen221.lab2.util.ConwayAbstractRule;

/**
 * This is a extend class for extending a rule over the AbstractRule class
 * 
 * @author wangrong
 * @version 1.0
 */
public class ConwaysOverpopulationRule extends ConwayAbstractRule {

	public int apply(int x, int y, int neighbours) {
		/**
		 * @return dead if neighbours more than 3
		 * @return rule not apply if neighbours less than 3
		 */
		if (neighbours > 3) {
			// This rule was applied in this case
			return ConwayAbstractRule.DEAD;
		} else {
			// This rule was not applied in this case
			return Rule.NOT_APPLICABLE;
		}
	}
}
