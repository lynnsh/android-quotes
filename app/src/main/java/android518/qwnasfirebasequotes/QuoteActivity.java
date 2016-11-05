package android518.qwnasfirebasequotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
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

    /**
     * Displays all information pertaining to a quote
     *
     * @param quote
     */
    private void displayQuote(final Quote quote)
    {
        try {

            TextView attr_textview = (TextView) findViewById(R.id.attributedTextView);
            TextView quote_textview = (TextView) findViewById(R.id.quoteTextView);
            TextView ref_textview = (TextView) findViewById(R.id.referenceTextView);
            TextView date_textview = (TextView) findViewById(R.id.dateTextView);

            attr_textview.setText("- " + quote.getAttributed());
            quote_textview.setText("\" " + quote.getQuote() + " \"");
            ref_textview.setText(quote.getReference());
            date_textview.setText(quote.getDate());

            //Linkify reference to allow user to open browser to the webpage
            Linkify.addLinks(ref_textview, Linkify.WEB_URLS);

            //Allow user to view the blurb by clicking on the attributed's name
            attr_textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayBlurb(quote);
                }
            });



            /*Add onClick event to open browser to the page of the reference of the quote
            ref_textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = quote.getReference();
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
            */
        }
        catch(NullPointerException npe)
        {
            npe.getMessage();
        }
    }

    /**
     * Displays a dialog box containing a blurb which is
     * information about the attributed person who said the quote
     *
     * @param quote - Quote object
     */
    private void displayBlurb(Quote quote)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(quote.getAttributed())
                .setMessage(quote.getBlurb())
                .setCancelable(true)
                .setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing, just dismiss the dialog
                    }
                });

        AlertDialog ad = builder.create();
        ad.show();
    }
}
