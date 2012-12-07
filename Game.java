package com.nathan;

import java.io.*;

import java.util.Arrays;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;


public class Game extends Activity {
	// constant for the save file name
	private final String SAVE_FILE = "saveState.dat";
	
	// two dimensional array of image buttons
	private ImageButton[][] imgBoard;
	// variable to hold the game type and the game type that gets saved on app close and orientation change
	private int gameType;
	private int gameTypeSaved;
	// variable to hold the board object and the computer object
	private Board board;
	private Computer comp;
	//variable to hold the handler object and the file object 
	private Handler handler;
	private File file;
	// variables to hold the button objects and the text view 
	private Button btnBack;
	private Button btnNew; 
	private TextView txtOutput;
	// string output for the  output text view
	private String output;
	
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        // init the file object with the save file constant 
        file = new File(SAVE_FILE);
        file = getFileStreamPath(SAVE_FILE);
        //instantiate image button array
        imgBoard = new ImageButton[3][3];
        output = "";
        // init the computer and handler object 
        comp = new Computer();
        handler = new Handler();
        
        //get the  image button views for each image button
        imgBoard[0][0] = (ImageButton)findViewById(R.id.btnImg0);
        imgBoard[0][1] = (ImageButton)findViewById(R.id.btnImg1);
        imgBoard[0][2] = (ImageButton)findViewById(R.id.btnImg2);
        imgBoard[1][0] = (ImageButton)findViewById(R.id.btnImg3);
        imgBoard[1][1] = (ImageButton)findViewById(R.id.btnImg4);
        imgBoard[1][2] = (ImageButton)findViewById(R.id.btnImg5);
        imgBoard[2][0] = (ImageButton)findViewById(R.id.btnImg6);
        imgBoard[2][1] = (ImageButton)findViewById(R.id.btnImg7);
        imgBoard[2][2] = (ImageButton)findViewById(R.id.btnImg8);
        //get the button views and the text view
        btnBack = (Button) findViewById(R.id.btnBack);
        btnNew = (Button) findViewById(R.id.btnNew);
        txtOutput = (TextView) findViewById(R.id.txtOutput);
        
        //check the savedInstanceState is null or not
        if (savedInstanceState != null) {
        	// get the board object 
        	board = (Board) savedInstanceState.getSerializable("board");
        	//if the game is over disable buttons and enable new button 
        	// otherwise disable the new button
        	if(board.isGameOver()){
        		imgsSetEnabled(false);
        		btnNew.setEnabled(true);
        	}else{
        		btnNew.setEnabled(false);
        	}
        	// repopulate the images with the board object 
        	rePopImages(board.getBoard());
        	//reset the text object
        	txtOutput.setText(savedInstanceState.getString("txtOutput"));
        	// get the game type integer 
        	gameType = savedInstanceState.getInt("gameType");
        	
        	// if the savedInstanceState is null check if the file exists 
        	
        }else{
        	if(file.exists()){
        		//if the file exists call the on load method and repopulate the images 
        		onLoad();
        		rePopImages(board.getBoard());
    		}else{
    			//otherwise create a new board object
    			board = new Board(this);	
    		}
        }
        
        // add listeners to the buttons
        btnBack.setOnClickListener(listener);
        btnNew.setOnClickListener(listener);
        
        //set the event listener for each image button
	    for(int r = 0; r <=2; r++){ 
	    	for(int c = 0; c <=2; c++){
	    		imgBoard[r][c].setOnClickListener(listener);
	        }
	    }
	    

	    // the game type is passed from the main menu, through a bundle
	    //depending on which button is selected it will be pvp or pv comp
        Bundle myBundle = getIntent().getExtras();
        gameType = myBundle.getInt("gameType");
        
        // check game type selected
        
        // check the game type for the resume option    
        if(gameType != Main.RESUME && savedInstanceState == null){
        	// if not reset everything 
        	board.resetBoard(gameType);
        	imgsSetEnabled(true);
        	clearImages();
        	btnNew.setEnabled(false);
        	
        }else if(gameType == Main.RESUME){
        	// if resume repopulate images 
        	rePopImages(board.getBoard());
        	
        	// check the saved instance state, otherwise this condition
        	// will over write during orientation change
        	if(savedInstanceState == null){
        		gameType = gameTypeSaved;
        	}else{
        		gameType = savedInstanceState.getInt("gameType");
        	}
        	// check if the game is over and keep the board disabled 
        	if(board.isGameOver()){
        		
        		imgsSetEnabled(false);
        		btnNew.setEnabled(true);
        		
        	}else{
        		btnNew.setEnabled(false);
        	}
        	
        }
 
    }
	// on destroy and onStop methods called to save the object in a binary file
    @Override
    public void onDestroy() {
        super.onDestroy();
        onSave();
    }
    @Override
    public void onStop() {
    	super.onStop(); 
        onSave();
    }
    // onSaveInstanceState called to save the board object, game type and the output text
    @Override
	public void onSaveInstanceState(Bundle outState){
    	outState.putSerializable("board",board);
    	outState.putInt("gameType", gameType);
    	outState.putString("txtOutput",txtOutput.getText().toString());
    }
	
//----------------------------------------------------PRIVATE METHODS
    
    // method to clear all images
	private void clearImages(){
		for(int r = 0; r <=2; r++){ 
        	for(int c = 0; c <=2; c++){
        		imgBoard[r][c].setImageResource(0);
            }
        }
	}
	
	//method to repopulate images
	private void rePopImages(int[][] myBoard){
		for(int r = 0; r <=2; r++){ 
        	for(int c = 0; c <=2; c++){
        		// in a nested for loop, check the board for locations where there is an x or o and place an image accordingly
        		// also disable that location
        		if(myBoard[r][c] == Board.EX){
        			imgBoard[r][c].setImageResource(R.drawable.ex);
        			imgBoard[r][c].setEnabled(false);
        		}else if(myBoard[r][c] == Board.OH){
        			imgBoard[r][c].setImageResource(R.drawable.oh);
        			imgBoard[r][c].setEnabled(false);
        		}
        		
        		
            }
        }
	}
	// method to disable or enable all images 
	private void imgsSetEnabled(boolean isEnabled){
		for(int r = 0; r <=2; r++){ 
        	for(int c = 0; c <=2; c++){
        		imgBoard[r][c].setEnabled(isEnabled);
        		
            }
        }
	}
//----------------------------------------------------EVENT LISTENERS
	
	//on button click event listener
	private OnClickListener listener = new OnClickListener(){
		public void onClick(View v){
			//on button click event handler
			onBtnClick(v);
		}
	};


	
	//----------------------------------------------------EVENT HANDLERS
	// on save to save my object and data to a binary file
	private void onSave() {
        try{
            
            //parameters are name of file and the mode, which sets the permissions, readable says all apps can access it
            FileOutputStream outFileStream = openFileOutput(SAVE_FILE, MODE_WORLD_READABLE);
            
            // object output steam to write my object
            ObjectOutputStream outObjectStream = new ObjectOutputStream(outFileStream);
            
            // write the board object and the game type
            outObjectStream.writeObject(board);
            outObjectStream.writeInt(gameType);
            
            //flushes out the stream, by making sure all bytes have been transferred
            
            outObjectStream.flush();
            outObjectStream.close();
            
        }catch (Exception e){
        	Log.d("Nathan","ONSAVE Exception: " + e.getMessage());
        	e.printStackTrace();
        }
    }
    // method to load my object and game type from the file saved
    private void onLoad() {
        
        try {
            
        		//create file input stream object and object input stream object
                FileInputStream inFileStream = openFileInput(SAVE_FILE);
                ObjectInputStream inObjectStream = new ObjectInputStream(inFileStream);
                
                // read the object and gameType from the file
                board = (Board) inObjectStream.readObject();
                gameTypeSaved = inObjectStream.readInt();
                
                //close the stream 
                inObjectStream.close();
            
                

        } catch (Exception e) {
        	Log.d("Nathan","ONLOAD Exception: " + e.getMessage());
            e.printStackTrace();
        }
        
        
    }

	
	// handler for the button click
	private void onBtnClick(final View v){
		
		//separate thread to keep the ui from locking up
		Thread thread = new Thread(new Runnable(){
			
			@Override
			public void run(){
				try{
					// check for back button id, if clicked close the game activity
					boolean newG = false;
					if(btnBack.getId() == v.getId()){
						finish();
					// check if new button was clicked
					}else if(btnNew.getId() == v.getId()){
						// if clicked a boolean is set to true then a final boolean is assigned with it
						// to be used in another thread
						newG = true;
					}
					
					final boolean newGame = newG;
					
					// loop through image buttons on each click, checking to see which one was clicked and assigning an image to it
					for(int r = 0; r <=2; r++){ 
			        	for(int c = 0; c <=2; c++){
			        		if(imgBoard[r][c].getId() == v.getId()){
			        			// array to hold the position from the AI's move
			        			int[] pos = new int[2];
			        			
			        			// had to create final versions of r and c, to prevent them from changing while the 
			        			//post runnable is running
			        			final int x = r;
			        			final int y = c;
			        			//another final to hold the board turn
			        			final int z = board.getTurn(); 
			        			
			        			// check the turns, the first turn is always player one
			        			if(board.getTurn() == 0){
			        				// the board is set to the position with the new value
	        						board.setBoard(x,y,Board.EX);
	        						// check if the game is not PVP
	        						if(gameType != Main.PVP){
	        							// run through the turn 
	        							output = board.ticTacToe();
	        							// if the game is not over by tie or player one win
	        							// allow the AI to take it's turn
	        							if(!board.isTie() && !board.isP1Win()){
	        								// set the pos array equal to AI's move
		        							pos = comp.play(board.getBoard(),board.getTouchCount());
		        							// set the board to the move
		        							board.setBoard(pos[0],pos[1],Board.OH);
		        							// run the game
		        							output = board.ticTacToe();
	        							}
	        						}
	        					}else{
	        						//the computer will take the second turn
	        						//this else is reached if that didn't happen setting the board to
	        						//player twos move
        							board.setBoard(x,y,Board.OH);
	        					}
			        			// if the game is not PVC run the game 
			        			if(gameType != Main.PVC){
			        				output = board.ticTacToe();
			        			}
			        			
			        			// final variables to hold the computer position 
			        			final int compX = pos[0];
			        			final int compY = pos[1];
			        			// final booleans to hold the tie and player one win
			        			final boolean tie = board.isTie();
			        			final boolean p1Win = board.isP1Win();
			        			

			        			// use the image view post method to add the images to 
			        			// the image views after the main thread has run
			        			imgBoard[x][y].post(new Runnable(){
			        				public void run(){
			        					if(z == 0){
			        						// same process has obove but set the images to the image views 
			        						// based on the turn or if the computer is playing
			        						imgBoard[x][y].setImageResource(R.drawable.ex);
			        						if(gameType != Main.PVP){

			        							if(!tie && !p1Win){
			        								imgBoard[compX][compY].setImageResource(R.drawable.oh);
				        							board.setTurn(0);
				        							imgBoard[compX][compY].setEnabled(false);
			        							}
			        							
			        						}
			        						
			        					}else{

		        							if(gameType != Main.PVC){
		        								imgBoard[x][y].setImageResource(R.drawable.oh);
		        							}
			        						
			        						
			        							
			        					}
			        					// disable the image after clicked or the computer enters a value
			        					imgBoard[x][y].setEnabled(false);
			        				}
			        			});
			        			// break the loop so it doesn't continue searching
			        			// ASK SEAN WHY SOME PEOPLE THINK BREAKS ARE BAD
			        			break;
			        		}
			            }	
			        }
					// a handler object that creates a new runnable
					// the game is checked if it is over or not
					// if it is the board is reset if a new game is selected
        			handler.post(new Runnable(){
        				
						@Override
						public void run() {
							
							if(board.isGameOver() == true){
								btnNew.setEnabled(true);
								if(newGame){
									board.resetBoard(gameType);
									clearImages();
									imgsSetEnabled(true);
									btnNew.setEnabled(false);
									txtOutput.setText("");
								}else{
									// if the game is over and newGame isn't clicked disable the images and set the text out
									// put to the game over message
									imgsSetEnabled(false);
									txtOutput.setText(output);
								}
			        			
							}else{
								btnNew.setEnabled(false);
							}
						}
        				
        			});

					
					
				}catch(Exception e){
					Log.d("Nathan","Exception in Thread: " + e.getMessage());
					e.printStackTrace();
				}
			}
		});
		
		// start the thread 
		thread.start();
		
	}
	
}

