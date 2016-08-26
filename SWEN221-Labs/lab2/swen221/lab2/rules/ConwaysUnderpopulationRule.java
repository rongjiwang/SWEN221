package swen221.lab2.rules;

import swen221.lab2.model.Rule;
import swen221.lab2.util.ConwayAbstractRule;

/**
 * This is a extend class for extending a rule over the AbstractRule class
 * 
 * @author wangrong
 * @version 1.0
 */
public class ConwaysUnderpopulationRule extends ConwayAbstractRule {
	/**
	 * @return dead if neighbours less than 2
	 * @return rule not apply if neighbours over than 2
	 */
	public int apply(int x, int y, int neighbours) {
		if (neighbours < 2) {
			// This rule was applied in this case
			return ConwayAbstractRule.DEAD;
		} else {
			// This rule was not applied in this case
			return Rule.NOT_APPLICABLE;
		}
	}
}
