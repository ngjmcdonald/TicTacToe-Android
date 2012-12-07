package com.nathan;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity {
	//class constants that will signify to the game class which game type was chosen, pvp player vs comp
	public static final int PVP = 0;
	public static final int PVC = 1;
	public static final int RESUME = 2;
	// variables to hold the buttons, intent and bundle
	private Button btnPlayer, btnComp, btnResume, btnQuit;
	private Intent intent;
	private Bundle out;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    //instantiate intent for game class
	    // init the intent with the game activity 
	    intent = new Intent("com.nathan.GAME");
	    //instantiate  bundle for contants values 
	    out = new Bundle();
	    
	    //init all the buttons
	    btnPlayer = (Button)findViewById(R.id.btnPlayer);
        btnComp = (Button)findViewById(R.id.btnComp);
        btnResume = (Button)findViewById(R.id.btnResume);
        btnQuit= (Button)findViewById(R.id.btnQuit);
        //assign all the buttons the event listener
        btnPlayer.setOnClickListener(listener);
        btnComp.setOnClickListener(listener);
        btnResume.setOnClickListener(listener);
        btnQuit.setOnClickListener(listener);
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
	// button click event handler
	private void onBtnClick(View v){
		//check the source of the event and assign the bundle the appropriate constant
		
		//if the player button is picked then the player constant is sent to the game class
		//vice versa if other button is clicked
		if(btnPlayer.getId() == v.getId()){
			out.putInt("gameType", PVP);
		}else if(btnComp.getId() == v.getId()){
			out.putInt("gameType", PVC);
		}else if(btnResume.getId() == v.getId()){
			out.putInt("gameType", RESUME);
		}else if(btnQuit.getId() == v.getId()){
			finish();
		}
		
		//put the bundle in the intent and start the activity with the intent
		if(btnQuit.getId() != v.getId()){
			intent.putExtras(out);
			startActivity(intent);
		}
	
		
	}
  

}
