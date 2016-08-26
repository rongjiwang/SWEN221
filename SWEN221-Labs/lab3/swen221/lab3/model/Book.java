package swen221.lab3.model;

import java.awt.Color;
import java.awt.Graphics;

public class Book extends PickupableItem {
	public static boolean isRead = false;
	public Book(){
	}

	@Override
	public String getDescription() {
		System.out.println("read method999 "+isRead);

		if(isRead == true){return "A book entitled \"Great Expectations\"; it looks like it has been read";}
		else{return "A book entitled \"Great Expectations\"";}//"A book entitled \"Great Expectations\";";
	}

	@Override
	public String[] getActions() {
		return new String[]{"Pickup", "Drop", "Read"};	}
	
	public void readBook(){
		isRead = true;
	}

	@Override
	public void draw(Graphics g) {

		int width = (int) g.getClipBounds().getWidth();
		int height = (int) g.getClipBounds().getHeight();
		int xStart = width / 4;
		int yStart = height / 4;
		int xEnd = (width * 3) / 4;
		int yEnd = (height * 3) / 4;
		g.setColor(Color.BLUE);
		g.fillRect(xStart, yStart, xEnd - xStart, yEnd - yStart);
		g.setColor(Color.BLUE.darker());
		g.fillRect(xStart+5, yStart+5, (xEnd-10) - xStart, (yEnd-10) - yStart);
		g.setColor(Color.BLACK);
		g.drawRect(xStart, yStart, xEnd - xStart, yEnd - yStart);
		
	}
}
