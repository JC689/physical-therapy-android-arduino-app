package com.example.bluetooth2;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class Threshold extends Activity {
	
	public final static String EXTRA_MESSAGE = "com.example.bluetooth2.MESSAGE";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_threshold);
		// Show the Up button in the action bar.
		setupActionBar();		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	/*public class SpinnerActivity extends Activity implements OnItemSelectedListener {
	    
	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	        // An item was selected. You can retrieve the selected item using
	        // parent.getItemAtPosition(pos)
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	        // Another interface callback
	    }
	    Spinner spinner = (Spinner) findViewById(R.id.spinner1);
	    spinner.setOnItemSelectedListener(this);
	}*/
	
	public void graphDisplay(View view){
		//Intent intent = new Intent(this, Threshold.class);
		LineGraph line = new LineGraph();
		Intent lineIntent = line.getIntent(this);
		/*EditText editText = (EditText) findViewById(R.id.editThresh1);
		String message = editText.getText().toString();
		lineIntent.putExtra(EXTRA_MESSAGE, message);
		startActivity(lineIntent);
		Intent lineIntent = new Intent(this,LineGraph.class);*/
		EditText editText = (EditText) findViewById(R.id.editThresh1);
		String message = editText.getText().toString();
		lineIntent.putExtra("threshold1", message);
		startActivity(lineIntent);
		
	}


}
