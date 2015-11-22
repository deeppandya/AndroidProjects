package com.example.barcodescan;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;

public class Print extends Activity {

private Socket client;
private PrintWriter printwriter;
private String _barcodestring;


public Print() {
	
}

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
//setContentView(R.layout.activity_simple_client);

//textField = (EditText) findViewById(R.id.editText1); //reference to the text field
//button = (Button) findViewById(R.id.button1);   //reference to the send button

//Button press event listener


}

public void startconnection(String barcodestring)
{
	_barcodestring=barcodestring;
	connection _con=new connection();
	_con.start();
}

public class connection extends Thread
{
	@Override
    public void run() 
	{
		//messsage = textField.getText().toString(); //get the text message on the text field
		//textField.setText("");      //Reset the text field to blank

		try {

		 client = new Socket("192.168.1.106", 4444);  //connect to server
		 printwriter = new PrintWriter(client.getOutputStream(),true);
		 printwriter.write(_barcodestring);  //write the message to output stream
		 printwriter.flush();
		 printwriter.close();
		 client.close();   //closing the connection

		} catch (UnknownHostException e) {
		 e.printStackTrace();
		} catch (IOException e) {
		 e.printStackTrace();
		}
	}
}
}
