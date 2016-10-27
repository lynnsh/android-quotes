package android518.qwnasfirebasequotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class QuoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            Quote q = (Quote)b.getSerializable("quote");
            Log.d("QUOTE", q.toString());
        }
    }
}
