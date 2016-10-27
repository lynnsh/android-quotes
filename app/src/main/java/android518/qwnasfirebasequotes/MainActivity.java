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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<String> aa;
    private DatabaseReference database;
    private String[] categories;
    private Random random;
    private int gen;
    private Quote current;
    private Quote last;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieving Shared Preferences
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);


        database = FirebaseDatabase.getInstance().getReference();
        random = new Random();

        ListView lv=(ListView) findViewById(R.id.cat_list);
        categories = getResources().getStringArray(R.array.categories);
        aa = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        lv.setAdapter(aa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                generateQuote(position);
            }
        });
    }

    private void generateQuote(int position) {
        database.child(categories[position]).addListenerForSingleValueEvent
            (new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int count = (int)dataSnapshot.child("attributed").getChildrenCount();
                    gen = random.nextInt(count);
                    String attr = dataSnapshot.child("attributed").child(gen+"").getValue().toString();
                    String blurb = dataSnapshot.child("blurb").child(gen+"").getValue().toString();
                    String q = dataSnapshot.child("quote").child(gen+"").getValue().toString();
                    String ref = dataSnapshot.child("reference").child(gen+"").getValue().toString();
                    String date = dataSnapshot.child("date").child(gen+"").getValue().toString();
                    current = new Quote(attr, blurb, q, ref, date);
                    sendQuote(current);


                    Log.d("--MAIN", current+"");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quote_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.last:
                //sendQuote(last); to do
                return true;
            case R.id.about:
                startActivity(new Intent(this, About.class));
                return true;
            case R.id.random:
                //to do
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void sendQuote(Quote q) {
        Intent i = new Intent(this, QuoteActivity.class);
        i.putExtra("quote", q);
        startActivity(i);
    }
}