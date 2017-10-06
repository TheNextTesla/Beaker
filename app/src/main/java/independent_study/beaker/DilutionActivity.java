package independent_study.beaker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DilutionActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dilution);

        final EditText editTextM1 = (EditText) findViewById(R.id.editTextM1);
        final EditText editTextV1 = (EditText) findViewById(R.id.editTextV1);
        final EditText editTextM2 = (EditText) findViewById(R.id.editTextM2);
        final EditText editTextV2 = (EditText) findViewById(R.id.editTextV2);
        Button buttonSolve = (Button) findViewById(R.id.buttonSolve);

        buttonSolve.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(editTextM1.getText().equals(""))
                {

                }
                else if(editTextV1.getText().equals(""))
                {

                }
                else if(editTextM2.getText().equals(""))
                {

                }
                else if(editTextV2.getText().equals(""))
                {

                }
                else
                {
                    //https://stackoverflow.com/questions/26097513/android-simple-alert-dialog
                    AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("All Entries Were Detected to Be Filled");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });
    }
}
