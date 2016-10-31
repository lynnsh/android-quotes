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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<String> aa;
    private DatabaseReference database;
    private FirebaseAuth firebaseAuth;
    private String[] categories;
    private Random random;
    private Quote current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        random = new Random();

        database = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword("firebasedroid518@gmail.com","firebaseassignment");

        getCategories();
        //categories = getResources().getStringArray(R.array.categories);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(current);
        editor.putString("quote", json);
        editor.commit();
    }

    @Override
    public void onStop() {
        super.onStop();
        //firebaseAuth.signOut();
    }

    private void generateQuote(int position) {
        database.child(categories[position]).addListenerForSingleValueEvent
            (new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int count = (int)dataSnapshot.child("attributed").getChildrenCount();
                    int gen = random.nextInt(count);
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
                public void onCancelled(DatabaseError databaseError) {}
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
                sendQuote(retrieveLastQuote());
                return true;
            case R.id.about:
                startActivity(new Intent(this, About.class));
                return true;
            case R.id.random:
                generateRandomQuote();
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

    private void generateRandomQuote() {
        int category = random.nextInt(categories.length);
        generateQuote(category);
    }

    private Quote retrieveLastQuote() {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("quote", "");
        return gson.fromJson(json, Quote.class);
    }

    private void getCategories() {
        database.addListenerForSingleValueEvent
                (new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int size = (int)dataSnapshot.getChildrenCount();
                        categories = new String[size];
                        Iterable<DataSnapshot> iterator = dataSnapshot.getChildren();
                        int index = 0;
                        for(DataSnapshot d : iterator) {
                            categories[index] =  d.getKey();
                            index++;
                            Log.d("MAIN", categories[index-1]);
                        }
                        setListView();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    private void setListView() {
        ListView lv=(ListView) findViewById(R.id.cat_list);

        int[] images = {R.drawable.compsci, R.drawable.fiction, R.drawable.humour,
                        R.drawable.philosophy, R.drawable.tv};

        lv.setAdapter(new CustomAdapter(this, R.layout.list, categories, images));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                generateQuote(position);
            }
        });
    }
}
