package android518.qwnasfirebasequotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Random;

/**
 * Class responsible for showing the categories layout,
 * getting the information from Firebase,
 * and displaying Options menu.
 *
 * @author William Ngo
 * @author Alena Shulzhenko
 * @version 2016-11-05
 */
public class MainActivity extends AppCompatActivity {

    private DatabaseReference database;
    private FirebaseAuth firebaseAuth;
    private String[] categories;
    private Random random;
    private Quote currentQuote;
    private static final String TAG = "MAIN";

    /**
     * Lifecycle method. Signs in to Firebase and displays the list of categories.
     * @param savedInstanceState a Bundle object.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create new random object
        random = new Random();

        //reference to the Firebase database
        database = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        //sign in the app to Firebase
        firebaseAuth.signInWithEmailAndPassword("firebasedroid518@gmail.com","firebaseassignment");

        //get category names from Firebase
        getCategories();
    }

    /**
     * Creates the options menu from the specified layout.
     * @param menu the options menu in which the items are placed.
     * @return true to display the menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        //inflates menu using custom quote_menu layout
        getMenuInflater().inflate(R.menu.quote_menu, menu);
        return true;
    }

    /**
     * Called when the item in the menu is selected.
     * QuoteActivity is called when the last or random quote options are chosen,
     * and AboutActivity is called when the about option is chosen.
     * @param item the item that was selected.
     * @return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.last:
                sendQuote(retrieveLastQuote());
                return true;
            case R.id.about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.random:
                generateRandomQuote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Executed when the back button is clicked. The app is signed out from Firebase.
     */
    @Override
    public void finish() {
        firebaseAuth.signOut();
        Log.d(TAG, "in finish()");
        super.finish();
    }

    /**
     * Generates a random quote based on the category.
     * The quote is sent to the Quote Activity.
     * Saves the current quote to the disk.
     * @param position the index value of the requested category in Firebase database.
     */
    private void generateQuote(int position) {
        database.child(categories[position]).addListenerForSingleValueEvent
            (new ValueEventListener() {
                @Override
                /**
                 * Called each time data changes for this snapshot.
                 * @param dataSnapshot the current data at the location.
                 */
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //find the number of quotes in this category
                    int count = (int)dataSnapshot.child("attributed").getChildrenCount();
                    //generate a random integer
                    int generatedInt = random.nextInt(count);
                    currentQuote = createQuote(dataSnapshot, generatedInt);
                    saveCurrentQuote();
                    sendQuote(currentQuote);
                    Log.i(TAG, currentQuote.toString());
                }

                @Override
                //triggered if this listener either failed at the server, or
                // is removed as a result of the security and Firebase rules
                public void onCancelled(DatabaseError databaseError) {}
            });
    }

    /**
     * Creates s Quote object from the information in Firebase,
     * given its position (random generated integer).
     * @param dataSnapshot the current data at the location.
     * @param generatedInt the generated position of the quote that is created.
     * @return the created Quote from the information in Firebase.
     */
    private Quote createQuote(DataSnapshot dataSnapshot, int generatedInt) {
        String attr = dataSnapshot.child("attributed").child(generatedInt+"").getValue().toString();
        String blurb = dataSnapshot.child("blurb").child(generatedInt+"").getValue().toString();
        String q = dataSnapshot.child("quote").child(generatedInt+"").getValue().toString();
        String ref = dataSnapshot.child("reference").child(generatedInt+"").getValue().toString();
        String date = dataSnapshot.child("date").child(generatedInt+"").getValue().toString();
        return new Quote(attr, blurb, q, ref, date);
    }

    /**
     * Saves the current quote to the disk.
     */
    private void saveCurrentQuote() {
        //retrieving SharedPreferences
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        //converting the Quote object to JSON
        String json = gson.toJson(currentQuote);
        //saving the Quote as JSON
        editor.putString("quote", json);
        editor.commit();
    }

    /**
     * Send the provided Quote to QuoteActivity.
     * @param quote the Quote to send to QuoteActivity.
     */
    private void sendQuote(Quote quote) {
        Intent i = new Intent(this, QuoteActivity.class);
        //add Quote as Serializable object
        i.putExtra("quote", quote);
        //start QuoteActivity
        startActivity(i);
    }

    /**
     * Generates random category and based on that a random quote.
     * The quote is saved to disk and send to QuoteActivity.
     */
    private void generateRandomQuote() {
        int category = random.nextInt(categories.length);
        generateQuote(category);
    }

    /**
     * The last saved quoted is retrieved from the disk.
     * @return the last Quote that was saved to disk.
     */
    private Quote retrieveLastQuote() {
        //retrieving SharedPreferences
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        //retrieving the Quote, null if no quote is available
        String json = prefs.getString("quote", null);
        return gson.fromJson(json, Quote.class);
    }

    /**
     * Gets categories from Firebase and adds them to the list to display.
     */
    private void getCategories() {
        database.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            /**
             * Called each time data changes for this snapshot.
             * @param dataSnapshot the current data at the location.
             */
            public void onDataChange(DataSnapshot dataSnapshot) {
                //the number of categories in the database
                int size = (int)dataSnapshot.getChildrenCount();
                categories = new String[size];
                Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();
                int index = 0;
                //add categories to the array
                for(DataSnapshot d : iterator) {
                    categories[index] =  d.getKey();
                    index++;
                }
                //set up the list view to display the categories
                setListView();
            }

            @Override
            //triggered if this listener either failed at the server, or
            // is removed as a result of the security and Firebase rules
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    /**
     * Sets up the custom ListView with images to display the categories.
     */
    private void setListView() {
        ListView lv=(ListView) findViewById(R.id.cat_list);

        //array to hold id's of category images
        int[] images = {R.drawable.compsci, R.drawable.fiction, R.drawable.humour,
                        R.drawable.philosophy, R.drawable.tv};

        //set the adapter with image support
        lv.setAdapter(new CustomAdapter(this, R.layout.list, categories, images));

        //set the listener that generates a random quote
        //for the chosen(clicked) category
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            /**
             * Callback method to be invoked when an item in this AdapterView has been clicked.
             * @param parent the AdapterView where the click happened.
             * @param view 	the view within the AdapterView that was clicked.
             * @param position 	the position of the view in the adapter.
             * @param id 	the row id of the item that was clicked.
             */
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                generateQuote(position);
            }
        });
    }
}
