package com.example.tcalc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CalcActivity extends Activity implements View.OnClickListener {
	OpMode mOpMode;
	String strFar,strCel;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		RelativeLayout rlCalcLayout = new RelativeLayout(this);
		TextView tvNotification = new TextView(this);
		tvNotification.setId(R.id.codeNotificationId);
		tvNotification.setTextSize(15);
		RelativeLayout.LayoutParams lpNotification = new RelativeLayout.LayoutParams(800, LayoutParams.WRAP_CONTENT);
		lpNotification.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lpNotification.setMargins(0, 100, 0, 0);
	     Intent intent = getIntent();
	      
	     Bundle bndl = intent.getExtras();
	     if (bndl != null){
//		     mOpMode = (OpMode)bndl.getSerializable("operation");
		     mOpMode = (OpMode)intent.getSerializableExtra("operation");
		     int numsPrecision = bndl.getInt("precision");
		     strFar = bndl.getString("farVal");
		     strCel= bndl.getString("celVal");
		     Float flFar,flCel;
		     if (mOpMode.equals(OpMode.CHECK)) {
		    	 flFar =  Float.parseFloat(strFar);
		    	 flCel =  Float.parseFloat(strCel);
		    	 strFar = String.format("%." +numsPrecision +"f", flFar);
	    		 strCel = String.format("%." +numsPrecision +"f", flCel);		    		 
		    	 if (Math.abs(flFar -((flCel * 9/5) +32)) < 0.05) //valid difference
		    		 tvNotification.setText("Bravo!, the temperature "+ strCel + "°C, is indeed "+ strFar + "°F");
		    	 else
		    		 tvNotification.setText("Oops!, your answer is wrong, you may try again.");		    		 
		     }
		     else {//calculate
		    	 if (strFar.isEmpty()) {
		    		 flFar =  (Float.parseFloat(strCel) * 9/5) + 32;
	//	    		 flFar = adjustPrecision(flFar,3);
	//	    		 strFar = Double.toString(flFar);
		    		 strFar = String.format("%." +numsPrecision +"f", flFar);
		    		 strCel = String.format("%." +numsPrecision +"f", Float.parseFloat(strCel));
		    		 tvNotification.setText("The temperature "+ strCel + "°C, is converted to "+ strFar + "°F");
		    	 }
		    	 else {
		    		 flCel =  (Float.parseFloat(strFar) -32) * 5/9;
//		    		 flCel = adjustPrecision(flCel,3);
//		    		 strCel = Double.toString(flCel);
		    		 strCel = String.format("%." +numsPrecision +"f", flCel);		    		 
		    		 strFar = String.format("%." +numsPrecision +"f", Float.parseFloat(strFar));		    		 
		    		 tvNotification.setText("The temperature "+ strFar + "°F, is converted to " + strCel + "°C");
		    	 }
		     }

	     }
	     
	     rlCalcLayout.addView(tvNotification, lpNotification);

	     Button myButton = new Button(this);
	     RelativeLayout.LayoutParams btnLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	     btnLayoutParams.addRule(RelativeLayout.BELOW,R.id.codeNotificationId);
	     btnLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
	     btnLayoutParams.setMargins(0, 300, 0, 0);
//	     myButton.setLayoutParams(btnLayoutParams);
	     myButton.setText("Return");
	     myButton.setBackgroundColor(Color.GRAY);

	     myButton.setOnClickListener(this);
	     myButton.setEnabled(true);

	     rlCalcLayout.addView(myButton, btnLayoutParams);

	     setContentView(rlCalcLayout, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	     super.onCreate(savedInstanceState);
	}

	private static double adjustPrecision(double valueToRound, int numberOfDecimalPlaces)
	{
	    double multipicationFactor = Math.pow(10, numberOfDecimalPlaces);
	    double interestedInZeroDPs = valueToRound * multipicationFactor;
	    return Math.round(interestedInZeroDPs) / multipicationFactor;
	}
	
	@Override
	public void onClick(View v) {
		
		Intent i = new Intent();
		i.putExtra("farVal", strFar);
		i.putExtra("celVal", strCel);			
		if (mOpMode.equals(OpMode.CHECK)) {
		}

		setResult(RESULT_OK, i);
		finish();

		
	}
}
