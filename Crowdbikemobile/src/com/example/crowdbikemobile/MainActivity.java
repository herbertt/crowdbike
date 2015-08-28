package com.example.crowdbikemobile;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;
import at.abraxas.amarino.Amarino;

public class MainActivity extends Activity {
	
	private static final String DEVICE_ADDRESS = "00:12:00:09:03:01";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Amarino.connect(this, DEVICE_ADDRESS);
    }
    
    
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	
    }
    
    @Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Amarino.disconnect(this, DEVICE_ADDRESS);
		
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    public void sendInformation(String informationText){
        String text = null;
        informationText = text;
    	Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'A', text);	
    }
    
}
