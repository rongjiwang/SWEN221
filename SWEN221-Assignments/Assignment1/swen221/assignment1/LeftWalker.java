package swen221.assignment1;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import maze.*;
/*
 * Student name: Rongji Wang
 * Student ID:   300139010
 * */
public class LeftWalker extends Walker {

	private Direction[] allDirections = Direction.values();
	//private Direction front= allDirections[0];;
	//private Direction right= allDirections[1];;
	//private Direction back= allDirections[2];;                 /*  walker's direction logic */
	//private Direction left= allDirections[3];;
	private boolean hadLeftWall = false;
	private Direction noWall = Direction.WEST;
	private int x = 0 ;
	private int y = 0 ;
	private Location loc;
	private Map<Location,Set<Direction>> map = new HashMap<Location,Set<Direction>>();
	private boolean outOfTheLoop = false;


	public LeftWalker() {
		super("Left Walker");

	}

	@Override
	protected Direction move(View v) {
		pause(100);
		return mazeWork(v);
	}

	public void walkerLocation(Direction d){       //update x and y
		if(d.equals(Direction.EAST)){
			this.x+=1;
		}
		else if(d.equals(Direction.WEST)){
			this.x-=1;
		}
		else if(d.equals(Direction.SOUTH)){
			this.y+=1;
		}
		else if(d.equals(Direction.NORTH)){
			this.y-=1;
		}
		}

	private Direction mazeWork(View v){
		loc = new Location(this.x,this.y);
		Direction go = determinePossibleDirections(v);  //hunt for direction to go

		boolean visited = false;
		for(Location l: map.keySet()){         //check for first time visitor
			if(l.getX() == this.x && l.getY() == this.y){
				visited = true;
			}
		}

		if(!visited){                                         //store in map if not visited
			Set<Direction> dir = new HashSet<Direction>();
			if(v.mayMove(Direction.EAST))dir.add(Direction.EAST);
			if(v.mayMove(Direction.NORTH))dir.add(Direction.NORTH);
			if(v.mayMove(Direction.WEST))dir.add(Direction.WEST);
			if(v.mayMove(Direction.SOUTH))dir.add(Direction.SOUTH);
			map.put(loc, dir);
		}

		else{                                                 //find the unvisited direction clockwise
			for(Location l: map.keySet()){

				if(l.getX() == this.x && l.getY() == this.y){
					Set<Direction> set = map.get(l);
					if(!set.contains(go)){                   // return the new direction
						if(set.contains(allDirections[1])) {
							go=allDirections[1];
							outOfTheLoop = true;
							break;
							}
						else if(set.contains(allDirections[2])) {
							go=allDirections[2];
							outOfTheLoop = true;
							break;
							}
						else if(set.contains(allDirections[3])){
							go=allDirections[3];
							outOfTheLoop = true;
							break;
							}
					}
				}
			}
		}
		for(Location l: map.keySet()){       //remove the choosen direction
			if(l.getX() == this.x && l.getY() == this.y){
				map.get(l).remove(go);
			}}
		walkerLocation(go);
		return go;
	}

	private Direction determinePossibleDirections(View v) {    //allDirections[0] is walker's head direction
		if(noWallAround(v) && hadLeftWall){
			rotateClockWise();
			return allDirections[0];
		}
		//left is free, and had left then move left
		else if(v.mayMove(allDirections[3]) && hadLeftWall && !outOfTheLoop){
			rotateClockWise();
			return allDirections[0];
		}
		if(outOfTheLoop && hadLeftWall){
			outOfTheLoop = false;
			return keepWallOnLeft(v);
		}
		if(noWallAround(v) && !hadLeftWall){
			if(noWall == Direction.WEST){return Direction.NORTH;}
			else if(noWall == Direction.NORTH){return Direction.EAST;}
			else if(noWall == Direction.EAST){return Direction.SOUTH;}
			else if(noWall == Direction.SOUTH){return Direction.WEST;}

		}
		 return keepWallOnLeft(v);
	}

	private boolean noWallAround(View v){
		if(v.mayMove(allDirections[0])&&v.mayMove(allDirections[1])&&
				v.mayMove(allDirections[2])&&v.mayMove(allDirections[3])){
			return true;
		}
		return false;
	}
	private Direction keepWallOnLeft(View v){  //return walker direction when has left wall and front is free to go
		for(int i=0; i<4; i++){
		if(!v.mayMove(allDirections[3]) && v.mayMove(allDirections[0])){
			hadLeftWall = true;
			return allDirections[0];
		}
		else{
			hadLeftWall = false;
			rotateAntiClockWise();
			 }}
		return null;
	}

	private void rotateClockWise(){            //clock wise
		Direction temp = allDirections[3];
		allDirections[3] =allDirections[2];
		allDirections[2]=allDirections[1];
		allDirections[1]=allDirections[0];
		allDirections[0] = temp;
	}
	private void rotateAntiClockWise(){         //anti clock wise
		Direction temp = allDirections[0];
		allDirections[0]=allDirections[1];
		allDirections[1]=allDirections[2];
		allDirections[2]=allDirections[3];
		allDirections[3]=temp;
	}

}