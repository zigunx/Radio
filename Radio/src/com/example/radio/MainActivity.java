package com.example.radio;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.radio.entity.ResponseInfo;
import com.example.radio.parser.TokenParser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	
	private static final String KEY_OK = "0";
	private static final String KEY_ERROR = "1";
	public static final String KEY_TOKEN = "token";
	
	private  String user = "";
	private  String password = "";
	private  String url = "";//"http://android-course.comli.com/login.php?username=student&password=student";
	
	ResponseInfo info;
	
	EditText editTextUser;
	EditText editTextPassword;
	Button btnSet;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        editTextUser = (EditText) findViewById(R.id.editUser);
        editTextPassword = (EditText) findViewById(R.id.editPassword);
        btnSet = (Button) findViewById(R.id.btnSet);
        btnSet.setOnClickListener(new MyListener());
        
        //LoaderTask task = new LoaderTask();

    }
    
    public class LoaderTask extends AsyncTask<String, Void, ResponseInfo>{

		@Override
		protected ResponseInfo doInBackground(String... params) {
			HttpGet httpRequest = null;

            httpRequest = new HttpGet(params[0]);

            HttpClient httpclient = new DefaultHttpClient();
            
            try {
				HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
				HttpEntity entity = response.getEntity();
				InputStream stream = entity.getContent();
				info = new TokenParser().parse(stream);
			} catch (ClientProtocolException e) {			
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
            
			return info;
		}
		
		@Override
	    protected void onPostExecute(ResponseInfo result) {
	      super.onPostExecute(result);
	      
	      Log.i("DEBUG", result.getCode());
	      Log.i("DEBUG", result.getToken());
	      
	      if (result.getCode().equals(KEY_OK)){
	    	  Intent intent = new Intent(getApplicationContext(), ListActivity.class);
	    	  intent.putExtra(KEY_TOKEN, result.getToken());
	    	  startActivity(intent);
	      } else if (result.getCode().equals(KEY_ERROR)){
	    	  
	    	  AlertDialog dialog = DialogScreen.getDialog(MainActivity.this, DialogScreen.IDD_TOKEN, result.getMessage());
	          dialog.show();
	    	 
	      }
	      
    	}
    	
    }
    
    private class MyListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.btnSet:
				user = editTextUser.getText().toString();
				password = editTextPassword.getText().toString();
				if (user.length() == 0) editTextUser.setError("Please enter your user name");
				else if (password.length() < 6) editTextPassword.setError ("Password must be not less 6 symbol");
				else {
					url = "http://android-course.comli.com/login.php?username=" + user + "&password=" + password;
					new LoaderTask().execute(url);
				}
				break;
			}
			
		}
    	
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
