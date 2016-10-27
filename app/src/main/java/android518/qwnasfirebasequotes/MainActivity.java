package android518.qwnasfirebasequotes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    private Quote last;
    MainActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance().getReference();
        random = new Random();
        this.context = this;

        ListView lv=(ListView) findViewById(R.id.cat_list);
        categories = getResources().getStringArray(R.array.categories);
        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);
        lv.setAdapter(aa);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                generateQuote(position, id);
            }
        });
    }

    private void generateQuote(int position, long id) {
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
                    Quote last = new Quote(attr, blurb, q, ref, date);

                    Intent i = new Intent(context, QuoteActivity.class);
                    i.putExtra("quote", last);
                    startActivity(i);

                    Log.d("--MAIN", last+"");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }
}
