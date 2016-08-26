package swen221.assignment3.shapes;

/**
 * Abstract class for ShapeDifference, ShapeIntersection and ShapeUnion class to
 * extends Shape class(override methods)
 * 
 * @author Rongji Wang
 */
public abstract class ShapeOperator implements Shape {
	protected Shape s1;
	protected Shape s2;

	public ShapeOperator(Shape s1, Shape s2) {
		this.s1 = s1;
		this.s2 = s2;
	}
}
