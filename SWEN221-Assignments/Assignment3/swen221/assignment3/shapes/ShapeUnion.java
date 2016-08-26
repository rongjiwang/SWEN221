package swen221.assignment3.shapes;
/**
 * Draw two visible Rectangle 
 * @author Rongji Wang
 */
public class ShapeUnion extends ShapeOperator {

	public ShapeUnion(Shape s1, Shape s2) {
		super(s1, s2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean contains(int x, int y) {
		boolean diffArea = !s1.contains(x, y) && !s2.contains(x, y);
		return !diffArea;
	}
	
	@Override
	public Rectangle boundingBox() {
		int minX = Math.min(s1.boundingBox().getX(), s2.boundingBox().getX());
		int minY = Math.min(s1.boundingBox().getY(), s2.boundingBox().getY());

		int maxX = Math.max(s1.boundingBox().getX() + (s1.boundingBox().getWidth()),
				s2.boundingBox().getX() + (s2.boundingBox().getWidth()));
		int maxY = Math.max(s1.boundingBox().getY() + (s1.boundingBox().getHeight()),
				s2.boundingBox().getY() + (s2.boundingBox().getHeight()));

		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

}
