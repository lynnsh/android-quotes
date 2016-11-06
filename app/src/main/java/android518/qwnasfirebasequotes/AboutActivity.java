package android518.qwnasfirebasequotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Activity responsible for displaying the about page
 *
 * @author William Ngo
 * @author Alena Shulzhenko
 * @version 2016-11-05
 */
public class AboutActivity extends AppCompatActivity {

    /**
     * Overriden lifecycle method. Sets the layout.
     * @param savedInstanceState a Bundle object.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}
