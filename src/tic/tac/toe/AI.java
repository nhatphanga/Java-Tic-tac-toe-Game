package tic.tac.toe;

import java.util.Random;

public class AI {


	public static Random random = new Random();
	public static boolean found = false;
	public static boolean AIFirst = false;
	public static int chance;

	// checking the boxes if they are the same as the xo char it needs to check
	public static boolean checkForAI(int i1, int j1, int i2, int j2, char xo) {
		if (  ( Game.moves[i1][j1] == Game.moves[i2][j2]) && (Game.moves[i1][j1] == xo )  )
			return true;
		else
			return false;
	}

	public static int[] getRandomIJ() {  // get random location for the AI in case it can play any available square
		int i = 0;
		int j = 0;
    
		do
		{
			i = random.nextInt(3)+1;
			j = random.nextInt(3)+1;
			if ( Game.moves[i][j] == 'e')
			{
				found = true;
			}
		} while (found == false);
		return new int[] {i,j};
	}


	public static int[] findIJ( int diff) {    // This is the main method that connect everything together. 
		found = false; //reset found to false
		
		if (diff == 38779) { // special return, make it unbeatable
			return getUnbeatableIJ();
		}
		
		chance = random.nextInt(10);
		if (chance > diff) { // is the chance number is out of the probability range, AI will generate a random
			return getRandomIJ();
		}
  
		return getIJ();
	}

	public static int[] getUnbeatableIJ() {
		if (Game.getMoves() <=2)  { //for Unbeatable AI, simply just check for the center if it is empty and play it
			if ( Game.moves[2][2] == 'e') {
				found = true;
				return new int[] {2,2};
				}					
		}
		return getIJ();
	}


	public static int[] getIJ() {
		char xo = 'o';
		
		if (Game.getMoves() ==2 || Game.getMoves() == 1)  { //for advance AI, target a corner in the first move
			int i = 0;
			int j = 0;
			boolean cornerFound = false;
			do
			{
				i = random.nextInt(3)+1;
				j = random.nextInt(3)+1;
				if ((i%2 == 1 && j%2 == 1) || ( i%2 == 0 && j%2 == 0)) {
					if ( Game.moves[i][j] == 'e') {
						found = true;
						return new int[] {i,j};
					}
				}
			} while (cornerFound == false);
			 return new int[] {i,j}; 
		}
					
		int xoReturn[] = loopIJ(xo); // first loop to check the move that it can win
		if (xoReturn[0] != -1) { // if it is valid, return
			return new int[] {xoReturn[0], xoReturn[1]};
		}
		else {
			xo = 'x';  // or swap it to x and check the move it can block
			int xoReturn2[] = loopIJ( xo);
			if (xoReturn2[0] != -1) {
				return new int[] {xoReturn2[0], xoReturn2[1]};
			}
			else {				
				return getRandomIJ(); //if both fail, getRandom
			}
		}	
	} 

	
	public static int[] loopIJ( char xo) {   // the condition loop consist of 8 different conditional checks in total
		for (int i = 1; i<4;i++) {            //based on 4 possible directions of the move ( horizontal, vertical, diagonal left to right and diagonal right to left)
			for (int j = 1; j<4;j++) {         // and 2 distance options for each (either 1 box away or 2 boxes away)
				if (j<=3) {					
					if (checkForAI(i,j,i,j+1,xo)) {  //horizontal check
						if (Game.moves[i][j+2] == 'e') {
							found = true;
							return new int[] {i,j+2};
						}
						else if (Game.moves[i][j-1] == 'e') {
							found = true;
							return new int[] {i,j-1};
						}
					}
				}
				if (i<=3) {
					if (checkForAI(i,j,i+1,j,xo)) { //vertical check	
						if (Game.moves[i+2][j] == 'e') {
							found = true;
							return new int[] {i+2,j};
						}
						else if (Game.moves[i-1][j] == 'e') {
							found = true;
							return new int[] {i-1,j};
						}
					}
				}
			  
				if(i <= 2 && j <= 2) {
					if (checkForAI(i,j,i+1,j+1,xo)) { //diagonal left to right check
						if (Game.moves[i+2][j+2] == 'e') {					  
							found = true;
							return new int[] {i+2,j+2};
						}
						else if (Game.moves[i-1][j-1] == 'e') {					  
							found = true;
							return new int[] {i-1,j-1};
						}
					}
				}
				if(i>=2 && j<=2) {
					if (checkForAI(i,j,i+1,j-1,xo)) { //diagonal right to left check
						if (Game.moves[i+2][j-2] == 'e') {
							found = true;
							return new int[] {i+2,j-2};
						}
						else if (Game.moves[i-1][j+1] == 'e') {
							found = true;
							return new int[] {i-1,j+1};
						}
					}
				}
				if (j <= 3 && i == 1) {
					if (checkForAI(i,j,i+2,j,xo)) {  // vertical jump check 
						if (Game.moves[i+1][j] == 'e') {
							found = true;
							return new int[] {i+1,j};
						}
					}
				}
				if (i <= 3 && j == 1) {
					if (checkForAI(i,j,i,j+2,xo)) { //horizontal jump check	
						if (Game.moves[i][j+1] == 'e') {
							found = true;
							return new int[] {i,j+1};
						}
					}
				}
				  
				if (i == 1 && j ==1) {
					if (checkForAI(i,j,i+2,j+2,xo)) { //diagonal left to right jump check
						if (Game.moves[i+1][j+1] == 'e') {					  
							found = true;
							return new int[] {i+1,j+1};
						}
					}
				}
				if (i == 1 && j == 3) {
					if (checkForAI(i,j,i+2,j-2,xo)) { //diagonal right to left jump check
						if (Game.moves[i+1][j-1] == 'e') {
							found = true;
							return new int[] {i+1,j-1};
						}
					}
				}
				  
			}
		}
		return new int[] {-1,-5}; // if it cannot get a valid answer, return -1 back to the main method
	}
  
}
