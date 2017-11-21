/*
 * DnD Character Manager.
 * Copyright (C) 2016 James Hamann
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.mizobogames.fhbgds;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

public class Dice implements Serializable{
	private static final long serialVersionUID = -7644086382623742779L;

	private List<Die> dice;
	
	private int faces;
	
	private int modifier;
	private int multiplier;
	private boolean hasMod = false;
	private boolean hasMult = false;
	private boolean multFirst = false;
	
	private Dice(){}
	public Dice(List<Die> dice){
		this.dice = dice;
	}

	public static Dice getDice(String dice) {
		int num = Integer.valueOf(dice.substring(0, dice.indexOf("d")));
		int faces = Integer.valueOf(dice.substring(dice.indexOf("d") + 1));
		LinkedList<Die> diceList = new LinkedList<Die>();
		Dice newDice = new Dice();
		for(int i = 0; i < num; i++){
			diceList.add(newDice.new Die(faces));
		}
		newDice.setDiceList(diceList);
		newDice.faces = faces;
		return newDice;
	}
	
	public int getNumOfFaces(){
		return faces;
	}
	
	public int roll(){
		int result = 0;
		for(Die d : this.dice){
			result += d.roll();
		}
		if(!this.multFirst){
			if(this.hasMod){
				result += this.modifier;
			}
			if(this.hasMult){
				result *= this.multiplier;
			}
		}else{
			if(this.hasMult){
				result *= this.multiplier;
			}
			if(this.hasMod){
				result += this.modifier;
			}
		}
		return result;
	}
	
	public List<Integer> rollAndReturnListOfResults(){
		List<Integer> results = new LinkedList<Integer>();
		for(Die d : this.dice){
			results.add(d.roll());
		}
		if(!this.multFirst){
			if(this.hasMod){
				results.add(this.modifier);
			}
			if(this.hasMult){
				results.add(this.multiplier);
			}
		}else{
			if(this.hasMult){
				results.add(this.multiplier);
			}
			if(this.hasMod){
				results.add(this.modifier);
			}
		}
		return results;
	}
	
	public Dice multFirst(){
		this.multFirst = true;
		return this;
	}
	
	public Dice multLast(){
		this.multFirst = false;
		return this;
	}

	public void setDiceList(List<Die> dice){
		this.dice = dice;
	}
	
	public Dice modifier(int modifier){
		this.modifier = modifier;
		this.hasMod = true;
		return this;
	}
	
	public Dice multiplier(int mult){
		this.multiplier = mult;
		this.hasMult = true;
		return this;
	}
	
	@Override
	public String toString(){
		return this.dice.size() + "d" + this.dice.get(0).faces;
	}
	
	public class Die implements Serializable{
		private static final long serialVersionUID = 449230527060035868L;

		public Die(int faces){
			this.faces = faces;
		}
		
		SecureRandom rand = new SecureRandom();
		int faces;

		public int roll() {
			return rand.nextInt(faces) + 1;
		}
	}
}
