package swen221.assignment3.shapes;

/**
 * Rectangle class
 * 
 * @author Rongji Wang
 *
 */
public class Rectangle implements Shape {
	private int y;
	private int x;
	private int height;
	private int width;

	/**
	 * Construct a Rectangle
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * 
	 */
	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	/*
	 * 
	 * check x and y both with in the box range
	 */
	@Override
	public boolean contains(int x, int y) {
		int minX = this.x;
		int minY = this.y;
		int maxX = minX + this.width;
		int maxY = minY + this.height;
		if (x < minX || y < minY || x >= maxX || y >= maxY) {
			return false;
		}
		return true;
	}

	@Override
	public Rectangle boundingBox() {
		return this;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

}
