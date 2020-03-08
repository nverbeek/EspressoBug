package com.example.espressobug;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private AlertDialog mSimpleDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // simple button that launches a dialog
        Button button = findViewById(R.id.showDialogButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSimpleDialog();
            }
        });
    }

    /**
     * Shows a dialog containing a TextInputLayout
     * Enter something other than 1234 to get an error to show
     * Run 'TextInputErrorTest' - it will hang forever repeating these steps
     */
    public void showSimpleDialog()
    {
        // make extra sure the dialog is gone before showing it again
        if (mSimpleDialog != null && mSimpleDialog.isShowing())
        {
            mSimpleDialog.dismiss();
        }

        final AlertDialog.Builder lDialogBuilder = new AlertDialog.Builder(MainActivity.this);

        // set the dialog's content to a custom view
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.instruction_simple_dialog, new LinearLayout(this), false);
        lDialogBuilder.setView(convertView);

        final EditText lDataEntry = convertView.findViewById(R.id.data_entry);

        final TextInputLayout lDataEntryLayout = convertView.findViewById(R.id.data_entry_layout);

        // if we have a hint, then we want to set that text.
        lDataEntryLayout.setHint("Enter 1234, or something else for an error");

        // get and set buttons
        Button lButton0 = convertView.findViewById(R.id.button_0);

        lButton0.setVisibility(View.VISIBLE);
        lButton0.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(lDataEntry.getText().toString().equals("1234"))
                {
                    mSimpleDialog.dismiss();
                }
                else
                {
                    ////////////////////////////////////////////////////////////////////////////
                    // This line causes the hang in the test. For some reason, setting the error
                    //   will cause the espresso button click to hang forever
                    // I have tracked this down to Tap.java, line 170:
                    //   if (!MotionEvents.sendUp(uiController, res.down)) - this never returns
                    ////////////////////////////////////////////////////////////////////////////
                    lDataEntryLayout.setError("You entered the wrong text!");
                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        lDialogBuilder.setCancelable(false);
        mSimpleDialog = lDialogBuilder.create();
        mSimpleDialog.setCanceledOnTouchOutside(false);
        mSimpleDialog.show();
    }
}
