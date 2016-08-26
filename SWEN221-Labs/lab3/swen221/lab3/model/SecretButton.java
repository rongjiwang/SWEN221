package swen221.lab3.model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class SecretButton implements Item {
	private int code;
	private boolean buttonPushed = false;
	private Room oneSide;
	private Door door;
	private List<Item> list;
	public SecretButton(Room oneSide,int code){
		this.code = code;
		this.oneSide = oneSide;
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getActions() {
		return new String[]{"Press"};
	}

	@Override
	public boolean performAction(String action, Player player) {
		if(action == "Press"){
		list = player.getLocation().findDoors();
		for(Item l: list){
			if(l.getActions().length == 3 && l.getActions()[1]=="Unlock"){
				//System.out.println(l);
				LockedDoor ld = (LockedDoor)l;
				if(ld.getCode() == this.code){
					ld.isLock = false;
					buttonPushed = true;
					return true;
				}
				
			}
		}}
		return false;
	}

	@Override
	public void draw(Graphics g) {
		int width = (int) g.getClipBounds().getWidth();
		int height = (int) g.getClipBounds().getHeight();
		int xStart = width / 4;
		int yStart = height / 8;
		int xEnd = (width * 3) / 4;
		int yEnd = (height * 7) / 8;
	
		g.setColor(Color.YELLOW);
		g.fillRect(xStart, yStart, xEnd - xStart, yEnd - yStart);
		g.setColor(Color.gray);
		g.drawRect(xStart, yStart, xEnd - xStart, yEnd - yStart);
		
	}
	
}
