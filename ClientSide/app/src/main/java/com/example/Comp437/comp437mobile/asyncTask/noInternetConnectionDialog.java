package com.example.kwoksinman.comp437mobile.asyncTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * Created by KwokSinMan on 23/2/2016.
 */
public class noInternetConnectionDialog extends DialogFragment implements DialogInterface.OnClickListener {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TextView messageTextView =  new TextView(getActivity());
        messageTextView.setText("Make sure the phone has a good network signal");
        messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        messageTextView.setPadding(0, 30, 0, 30);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Internet connection is not connected")
                .setView(messageTextView)
                .setPositiveButton("確定", null)
                .create();
    }
    @Override
    public void onClick(DialogInterface dialog, int whichButton){}
}