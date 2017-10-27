package independent_study.beaker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private enum TOOL_ACTIVITIES {DILUTION_ACTIVITY, TITRATION_ACTIVITY}
    private static ArrayList<TOOL_ACTIVITIES> TOOL_LIST = new ArrayList<>();

    static
    {
        System.loadLibrary("native-lib");
        TOOL_LIST.add(TOOL_ACTIVITIES.DILUTION_ACTIVITY);
        TOOL_LIST.add(TOOL_ACTIVITIES.TITRATION_ACTIVITY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = (TextView) findViewById(R.id.textView);
        ListView listView = (ListView) findViewById(R.id.listView);

        ArrayAdapter<TOOL_ACTIVITIES> arrayAdapter = new ArrayAdapter<>(this, R.layout.list_text_view, TOOL_LIST);
        listView.setAdapter(arrayAdapter);

        //https://developer.android.com/reference/android/widget/AdapterView.OnItemClickListener.html
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Log.d("MainActivity", "onItemClick: " + i);
                switch((TOOL_ACTIVITIES) adapterView.getItemAtPosition(i))
                {
                    case DILUTION_ACTIVITY:
                        startActivity(new Intent(getApplicationContext(), DilutionActivity.class));
                        break;
                    case TITRATION_ACTIVITY:
                        startActivity(new Intent(getApplicationContext(), TitrationActivity.class));
                    default:
                        Log.d("MainActivity", "Default Click");
                        break;
                }
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
