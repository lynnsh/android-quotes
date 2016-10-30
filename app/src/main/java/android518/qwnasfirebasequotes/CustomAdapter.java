package android518.qwnasfirebasequotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by aline on 30/10/16.
 */

public class CustomAdapter extends BaseAdapter {
    private final MainActivity context;
    private String[] categories;
    private int[] images;
    private int layout;
    private final LayoutInflater inflater;

    public CustomAdapter(MainActivity mainActivity, int layout, String[] categories, int[] images) {
        this.context = mainActivity;
        this.categories = categories;
        this.images = images;
        this.layout = layout;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return categories.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View rowView = inflater.inflate(layout, parent, false);
        rowView.setId(position);
        TextView tv = (TextView) rowView.findViewById(R.id.tv);
        ImageView img = (ImageView) rowView.findViewById(R.id.img);
        tv.setText(categories[position]);
        img.setImageResource(images[position]);
        return rowView;
    }
}
