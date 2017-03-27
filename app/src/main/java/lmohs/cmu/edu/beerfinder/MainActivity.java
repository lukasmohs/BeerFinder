package lmohs.cmu.edu.beerfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MainActivity callback = this;
        new Connection().getBeer(this);
    }

    public void onDownloadReady(String res){
        System.out.println(res);
        ((TextView)findViewById(R.id.textView)).setText(res);
    }
}
