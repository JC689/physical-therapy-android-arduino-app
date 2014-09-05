

package com.example.bluetooth2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
//import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener {

	Button Connect;
	ToggleButton OnOff;
	TextView Result;
	private String dataToSend;
	
	private static final String TAG = "Jon";
	private BluetoothAdapter mBluetoothAdapter = null;
	private BluetoothSocket btSocket = null;
	private OutputStream outStream = null;
	private static String address = "00:06:66:68:3A:4D";
	private static final UUID MY_UUID = UUID
			.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private InputStream inStream = null;
    Handler handler = new Handler(); 
    byte delimiter = 10;
    boolean stopWorker = false;
    int readBufferPosition = 0;
    byte[] readBuffer = new byte[1024];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Connect = (Button) findViewById(R.id.connect);
		//OnOff = (ToggleButton) findViewById(R.id.tgOnOff);
		Result = (TextView) findViewById(R.id.analog);

		Connect.setOnClickListener(this);
		//OnOff.setOnClickListener(this);

		CheckBt();
		BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		Log.e("Jon", device.toString());

	}

	public void onClick(View control) {
		switch (control.getId()) {
		case R.id.connect:
				Connect();
			break;
		case R.id.tgOnOff:
			if (OnOff.isChecked()) {
				dataToSend = "1";
				writeData(dataToSend);
			} else if (!OnOff.isChecked()) {
				dataToSend = "0";
				writeData(dataToSend);
			}
			break;
		}
	}

	private void CheckBt() {
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (!mBluetoothAdapter.isEnabled()) {
			Toast.makeText(getApplicationContext(), "Bluetooth Disabled !",
					Toast.LENGTH_SHORT).show();
		}

		if (mBluetoothAdapter == null) {
			Toast.makeText(getApplicationContext(),
					"Bluetooth null !", Toast.LENGTH_SHORT)
					.show();
		}
	}
	
		public void Connect() {
			Log.d(TAG, address);
			BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
			Log.d(TAG, "Connecting to ... " + device);
			mBluetoothAdapter.cancelDiscovery();
			try {
				btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
				btSocket.connect();
				Log.d(TAG, "Connection made.");
			} catch (IOException e) {
				try {
					btSocket.close();
				} catch (IOException e2) {
					Log.d(TAG, "Unable to end the connection");
				}
				Log.d(TAG, "Socket creation failed");
			}
			
			beginListenForData();
		}
	
	private void writeData(String data) {
		try {
			outStream = btSocket.getOutputStream();
		} catch (IOException e) {
			Log.d(TAG, "Bug BEFORE Sending stuff", e);
		}

		String message = data;
		byte[] msgBuffer = message.getBytes();

		try {
			outStream.write(msgBuffer);
		} catch (IOException e) {
			Log.d(TAG, "Bug while sending stuff", e);
		}
	}

	/*@Override
	protected void onDestroy() {
		super.onDestroy();
	
			try {
				btSocket.close();
			} catch (IOException e) {
			}
	}*/
	
	public void beginListenForData()   {
		 try {
				inStream = btSocket.getInputStream();
			} catch (IOException e) {
			}
		 
	        Thread workerThread = new Thread(new Runnable()
	        {
	            public void run()
	            {                
	               while(!Thread.currentThread().isInterrupted() && !stopWorker)
	               {
	                    try 
	                    {
	                        int bytesAvailable = inStream.available();                        
	                        if(bytesAvailable > 0)
	                        {
	                            byte[] packetBytes = new byte[bytesAvailable];
	                            inStream.read(packetBytes);
	                            for(int i=0;i<bytesAvailable;i++)
	                            {
	                                byte b = packetBytes[i];
	                                if(b == delimiter)
	                                {
	                                    byte[] encodedBytes = new byte[readBufferPosition];
	                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
	                                    final String data = new String(encodedBytes, "US-ASCII");
	                                    readBufferPosition = 0;
	                                    handler.post(new Runnable()
	                                    {
	                                        public void run()
	                                        {
	                                        	
	                                        	if(data.contains("a0")){
	                                        		Result.setText("Analog 0 : "+data.substring(2));
	                                        	}
	                                        	/*if(Result.getText().toString().equals("A0")) {
	                                        		Result.setText(data);
	                                        	} else {
	                                        		Result.append("\n"+data);
	                                        	}*/
	                                        	
	                                        	/* You also can use Result.setText(data); it won't display multilines
	                                        	*/
	                                        	
	                                        }
	                                    });
	                                    /*try {
	                                        File myFile = new File("/sdcard/Data.txt");
	                                        myFile.createNewFile();
	                                        FileOutputStream fOut = new FileOutputStream(myFile);
	                                        OutputStreamWriter myOutWriter = 
	                                                                new OutputStreamWriter(fOut);
	                                        myOutWriter.append(data);
	                                        myOutWriter.close();
	                                        fOut.close();
	                                        Toast.makeText(getBaseContext(), 
	                                                "Done writing SD 'Data.txt'",
	                                                Toast.LENGTH_SHORT).show();
	                                    } catch (Exception e) {
	                                        Toast.makeText(getBaseContext(), e.getMessage(),
	                                                Toast.LENGTH_SHORT).show();
	                                    }*/
	                                }
	                                else
	                                {
	                                    readBuffer[readBufferPosition++] = b;
	                                }
	                            }
	                        }
	                    } 
	                    catch (IOException ex) 
	                    {
	                        stopWorker = true;
	                    }
	               }
	            }
	        });

	        workerThread.start();
	    }
 /* public void sendMessage(View view) {
	  Intent intent = new Intent(this, Threshold.class);
	  startActivity(intent);
  }*/
 }