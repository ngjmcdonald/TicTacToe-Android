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
	
	private final String SAVE_FILE = "saveState.dat";
	
	private ImageButton[][] imgBoard;
	private int gameType;
	private int gameTypeSaved;
	private Board board;
	private Computer comp;
	private AlertDialog.Builder aBuilder;
	private Handler handler;
	private File file;
	private Button btnBack;
	private Button btnNew;
	private TextView txtOutput;
	private String output;
	
	
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        file = new File(SAVE_FILE);
        file = getFileStreamPath(SAVE_FILE);
        //instantiate image button array
        output = "";
        imgBoard = new ImageButton[3][3];
        comp = new Computer();
        aBuilder =  new AlertDialog.Builder(this);
        
        
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
        btnBack = (Button) findViewById(R.id.btnBack);
        btnNew = (Button) findViewById(R.id.btnNew);
        txtOutput = (TextView) findViewById(R.id.txtOutput);
        // TODO gameType is not saving on a certain condition, some combination of resume and orientation change,
        //now saves only for PVC
        if (savedInstanceState != null) {
        	
        	board = (Board) savedInstanceState.getSerializable("board");
        	if(board.isGameOver()){
        		imgsSetEnabled(false);
        		btnNew.setEnabled(true);
        	}else{
        		btnNew.setEnabled(false);
        	}
        	rePopImages(board.getBoard());
        	
        	gameType = savedInstanceState.getInt("gameType");
        	
        	
        }else{
        	if(file.exists()){
        		onLoad();
        		rePopImages(board.getBoard());
        		
    		}else{
    			board = new Board(this);
    			
    		}
        	
        	
        }
        
        btnBack.setOnClickListener(listener);
        btnNew.setOnClickListener(listener);
        
        //set the event listener for each image button
	    for(int r = 0; r <=2; r++){ 
	    	for(int c = 0; c <=2; c++){
	    		imgBoard[r][c].setOnClickListener(listener);
	        }
	    }
	    
	    aBuilder.setTitle("Game Over");
	    aBuilder.setNegativeButton("No",aListener);
		aBuilder.setPositiveButton("Ok",aListener);

	    // the game type is passed from the main menu, through a bundle
	    //depending on which button is selected it will be pvp or pv comp
        Bundle myBundle = getIntent().getExtras();
        gameType = myBundle.getInt("gameType");
        
        // check game type selected
        
        if(gameType != Main.RESUME && savedInstanceState == null){
        	board.resetBoard(gameType);
        	imgsSetEnabled(true);
        	clearImages();
        	btnNew.setEnabled(false);
        	
        }else if(gameType == Main.RESUME){ //ADDED ELSE IF, MAY BREAK SOMETHING!
        	rePopImages(board.getBoard());

        	if(savedInstanceState == null){
        		gameType = gameTypeSaved;
        	}else{
        		gameType = savedInstanceState.getInt("gameType");
        	}

        	if(board.isGameOver()){
        		imgsSetEnabled(false);
        		btnNew.setEnabled(true);
        	}else{
        		btnNew.setEnabled(false);
        	}
        	
        }
        
        
        
        
        
        // TODO need to test the game type with every on create,
        //it is called after the main menu button is pressed
        
        // TODO BLANK LOG FOR TESTING---- DELETE
        Log.d("Nathan","");
        
        
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onSave();
    }
    @Override
    public void onStop() {
        super.onDestroy(); //NOT THIS
//        super.onStop();// TODO THIS 
        onSave();
    }
	//----------------------------------------------------PUBLIC 
	public void onSaveInstanceState(Bundle outState){
    	outState.putSerializable("board",board);
    	outState.putInt("gameType", gameType);
    }
	
	
//----------------------------------------------------GETS/SETS
	
	public int getGameType(){
		return gameType;
	}
//----------------------------------------------------PRIVATE METHODS

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
        			imgBoard[r][c].setEnabled(false);
        		}else if(myBoard[r][c] == Board.OH){
        			imgBoard[r][c].setImageResource(R.drawable.oh);
        			imgBoard[r][c].setEnabled(false);
        		}
        		
        		
            }
        }
	}
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
	private DialogInterface.OnClickListener aListener = new DialogInterface.OnClickListener(){

		@Override
		public void onClick(DialogInterface dialog, int whichBtn) {
			if(whichBtn == -1){
				
				
			}else{
				finish();
			}
			
		}
		
		
	};

	
	//----------------------------------------------------EVENT HANDLERS
	private void onSave() {
        try{
            
            //parameters are name of file and the mode, which sets the permissions, readable says all apps can access it
            FileOutputStream outFileStream = openFileOutput(SAVE_FILE, MODE_WORLD_READABLE);
            
            
            ObjectOutputStream outObjectStream = new ObjectOutputStream(outFileStream);
            
            outObjectStream.writeObject(board);
            outObjectStream.writeInt(gameType);
//            outObjectStream.writeObject(comp);
            
            //flushes out the stream, by making sure all bytes have been transferred
            
            outObjectStream.flush();
            outObjectStream.close();
            
        }catch (Exception e){
            Log.d("Nathan", e.getMessage());
        }
    }
    
    private void onLoad() {
        
        try {
            
        		
                FileInputStream inFileStream = openFileInput(SAVE_FILE);
                ObjectInputStream inObjectStream = new ObjectInputStream(inFileStream);

                board = (Board) inObjectStream.readObject();
                gameTypeSaved = inObjectStream.readInt();
                Log.d("Nathan","ONLOAD"+ gameTypeSaved);
//                comp = (Computer) inObjectStream.readObject();
                
                inObjectStream.close();
            
                

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
    }

	
	
	private void onBtnClick(final View v){
		//separate thread to keep the ui from locking up
		
		
		
		
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					boolean newG = false;
					if(btnBack.getId() == v.getId()){
						finish();
					}else if(btnNew.getId() == v.getId()){ // TODO -------
						newG = true;
					}
					
					final boolean newGame = newG;
					
					// loop through image buttons on each click, checking to see which one was clicked and assigning an image to it
					for(int r = 0; r <=2; r++){ 
			        	for(int c = 0; c <=2; c++){
			        		if(imgBoard[r][c].getId() == v.getId()){
			        			
			        			int[] blah = new int[2];
			        			
			        			
			        			// had to create final versions of r and c, other wise eclipse is
			        			//afraid they will change during the run under the post
			        			final int x = r;
			        			final int y = c;
			        			final int z = board.getTurn(); //rename this var?
			        			
			        			if(board.getTurn() == 0){
	        						board.setBoard(x,y,Board.EX);
	        						Log.d("Nathan","GAMETYPE CLICK"+gameType);
	        						if(gameType != Main.PVP){
	        							output = board.ticTacToe();
	        							
	        							if(!board.isTie() && !board.isP1Win()){
	        								// TODO CHECK IF YOU WIN BEFORE COMP TAKES TURN
		        							blah = comp.play(board.getBoard(),board.getTouchCount());
		        							Log.d("Nathan","Comp move"+ blah[0] + blah[1]);
		        							
		        							board.setBoard(blah[0],blah[1],Board.OH);
		        							//board.setTurn(0);
		        							output = board.ticTacToe();
	        							}
	        							
	        							
        								
        								
	        						}
	        					}else{
	        					
        							board.setBoard(x,y,Board.OH);
        							
	        						
	        					}
			        			if(gameType != Main.PVC){
			        				output = board.ticTacToe();
			        			}
			        			
			        			final int compX = blah[0];
			        			final int compY = blah[1];
			        			final boolean tie = board.isTie();
			        			final boolean p1Win = board.isP1Win();
			        			
			        			
			        			
			        			// TODO Oh is being placed some times where you click
			        			// and game didn't tie when it was a tie
			        			
			        			imgBoard[x][y].post(new Runnable(){
			        				public void run(){
			        					if(z == 0){
			        						imgBoard[x][y].setImageResource(R.drawable.ex);
			        						if(gameType != Main.PVP){
//			        							Log.d("Nathan","" + tie+"------Down");
//			        							Log.d("Nathan","" + p1Win);
			        							if(!tie && !p1Win){
			        								imgBoard[compX][compY].setImageResource(R.drawable.oh);
				        							board.setTurn(0);
				        							imgBoard[compX][compY].setEnabled(false);
//				        							Log.d("Nathan",""+ compX + compY);
			        							}
			        							
			        						}
			        						
			        					}else{

		        							if(gameType != Main.PVC){
		        								imgBoard[x][y].setImageResource(R.drawable.oh);
		        							}
			        						
			        						
			        							
			        					}
			        					imgBoard[x][y].setEnabled(false);
			        				}
			        			});
			        			
			        			break;
			        		}

			            }	
			        	
			        }
					
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
				}
			}
		});
		thread.start();
		
	}
	
}

