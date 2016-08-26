package swen221.lab3.model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class LockedDoor extends Door{
	public boolean isLock = true;
	private int code;
	private Room oneSide;	
	private Room otherSide;
	public LockedDoor(Room oneSide, Room otherSide, int code){
		super(oneSide,otherSide);
		this.code = code;
		this.oneSide = oneSide;
		this.otherSide = otherSide;
		}

	public int getCode(){
		return this.code;
	}
	public Room getSide(){
		return this.oneSide;
	}

	@Override
	public String[] getActions() {
		return new String[]{"Enter","Unlock","Lock"};
	}

	@Override
	public boolean performAction(String action, Player player) {
		System.out.println(action+ code);
		Room r = player.getLocation();

		if(action.equals("Enter")){
			if(isLock == false){
				if(r == oneSide) {
					player.setLocation(otherSide);
					//isLock = true;
					return true;
				} else {
					player.setLocation(oneSide);
					//isLock = true;
					return true;
				}
			}
			
			else{return false;}
		}
		else if(action.equals("Unlock")){
			Key key = new Key(code);
			for(Item i: player.getInventory()){
				Key key1 = (Key)i;
				if(key.getCode() == key1.getCode()){				
					if(isLock==true && key1.getCode() == 123){
						isLock = false;
						System.out.println("true");

						return true;
					}
					else {return false;}
}
			}
		
		}
		else if(action.equals("Lock")){
			if(isLock==false){
				isLock = true;
				return true;
			}
			else return false;
		}
		
		// First, check which side of the door the player is on.
		// Done
		
		return false;
	}

	@Override
	public String getDescription() {
		return "Require a passcode";
	}
	@Override
	public void draw(Graphics g) {
		int width = (int) g.getClipBounds().getWidth();
		int height = (int) g.getClipBounds().getHeight();
		int xStart = width / 4;
		int yStart = height / 8;
		int xEnd = (width * 3) / 4;
		int yEnd = (height * 7) / 8;
		g.setColor(Color.GREEN);
		g.fillRect(xStart, yStart, xEnd - xStart, yEnd - yStart);
		g.setColor(Color.BLACK);
		g.drawRect(xStart, yStart, xEnd - xStart, yEnd - yStart);
		g.fillRect(xStart+10, height / 3,10,10);
	}


}
