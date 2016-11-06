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

/**
 * This activity displays all the information pertaining to
 * a quote that has been passed from the MainActivity.
 *
 * @author William Ngo
 * @author Alena Shulzhenko
 * @version 2016-11-04
 */
public class QuoteActivity extends AppCompatActivity {

    /**
     * Lifecycle method. Gets the quote from the MainActivity.
     * @param savedInstanceState a Bundle object.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        //get the quote from the MainActivity
        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            Quote quote = (Quote)b.getSerializable("quote");
            if(quote != null)
                displayQuote(quote);
            //q is null if no last quote was saved
            else
                displayNoData();
        }
        else
            displayNoData();
    }

    /**
     * Displays all information pertaining to a quote.
     *
     * @param quote the quote which information is displayed.
     */
    private void displayQuote(final Quote quote)
    {
        //Get all textviews
        TextView attr_textview = (TextView) findViewById(R.id.attributedTextView);
        TextView quote_textview = (TextView) findViewById(R.id.quoteTextView);
        TextView ref_textview = (TextView) findViewById(R.id.referenceTextView);
        TextView date_textview = (TextView) findViewById(R.id.dateTextView);

        //Set all fields of a quote to the textviews
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
    }

    /**
     * Displays a dialog box containing a blurb which is
     * information about the attributed person who said the quote.
     *
     * @param quote Quote object
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
                        //Do nothing, just dismiss the dialog since this is a NegativeButton
                    }
                });

        builder.create().show();
    }

    /**
     * Displays no data warning if there was no Quote saved from the last run.
     */
    private void displayNoData() {
        TextView quote_textview = (TextView) findViewById(R.id.quoteTextView);
        quote_textview.setText(getResources().getString(R.string.nodata));
    }
}
