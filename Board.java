package com.nathan;

import android.util.Log;

public class Board {
	//-------------------------------------------------CONSTANTS
	public static final int EMPTY = 0;
	public static final int EX = 1;
	public static final int OH = 2;
	
	//-------------------------------------------------PROPERTIES
	
	private int turn;
	private int[][] board;
	private boolean isWin;
	
	//-------------------------------------------------CONSTRUCTOR
	public Board(){
		turn = 0;
		board = new int[3][3];
		isWin = false;
		
		
		
		
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
	public void setBoard(int[][] board) {
		this.board = board;
	}
	public boolean isWin() {
		return isWin;
	}
	
	
	
	
	//-------------------------------------------------PUBLIC METHODS
	// method to reset the game, maybe call it newGame()? 
	//but that sounds like it should be in the game class..
	public void resetBoard(int myGameType){
		//check the game type
		if(myGameType == Main.PVP){
			Log.d("Nathan","Game type is PVP");
		}else if(myGameType == Main.PVC){
			Log.d("Nathan","Game type is PVC");
		}
	}
	
	public int ticTacToe(){

		//game has to toggle the turn by itself
		if(turn == 1){
			turn = 0;
		}else{
			turn = 1;
		}
		
		
		
		
		
		return turn;
	}
	//method to re popluate the imgBoard on orientation change OR if the game shuts down
	// there is alread a get board this is probably useless....
	public int[][] populate(){
		
		return board;
	}
	//-------------------------------------------------PRIVATE METHODS
	
	private void checkWin(){
		
	}
	
}
