package com.nathan;

import java.io.Serializable;
import java.util.Arrays;

import android.content.Context;
import android.util.Log;

public class Board implements Serializable {

	//-------------------------------------------------CONSTANTS
	// constants to hold the empty, x and o values
	private static final long serialVersionUID = 1L;
	public static final int EMPTY = 0;
	public static final int EX = 1;
	public static final int OH = 2;
	
	//-------------------------------------------------PROPERTIES
	// ints to hold the turn count and the touch count
	private int turn, touchCount;
	//two dimensional array to hold the board value
	private int[][] board;
	//// boolen to check if the game is over, or which player won or a tie
	private boolean isGameOver;
	private boolean isP1Win;
	private boolean isP2Win;
	private boolean isTie;
	// strings to hold the concatonated board values for rows, columns and diagonals
	private String strRow,strCol,strDiag1,strDiag2;
	// string to hold the message on game over
	private String winMessage;
	// array to check the board values
	private String[] aryCol,aryRow;
	// global context to hold the context passed in 
	private  static Context context;
	
	
	//-------------------------------------------------CONSTRUCTOR
	public Board(Context con){
		// init the  
		board = new int[3][3];
		// reset the board   
		resetBoard(0);
		// init arrays and assign the context to the global
		aryRow = new String[3];
		aryCol = new String[3];
		context = con;
		
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
	public boolean isGameOver() {
		return isGameOver;
	}
	public int getTouchCount(){
		return touchCount;
	}
	public boolean isP1Win(){
		return isP1Win;
	}
	public boolean isP2Win(){
		return isP2Win;
	}
	public boolean isTie(){
		return isTie;
	}
	

	//-------------------------------------------------PUBLIC METHODS
	// method to reset the game
	public void resetBoard(int myGameType){
		// reset the properties
		isGameOver = false;
		isP1Win = false;
		isP2Win = false;
	   touchCount = 0;
	   turn= 0;
	   winMessage = "";
	   // loop through board and set each position to emprty
     for(int r = 0; r <=2; r++){ 
    	 for(int c = 0; c <=2; c++){
    		 board[r][c] = EMPTY;
    	 }
     }
		
	}
	
	// main method of the board class
	public String ticTacToe(){
		// increment the touch count
		touchCount++;
		//game has to toggle the turn by itself
		if(turn == 1){
			turn = 0;
		}else{
			turn = 1;
		}
		// use check win method to check if either play won or a tie
		checkWin();
		// return the win message set in the check Win method
		return winMessage;
	}
	
	//-------------------------------------------------PRIVATE METHODS
	
	private boolean checkWin(){
		// set the game over booleans to false
		isGameOver = false;
		isP1Win = false;
		isP2Win = false;
		isTie = false;
		
		// set the counter to 1 and empty all the variables and arrays
		int counter = 1;
		strRow = "";
		strCol = "";
		strDiag1 = "";
		strDiag2 = "";
		Arrays.fill(aryRow,"");
		Arrays.fill(aryCol,"");
		
		// only way to check the right diagonal is by manualy checking each position
		strDiag2 += board[0][2];
		strDiag2 += board[1][1];
		strDiag2 += board[2][0];
		

		// nested loop to run through board
		for(int r = 0; r <=2; r++){ 
			//clear strings after each row and column
			strRow="";
			strCol="";
    		for(int c = 0; c <=2; c++){
    			// concat the value of each position of each row and colum
    			strRow += board[r][c];
    			strCol += board[c][r];
    			
    			// checks the left side diagonal and concats into the string
    			if(r == c){
    				strDiag1 += board[r][c];
    			}
    			
    			// at the end of each rowand column
    			if(counter % 3 == 0){
    				 
    				aryRow[r] = strRow;
    				aryCol[c] = strCol;
    				
    				// check the strings if they are all ones or twos
    				// if they are set the win message and booleans to either player and break the loop
    				
    				// PLAYER ONE
    				if((aryRow[r].equals("111")) || (aryCol[c].equals("111")) 
    						|| (strDiag1.equals("111")) || strDiag2.equals("111")){
    					
    					winMessage = context.getResources().getString(R.string.xWins);
    					
    					isP1Win = true;
    					isGameOver = true;
    					break;
    				// PLAYER TWO / AI
    				}else if((aryRow[r].equals("222")) || (aryCol[c].equals("222")) 
    						|| (strDiag1.equals("222")) || strDiag2.equals("222")){
    					
    					winMessage = context.getResources().getString(R.string.oWins);
    					
    					isP2Win = true;
    					isGameOver = true;
    					
    					break;
    					
    				}
    			}	
    			// if the touch count is up to 9 and the game is still not over then it is a tie
    				if((touchCount == 9) && (!isGameOver)){
    					
    					winMessage = context.getResources().getString(R.string.gameTie);
    					
    					isTie = true;
    					isGameOver = true;
    					break;
    				}
    				
    			counter++;
     		}
    		
    		
    	}
		return isGameOver;
	}
	
}
