package swen221.assignment3.shapes;

public class ShapeIntersection extends ShapeOperator {
	/**
	 * ShapeIntersection class will be able to find overlapping area from two
	 * shapes
	 * 
	 * @param s1
	 *            , s2
	 * @author Rongji Wang
	 */
	public ShapeIntersection(Shape s1, Shape s2) {
		super(s1, s2);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean contains(int x, int y) {
		// TODO Auto-generated method stub
		return (s1.contains(x, y) && s2.contains(x, y));
	}

	@Override
	public Rectangle boundingBox() {
		int minX = -1;
		int minY = -1;
		int maxX = 0;
		int maxY = 0;
		int totalWidth = s1.boundingBox().getWidth() + s2.boundingBox().getWidth();
		int totalHeight = s1.boundingBox().getHeight() + s2.boundingBox().getHeight();
		
		for (int i = 0; i < totalWidth; i++) {
			for (int j = 0; j < totalHeight; j++) {
				if (s1.contains(i, j) && s2.contains(i, j)) {
					if (minX == -1 && minY == -1) { // update overlap area
													// drawing position
						minX = i;
						minY = j;
					} else {
						maxX = i; // update overlap area width and height
						maxY = j;
					}
				}
			}
		}
		Rectangle r = new Rectangle(minX, minY, (maxX - minX + 1), (maxY - minY + 1));
		return r;
	}

}
