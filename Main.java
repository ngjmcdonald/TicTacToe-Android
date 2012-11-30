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
	
	private Button btnPlayer;
	private Button btnComp;
	private Intent intent;
	private Bundle out;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);
	    //instantiate intent for game class
	    intent = new Intent("com.nathan.GAME");
	    //instantiate  bundle for contants values 
	    out = new Bundle();
	    
	    
	    btnPlayer = (Button)findViewById(R.id.btnPlayer);
        btnComp = (Button)findViewById(R.id.btnComp);
        
        btnPlayer.setOnClickListener(listener);
        btnComp.setOnClickListener(listener);
        
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
	
	private void onBtnClick(View v){
		
		//if the player button is picked then the player constant is sent to the game class
		//vice versa if other button is clicked
		if(btnPlayer.getId() == v.getId()){
			out.putInt("number", PVP);
		}else if(btnComp.getId() == v.getId()){
			out.putInt("number", PVC);
		}
		//put the bundle in the intent and start the activity with the intent
		intent.putExtras(out);
		startActivity(intent);
		
	}
  

}
