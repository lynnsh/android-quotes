package android518.qwnasfirebasequotes;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

public class QuoteActivity extends AppCompatActivity {
    Quote q = new Quote("no data", "no data", "no data", "no data", "no data");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            q = (Quote)b.getSerializable("quote");
            //q is null if no last quote was saved
            if(q != null)
                Log.d("QUOTE", q.toString());
        }

        displayQuote(q);
    }

    private void displayQuote(Quote quote)
    {
        TextView attr_textview = (TextView) findViewById(R.id.attributedTextView);
        TextView quote_textview = (TextView) findViewById(R.id.quoteTextView);
        TextView ref_textview = (TextView) findViewById(R.id.referenceTextView);
        TextView date_textview = (TextView) findViewById(R.id.dateTextView);

        attr_textview.setText(quote.getAttributed());
        quote_textview.setText(quote.getQuote());
        ref_textview.setText(quote.getReference());
        date_textview.setText(quote.getDate());
    }

    private void displayBlurb()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(q.getAttributed())
                .setMessage(q.getBlurb())
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //take out alert
                    }
                });
    }
}
