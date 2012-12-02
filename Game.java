package com.nathan;

import java.util.Arrays;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;


public class Game extends Activity {
	
	private ImageButton[][] imgBoard;
	private int gameType;
	private Board board;
	private AlertDialog.Builder aBuilder;
	private Handler handler;
	
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        
        //instantiate image button array
        
        imgBoard = new ImageButton[3][3];
        
        aBuilder =  new AlertDialog.Builder(this);
        
        aBuilder.setTitle("Game Over");
        handler = new Handler();
        
        
//        for(int r = 0; r <=2; r++){ 
//        	for(int c = 0; c <=2; c++){
//        		
//            }
//        }
        
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
        
        if (savedInstanceState != null) {
        	
        	board = (Board) savedInstanceState.getSerializable("board");
        	rePopImages(board.getBoard());
        	
        }else{
        	board = new Board();
        	
        }
        
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
        Log.d("Nathan","Gametpye: " + gameType);
        
        // TODO need to test the game type with every on create,
        //it is called after the main menu button is pressed
        
        // TODO BLANK LOG FOR TESTING---- DELETE
        Log.d("Nathan","");
        
        // TODO you may have to call onDestroy and onStop to make sure the state is always saved
        
    }
	//----------------------------------------------------PUBLIC 
	public void onSaveInstanceState(Bundle outState){
    	outState.putSerializable("board",board);
    }
	
	
//----------------------------------------------------GETS/SETS
	
	public int getGameType(){
		return gameType;
	}
//----------------------------------------------------PRIVATE METHODS
	private void newGame(){
		board.resetBoard(gameType);
	}
	
	private void clearImages(){
		for(int r = 0; r <=2; r++){ 
        	for(int c = 0; c <=2; c++){
        		imgBoard[r][c].setImageResource(0);
            }
        }
	}
	private void rePopImages(int[][] myBoard){
		for(int r = 0; r <=2; r++){ 
        	for(int c = 0; c <=2; c++){
        		if(myBoard[r][c] == Board.EX){
        			imgBoard[r][c].setImageResource(R.drawable.ex);
        		}else if(myBoard[r][c] == Board.OH){
        			imgBoard[r][c].setImageResource(R.drawable.oh);
        		}
        		
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
	private DialogInterface.OnClickListener aListener = new DialogInterface.OnClickListener(){

		@Override
		public void onClick(DialogInterface dialog, int whichBtn) {
			if(whichBtn == -1){
				board.resetBoard(gameType);
				clearImages();
				
			}else{
				finish();
			}
			
		}
		
		
	};

	
	//----------------------------------------------------EVENT HANDLERS
	//this may clean things up a bit..
	private Runnable runnable = new Runnable(){

		@Override
		public void run() {
			
			
		}
		
	};
	
	
	private void onBtnClick(final View v){
		//separate thread to keep the ui from locking up
		
//		aBuilder.setPositiveButton("OK",aListener);
//    	// make it show the dialog window
//		aBuilder.show();
		
		
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					
					
					
					
					// loop through image buttons on each click, checking to see which one was clicked and assigning an image to it
					for(int r = 0; r <=2; r++){ 
			        	for(int c = 0; c <=2; c++){
			        		if(imgBoard[r][c].getId() == v.getId()){

			        			// had to create final versions of r and c, other wise eclipse is
			        			//afraid they will change during the run under the post
			        			final int x = r;
			        			final int y = c;
			        			final int z = board.getTurn(); //rename this var?
			        			
			        			imgBoard[x][y].post(new Runnable(){
			        				public void run(){
			        					if(z == 0){
			        						imgBoard[x][y].setImageResource(R.drawable.ex);
			        					}else{
			        						imgBoard[x][y].setImageResource(R.drawable.oh);
			        						
			        					}
			        				}
			        			});
			        			
			        			
			        			
			        			if(board.getTurn() == 0){
	        						board.setBoard(x,y,Board.EX);
	        					}else{
	        						board.setBoard(x,y,Board.OH);
	        					}
			        			aBuilder.setMessage(board.ticTacToe());
			        			
			        			
			        			handler.post(new Runnable(){

									@Override
									public void run() {
										// TODO popup wont show during tie
										if(board.isWin() == true){
											
											aBuilder.setNegativeButton("No",aListener);
											aBuilder.setPositiveButton("Ok",aListener);
											aBuilder.show();
										}
									}
			        				
			        			});
			        			
			        			
			        			
			        			
			        		}
			        		
//			        		if(board.isWin()==true){
//			        			imgBoard[r][c].setEnabled(false);
//			        		}
			            }	
			        }
					
					
				}catch(Exception e){
					Log.d("Nathan","Exception in Thread: " + e.getMessage());
				}
			}
		});
		thread.start();
		
		
		Log.d("Nathan",Boolean.toString(board.isWin()));
		
		
		
		
	}
	
}

