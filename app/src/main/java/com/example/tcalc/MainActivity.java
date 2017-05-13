package com.example.tcalc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;



public class MainActivity extends Activity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, MyDialog.ResultsListener, View.OnFocusChangeListener{
	  
	OpMode mOpMode=OpMode.UNDEFINED;
	int numsPrecision=2;
	//int contextId; //save the editText that triggered the context menu
	Integer colorFar = Color.BLUE;
	Integer colorCel = Color.BLUE;
	int idContextSetCel = R.id.colorBlue;
	int idContextSetFar = R.id.colorBlue;
	static final int INTERNAL_CHECK_REQUEST = 1;  // The request code
	static final String PREFERENCE_FILE = "exam1-prf";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        RadioGroup rg = new RadioGroup(this); //create the RadioGroup
        ((RadioGroup)findViewById(R.id.rgOperation)).setOnCheckedChangeListener(this);
        EditText edFar = ((EditText)findViewById(R.id.edFarenheit));
        edFar.addTextChangedListener(new MyTextWatcher(R.id.edFarenheit, R.id.edCelcius));
        edFar.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				return false;
			}
		});
        
        EditText edCel = ((EditText)findViewById(R.id.edCelcius));
        edCel.addTextChangedListener(new MyTextWatcher(R.id.edFarenheit, R.id.edCelcius));
	    ((Button)findViewById(R.id.btGo)).setOnClickListener(this);
	    
	    SharedPreferences settings = getSharedPreferences(PREFERENCE_FILE,MODE_PRIVATE);
	    if (settings.contains("valid-data")) {
	        numsPrecision = settings.getInt("precision",2);
	        colorCel = settings.getInt("celColor", Color.BLUE);
	        colorFar = settings.getInt("farColor", Color.BLUE);	    	
	    }
	    else if (savedInstanceState != null){
	        mOpMode = (OpMode) savedInstanceState.getSerializable("mode");
	        numsPrecision = savedInstanceState.getInt("precision");
	        colorCel = savedInstanceState.getInt("celColor");
	        colorFar = savedInstanceState.getInt("farColor");
	    }
	    else{
	        mOpMode = OpMode.UNDEFINED;
	        numsPrecision = 2;
	        colorCel = Color.BLUE;
	        colorFar = Color.BLUE;
	    }
	    
	    edFar.setTextColor(colorFar);
       	edCel.setTextColor(colorCel);
	    registerForContextMenu(edCel);
	    registerForContextMenu(edFar);
		edFar.setOnFocusChangeListener(this);
		edCel.setOnFocusChangeListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

      getMenuInflater().inflate(R.menu.context, menu);
      
      int[] colors = new int[]{Color.RED, Color.GREEN, Color.BLUE};
      for (int i=0; i<colors.length;i++){
          MenuItem item = menu.getItem(i);
          SpannableString s = new SpannableString(item.getTitle());
          s.setSpan(new ForegroundColorSpan(colors[i]), 0, s.length(), 0);
          item.setTitle(s);
      }

 		int menuId;
		Integer color = ((EditText)v).getCurrentTextColor();
		menuId = (color == Color.BLUE?R.id.colorBlue:color == Color.GREEN?R.id.colorGreen:R.id.colorRed);

      menu.findItem(menuId).setChecked(true);
      super.onCreateContextMenu(menu, v, menuInfo);
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
    	outState.putSerializable("mode", mOpMode);
    	outState.putInt("precision", numsPrecision);
       	outState.putInt("celColor", ((EditText)findViewById(R.id.edCelcius)).getCurrentTextColor());
	 	outState.putInt("farColor", ((EditText)findViewById(R.id.edFarenheit)).getCurrentTextColor());

    	 super.onSaveInstanceState(outState);
     }
    
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
       
        // Restore state members from saved instance
        /*
        mOpMode = (OpMode) savedInstanceState.getSerializable("mode");
        numsPrecision = savedInstanceState.getInt("precision");
        colorCel = savedInstanceState.getInt("celColor");
        colorFar = savedInstanceState.getInt("farColor");
        
        ((EditText)findViewById(R.id.edFarenheit)).setTextColor(colorFar);
        ((EditText)findViewById(R.id.edCelcius)).setTextColor(colorCel);
*/
    }
    
    @Override
    protected void onStop(){
    	SharedPreferences settings = getSharedPreferences(PREFERENCE_FILE,MODE_PRIVATE);
    	SharedPreferences.Editor edit = settings.edit();
    	edit.putBoolean("valid-data", true);
    	edit.putInt("precision", numsPrecision);
    	edit.putInt("celColor", ((EditText)findViewById(R.id.edCelcius)).getCurrentTextColor());
    	edit.putInt("farColor", ((EditText)findViewById(R.id.edFarenheit)).getCurrentTextColor());
    	edit.commit();
       super.onStop();
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        
        switch (id) {
        case R.id.action_exit:
			MyDialog.newInstance(MyDialog.EXIT_DIALOG).show(getFragmentManager(), "Exit Dialog");
            return true;
        case R.id.action_settings:
			MyDialog.newInstance(MyDialog.PRECISION_DIALOG).show(getFragmentManager(), "Precision Dialog");
			return true;
        case R.id.action_help:
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("https://en.wikipedia.org/wiki/Conversion_of_units_of_temperature"));
		    startActivity(intent); 
		    return true;
        default:
            return super.onOptionsItemSelected(item);
        }
 
    }

	public int getCurrentPrecision(){
		return this.numsPrecision;
	}


    private void SetNewPrecision(int newPrecision){
		this.numsPrecision = newPrecision;
        EditText ed = ((EditText)findViewById(R.id.edFarenheit));
		Double num;
		if (!ed.getText().toString().equals("")){
			num = Double.parseDouble(ed.getText().toString());
			ed.setText(String.format("%." +newPrecision +"f", num));
		}


        ed = ((EditText)findViewById(R.id.edCelcius));
		if (!ed.getText().toString().equals("")){
			num = Double.parseDouble(ed.getText().toString());
			ed.setText(String.format("%." +newPrecision +"f", num));
		}

 	}

	 @Override
	    public boolean onContextItemSelected(MenuItem item) {

		 MyEditText.EditTextContextMenuInfo menuInfo = (MyEditText.EditTextContextMenuInfo) item.getMenuInfo();
		 EditText ed = menuInfo.targetView;

	      switch (item.getItemId()) {
	     
	      	case R.id.colorRed:
		      ed.setTextColor(Color.RED);
		      break;
	      	case R.id.colorGreen:
		      ed.setTextColor(Color.GREEN);
		      break;
	      	case R.id.colorBlue:
		      ed.setTextColor(Color.BLUE);
		      break;
	      }
		return true;
	 }
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
        Button btn = (Button)findViewById(R.id.btGo);
        EditText edFar = (EditText)findViewById(R.id.edFarenheit);
        edFar.setEnabled(true);
        EditText edCel = (EditText)findViewById(R.id.edCelcius);
        edCel.setEnabled(true);
        if (checkedId == R.id.rdCheck)  {
        	mOpMode = OpMode.CHECK;
            btn.setEnabled(edFar.getText().length() > 0 && edCel.getText().length()>0);
        }
        else {
        	mOpMode = OpMode.CALCULATE;        	
            btn.setEnabled(edFar.getText().length() > 0 ^ edCel.getText().length()>0);
        }
  	
	}

	@Override
	public void onFinishedDialog(int requestCode, Object results) {
		switch(requestCode){
			case MyDialog.EXIT_DIALOG:
				finish();
				System.exit(0);
				break;
			case MyDialog.PRECISION_DIALOG:
				SetNewPrecision((Integer)results);
				break;
		}
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus)
			SetNewPrecision(getCurrentPrecision());
	}


	private class MyTextWatcher implements TextWatcher{
		private int idMy, idOther;
		MyTextWatcher(int idMy, int idOther) {
			this.idMy = idMy;
			this.idOther = idOther;
		}
		@Override
		public void afterTextChanged(Editable s) {
	        Button btn = (Button)findViewById(R.id.btGo);
	        EditText edFar = (EditText)findViewById(R.id.edFarenheit);
	        EditText edCel = (EditText)findViewById(R.id.edCelcius);
	       if (MainActivity.this.mOpMode.equals(OpMode.CHECK))  {
	            btn.setEnabled(edFar.getText().length() > 0 && edCel.getText().length()>0);
	        }
	        else {     	
	            btn.setEnabled(edFar.getText().length() > 0 ^ edCel.getText().length()>0);
	        }
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {}
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {}
	}
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        // Check which request we're responding to
	        if (requestCode == INTERNAL_CHECK_REQUEST) {
	            // Make sure the request was successful
	            if (resultCode == RESULT_OK) {    			
	           		String strFar = data.getExtras().getString("farVal");
	           		((EditText)findViewById(R.id.edFarenheit)).setText(strFar);
	           		String strCel = data.getExtras().getString("celVal");
	           		((EditText)findViewById(R.id.edCelcius)).setText(strCel);
	           }
	        }
	    }
	@Override
	public void onClick(View v) {
		
	   	Intent intentGo = new Intent(this, CalcActivity.class); //explicit intent
	   	intentGo.setType("text/plain");

	   	intentGo.putExtra("precision", numsPrecision)
	   			.putExtra("celVal", ((EditText)findViewById(R.id.edCelcius)).getText().toString())	   		
	   			.putExtra("farVal", ((EditText)findViewById(R.id.edFarenheit)).getText().toString())	   		
	   			.putExtra("operation", mOpMode);
	   	startActivityForResult(intentGo,INTERNAL_CHECK_REQUEST);	
	}

}
