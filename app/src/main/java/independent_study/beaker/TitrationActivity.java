package independent_study.beaker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

public class TitrationActivity extends AppCompatActivity
{
    //http://chemed.chem.purdue.edu/genchem/topicreview/bp/ch17/water.php
    public static final double CONSTANT_OF_WATER_25C = 1.0 * Math.pow(10, -14);

    private enum CALCULATION_TYPE {BEFORE, EQUIV, AFTER, NONE}
    private CALCULATION_TYPE calculationType = CALCULATION_TYPE.NONE;
    private boolean isKPA = true;
    private boolean isKPForOriginal = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_titration);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        final EditText editTextKP = (EditText) findViewById(R.id.editTextKP);
        final EditText editTextML_T = (EditText) findViewById(R.id.editTextML_T);
        final EditText editTextMO_T = (EditText) findViewById(R.id.editTextMO_T);
        final EditText editTextML_O = (EditText) findViewById(R.id.editTextML_O);
        final EditText editTextMO_O = (EditText) findViewById(R.id.editTextMO_O);
        final EditText editTextPH = (EditText) findViewById(R.id.editTextPH);
        ToggleButton toggleButton_KP = (ToggleButton) findViewById(R.id.toggleButton_KP);
        ToggleButton toggleButtonWeak = (ToggleButton) findViewById(R.id.toggleButtonWeak);
        Button finalizeButton = (Button) findViewById(R.id.buttonFinalize);

        //https://stackoverflow.com/questions/8051069/how-to-show-image-using-imageview-in-android
        imageView.setImageResource(R.drawable.ic_titration);

        //https://stackoverflow.com/questions/11776423/android-togglebutton-listener
        toggleButton_KP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                isKPA = !b;
                Log.d("TitrationActivity", "Is KPA = " + isKPA);
            }
        });

        toggleButtonWeak.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                isKPForOriginal = !b;
                Log.d("TitrationActivity", "Is KP For Original = " + isKPForOriginal);
            }
        });

        //https://developer.android.com/guide/topics/ui/controls/radiobutton.html
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i)
            {
                Log.d("TitrationActivity", "onCheckChanged");
                switch(i)
                {
                    case R.id.radioButtonBefore:
                        calculationType = CALCULATION_TYPE.BEFORE;
                        Log.d("TitrationActivity", "Set to Before Equiv");
                        break;
                    case R.id.radioButtonEquiv:
                        calculationType = CALCULATION_TYPE.EQUIV;
                        Log.d("TitrationActivity", "Set to Equiv");
                        break;
                    case R.id.radioButtonAfter:
                        calculationType = CALCULATION_TYPE.AFTER;
                        Log.d("TitrationActivity", "Set to After Equiv");
                        break;
                    default:
                        Log.e("TitrationActivity", "Default onClick()");
                }
            }
        });

        finalizeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                double kp = Double.NaN;
                double ml_t = Double.NaN;
                double mo_t = Double.NaN;
                double ml_o = Double.NaN;
                double mo_o = Double.NaN;
                double ph = Double.NaN;

                try
                {
                    kp = !editTextKP.getText().toString().equals("") ? Double.parseDouble(editTextKP.getText().toString()) : Double.NaN;
                    ml_t = !editTextML_T.getText().toString().equals("") ? Double.parseDouble(editTextML_T.getText().toString()) : Double.NaN;
                    mo_t = !editTextMO_T.getText().toString().equals("") ? Double.parseDouble(editTextMO_T.getText().toString()) : Double.NaN;
                    ml_o = !editTextML_O.getText().toString().equals("") ? Double.parseDouble(editTextML_O.getText().toString()) : Double.NaN;
                    mo_o = !editTextMO_O.getText().toString().equals("") ? Double.parseDouble(editTextMO_O.getText().toString()) : Double.NaN;
                    ph = !editTextPH.getText().toString().equals("") ? Double.parseDouble(editTextPH.getText().toString()) : Double.NaN;
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    alertUser("Mal-formatted Number");
                }

                if(     (!Double.isNaN(kp) ? 1 : 0) +
                        (!Double.isNaN(ml_t) ? 1 : 0) +
                        (!Double.isNaN(mo_t) ? 1 : 0) +
                        (!Double.isNaN(ml_o) ? 1 : 0) +
                        (!Double.isNaN(mo_o) ? 1 : 0) +
                        (!Double.isNaN(ph) ? 1 : 0) == 5)
                {
                    switch(calculationType)
                    {
                        case BEFORE:
                        case EQUIV:
                        case AFTER:
                            solveTitration(calculationType, kp, ml_t, mo_t, ml_o, mo_o, ph, isKPA, isKPForOriginal);
                            break;
                        default:
                            alertUser("Select a Calculation Type");
                            break;
                    }
                }
                else
                {
                    alertUser("Not Enough Information Given");
                }
            }
        });
    }

    /**
     * Solves Titration Problems For a Double.NaN Variable
     * @param ct - Type of Calculation (see enum)
     * @param kp - KP (A or B)
     * @param ml_t - mL of Titrant
     * @param mo_t - Molarity of Titrant
     * @param ml_o - mL of Original
     * @param mo_o - Molarity of Original
     * @param ph - Current pH
     * @param isKPA - Is the Provided KP Set For Acids or Bases?
     * @param isKPForOriginal - Is the Provided KP For the Titrant or Original
     * @return The Value of the Missing Variable
     *
     * @see "https://en.wikipedia.org/wiki/Henderson%E2%80%93Hasselbalch_equation"
     * @see "https://en.wikipedia.org/wiki/Acid_dissociation_constant"
     * @see "https://chem.libretexts.org/Core/Physical_and_Theoretical_Chemistry/Acids_and_Bases/Ionization_Constants/Calculating_A_Ka_Value_From_A_Measured_Ph"
    */
    private double solveTitration(CALCULATION_TYPE ct,
                                  double kp, double ml_t, double mo_t,
                                  double ml_o, double mo_o,
                                  double ph, boolean isKPA, boolean isKPForOriginal)
    {
        if(Double.isNaN(kp))
        {
            //Current Moles of Titrant and Original
            double moles_t = (ml_t / 1000) * mo_t;
            double moles_o = (mo_o / 1000) * mo_o;

            if(ct == CALCULATION_TYPE.BEFORE)
            {
                //Theoretical Concentration of Moles of Original Remaining
                double mo_s_un = ((moles_o - moles_t) / (ml_o + ml_t) / 1000);
                //Molarity of Hydrogen
                double mo_h = (Math.pow(10, -ph));

                double kpa = -(mo_h * mo_h) / (mo_s_un - mo_h);
                Log.d("TitrationActivity", "KPA " + kpa);
                return (isKPA ? kpa : (CONSTANT_OF_WATER_25C / kpa));
            }
            else if(ct == CALCULATION_TYPE.EQUIV)
            {
                //Theoretical Concentration of Moles of Original Remaining
                double mo_s_un = 0;
                //Molarity of Hydrogen
                double mo_h = (Math.pow(10, -ph));

                double kpa = -(mo_h * mo_h) / (mo_s_un - mo_h);
                Log.d("TitrationActivity", "KPA " + kpa);
                return (isKPA ? kpa : (CONSTANT_OF_WATER_25C / kpa));
            }
            else if(ct == CALCULATION_TYPE.AFTER)
            {
                //Theoretical Concentration of Moles of Titrant Remaining
                double mo_s_un = ((moles_t - moles_o) / (ml_o + ml_t) / 1000);
                //Molarity of Hydrogen
                double mo_h = (Math.pow(10, -ph));

                double kpa = -(mo_h * mo_h) / (mo_s_un - mo_h);
                Log.d("TitrationActivity", "KPA " + kpa);
                return (kpa);
            }
        }
        else if(Double.isNaN(ml_t))
        {

        }
        else if(Double.isNaN(mo_t))
        {

        }
        else if(Double.isNaN(ml_o))
        {

        }
        else if(Double.isNaN(mo_o))
        {

        }
        else if(Double.isNaN(ph))
        {
            //Current Moles of Titrant and Original
            double moles_t = (ml_t / 1000) * mo_t;
            double moles_o = (mo_o / 1000) * mo_o;

            if(ct == CALCULATION_TYPE.BEFORE)
            {
                /*
                //Theoretical Concentration of Moles of Original Remaining
                double mo_s_un = ((moles_o - moles_t) / (ml_o + ml_t) / 1000);

                //Molarity of Hydrogen
                //https://chem.libretexts.org/Core/Analytical_Chemistry/Lab_Techniques/Titration/Titration_of_a_Weak_Base_with_a_Strong_Acid
                double mo_h = (mo_s_un + Math.sqrt(Math.pow(mo_s_un, 2) - 4 * (-1) * (mo_s_un / ))) / (2 * -1);

                double ph_real = Math.log10(-mo_h);
                */

                double mo_h_eq = (-kp + Math.sqrt((kp * kp) + 4 * (kp * mo_o))) / 2;

                Log.d("TitrationActivity", "MO_H_EQ " + mo_h_eq);

                double ph_eq = Math.log10(mo_h_eq);

                Log.d("TitrationActivity", "PH " + ph_eq);
                return (ph_eq);
            }

            if(ct == CALCULATION_TYPE.EQUIV)
            {
                //Molarity of Hydrogen = kp @ Equilibrium Point
                double mo_h = kp;
                double ph_real = Math.log10(-mo_h);

                Log.d("TitrationActivity", "PH " + ph_real);
                return (ph_real);
            }

            if(ct == CALCULATION_TYPE.AFTER)
            {
                //Theoretical Concentration of Moles of Titrant Remaining
                double mo_s_un = ((moles_t - moles_o) / (ml_o + ml_t) / 1000);

                //Molarity of Hydrogen
                double mo_h = (Math.pow(10, -ph));

                double kpa = -(mo_h * mo_h) / (mo_s_un - mo_h);
                Log.d("TitrationActivity", "PH " + kpa);
                //return (ph_real);
            }
        }
        else
        {
            alertUser("Something REALLY BAD just happened to Math");
        }

        //TODO: Finish and Replace
        return Double.NaN;
    }

    /**
     * Generates Pop-Up Dialogues
     * @param message - Message to be Appears as a "Pop-up" to the User
     * @see "https://stackoverflow.com/questions/26097513/android-simple-alert-dialog"
     */
    private void alertUser(String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(TitrationActivity.this).create();
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
