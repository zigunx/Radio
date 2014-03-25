package com.example.radio;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class DialogScreen {
	
	public static final int IDD_TOKEN = 1;
    public static final int IDD_STATION = 2;

    public static AlertDialog getDialog(Activity activity, int ID, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        switch(ID) {
        case IDD_TOKEN: 
            builder.setTitle(R.string.dialog_title);
            builder.setMessage(message);
            builder.setCancelable(true);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { 
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss(); 
                }
            });
            return builder.create();
            
        case IDD_STATION: 
            builder.setTitle(R.string.dialog_title);
            builder.setMessage(message);
            builder.setCancelable(true);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    
                    dialog.dismiss();
                }
            });
            return builder.create();
            
    		default:
            return null;
        }

    }
}
