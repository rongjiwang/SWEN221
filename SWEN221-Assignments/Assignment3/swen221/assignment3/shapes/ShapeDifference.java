package swen221.assignment3.shapes;

public class ShapeDifference extends ShapeOperator {

	public ShapeDifference(Shape s1, Shape s2) {
		super(s1, s2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean contains(int x, int y) {
		// TODO Auto-generated method stub
		return ((s1.contains(x, y) && !s2.contains(x, y)) || (!s1.contains(x, y) && s2.contains(x, y)));
	}

	@Override
	public Rectangle boundingBox() {
		// TODO Auto-generated method stub
		
		Rectangle r = new Rectangle(s1.boundingBox().getX(), s1.boundingBox().getY(), s1.boundingBox().getWidth(),
				s1.boundingBox().getHeight());
		return r;
	}

}
