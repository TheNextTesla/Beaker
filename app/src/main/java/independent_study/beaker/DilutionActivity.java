package independent_study.beaker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

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
                if(editTextM1.getText().toString().equals(""))
                {
                    if(!(editTextV1.getText().toString().equals("") || editTextM2.getText().toString().equals("") || editTextV2.getText().toString().equals("")))
                    {
                        try
                        {
                            double v1 = Double.parseDouble(editTextV1.getText().toString());
                            double m2 = Double.parseDouble(editTextM2.getText().toString());
                            double v2 = Double.parseDouble(editTextV2.getText().toString());
                            editTextM1.setText(String.format(Locale.US, "%5f", (m2 * v2 / v1)));
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            alertUser("Invalid Text!");
                        }
                    }
                    else
                    {
                        alertUser("Not all of the other required entries were filled");
                    }
                }
                else if(editTextV1.getText().toString().equals(""))
                {
                    if(!(editTextM1.getText().toString().equals("") || editTextM2.getText().toString().equals("") || editTextV2.getText().toString().equals("")))
                    {
                        try
                        {
                            double m1 = Double.parseDouble(editTextM1.getText().toString());
                            double m2 = Double.parseDouble(editTextM2.getText().toString());
                            double v2 = Double.parseDouble(editTextV2.getText().toString());
                            editTextV1.setText(String.format(Locale.US, "%5f", (m2 * v2 / m1)));
                        }
                        catch(Exception ex)
                        {
                            ex.printStackTrace();
                            alertUser("Invalid Text!");
                        }
                    }
                    else
                    {
                        alertUser("Not all of the other required entries were filled");
                    }
                }
                else if(editTextM2.getText().toString().equals(""))
                {
                    if(!(editTextM1.getText().toString().equals("") || editTextV1.getText().toString().equals("") || editTextV2.getText().toString().equals("")))
                    {
                        try
                        {
                            double v1 = Double.parseDouble(editTextV1.getText().toString());
                            double m1 = Double.parseDouble(editTextM1.getText().toString());
                            double v2 = Double.parseDouble(editTextV2.getText().toString());
                            editTextM2.setText(String.format(Locale.US, "%5f", (m1 * v1 / v2)));
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            alertUser("Invalid Text!");
                        }
                    }
                    else
                    {
                        alertUser("Not all of the other required entries were filled");
                    }
                }
                else if(editTextV2.getText().toString().equals(""))
                {
                    if(!(editTextM1.getText().toString().equals("") || editTextV1.getText().toString().equals("") || editTextM2.getText().toString().equals("")))
                    {
                        try
                        {
                            double v1 = Double.parseDouble(editTextV1.getText().toString());
                            double m1 = Double.parseDouble(editTextM1.getText().toString());
                            double m2 = Double.parseDouble(editTextM2.getText().toString());
                            editTextV2.setText(String.format(Locale.US, "%5f", (m1 * v1 / m2)));
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                            alertUser("Invalid Text!");
                        }
                    }
                    else
                    {
                        alertUser("Not all of the other required entries were filled");
                    }
                }
                else
                {
                    alertUser("All of the User Entries Were Detected to Be Filled");
                }
            }
        });
    }

    /**
     * Generates Pop-Up Dialogues
     * @param message - Message to be Appears as a "Pop-up" to the User
     * @see "https://stackoverflow.com/questions/26097513/android-simple-alert-dialog"
     */
    private void alertUser(String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(DilutionActivity.this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(message);
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
