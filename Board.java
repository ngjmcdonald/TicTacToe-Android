package com.nathan;

import java.io.Serializable;
import java.util.Arrays;

import android.util.Log;

public class Board implements Serializable {

	//-------------------------------------------------CONSTANTS
	private static final long serialVersionUID = 1L;
	public static final int EMPTY = 0;
	public static final int EX = 1;
	public static final int OH = 2;
	
	//-------------------------------------------------PROPERTIES
	
	private int turn, touchCount;
	private int[][] board;
	private boolean isWin;
	private String strRow,strCol,strDiag1,strDiag2;
	private String winMessage;
	private String[] aryCol,aryRow;
	
	
	//-------------------------------------------------CONSTRUCTOR
	public Board(){
		board = new int[3][3];
		isWin = false;
		resetBoard(0);
		
		aryRow = new String[3];
		aryCol = new String[3];
		
		
		
		
	}
	//-------------------------------------------------GETS/SETS
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
	public int[][] getBoard() {
		return board;
	}
	public void setBoard(int posR,int posY, int val) {
		board[posR][posY] = val;
	}
	public boolean isWin() {
		return isWin;
	}
	
	
	
	
	//-------------------------------------------------PUBLIC METHODS
	// method to reset the game, maybe call it newGame()? 
	//but that sounds like it should be in the game class..
	public void resetBoard(int myGameType){
	   touchCount = 0;
	   turn= 0;
	   winMessage = "";
     for(int r = 0; r <=2; r++){ 
    	 for(int c = 0; c <=2; c++){
    		 board[r][c] = EMPTY;
    	 }
     }
		
		//check the game type
		if(myGameType == Main.PVP){
			Log.d("Nathan","Game type is PVP");
		}else if(myGameType == Main.PVC){
			Log.d("Nathan","Game type is PVC");
		}
	}
	
	public String ticTacToe(){
		touchCount++;
		//game has to toggle the turn by itself
		if(turn == 1){
			turn = 0;
		}else{
			turn = 1;
		}
		
		
		
		
		
		checkWin();
		
		
		return winMessage;
	}
	//method to re popluate the imgBoard on orientation change OR if the game shuts down
	// there is alread a get board this is probably useless....
	public int[][] populate(){
		
		return board;
	}
	//-------------------------------------------------PRIVATE METHODS
	
	private boolean checkWin(){
		isWin = false;
		
		// TODO the winning message has to change depending on if it is the AI
		// that is playing or another player (Player Two Wins!) for eample
		/* a count is added to the loops, a string is concatonated for every three
		 * numbers then added to an array. if the values match 111 then player one wins*/
		int counter = 1;
		strRow = "";
		strCol = "";
		strDiag1 = "";
		strDiag2 = "";
		Arrays.fill(aryRow,"");
		Arrays.fill(aryCol,"");
		
		strDiag2 += board[0][2];
		strDiag2 += board[1][1];
		strDiag2 += board[2][0];
		
//		Log.d("Nathan",);
		
		for(int r = 0; r <=2; r++){ 
			strRow="";
			strCol="";
    		for(int c = 0; c <=2; c++){
    			strRow += board[r][c];
    			strCol += board[c][r];
    			
    			
    			if(r == c){
    				strDiag1 += board[r][c];
    			}
    			
    			
    			if(counter % 3 == 0){
    				
    				aryRow[r] = strRow;
    				aryCol[c] = strCol;
    				// TODO all these strings have to be in strings.xml
    				if((aryRow[r].equals("111")) || (aryCol[c].equals("111")) 
    						|| (strDiag1.equals("111")) || strDiag2.equals("111")){
    					
    					winMessage = "X wins! Play  again?";
    					isWin = true;
    					break;
    					
    				}else if((aryRow[r].equals("222")) || (aryCol[c].equals("222")) 
    						|| (strDiag1.equals("222")) || strDiag2.equals("222")){
    					
    					winMessage = "O wins! Play again?";
    					isWin = true;
    					break;
    					
    				}
    			}	
    				if(touchCount == 9){
    					winMessage = "Tie! Play again?";
    					isWin = true;
    				}
    				
    			counter++;
     		}
    		
    		
    	}
		return isWin;
	}
	
}
//loops to see what the numbers are in the console

//String foo = "";
//int b = 0;
//
//for(int r = 0; r <=2; r++){ 
//	for(int c = 0; c <=2; c++){
//		if(b % 3 == 0){
//			foo += "\n";
//		}
//		foo += Integer.toString(board[r][c]);
//		b++;
//		}
//}
//Log.d("Nathan", foo + "-----");
