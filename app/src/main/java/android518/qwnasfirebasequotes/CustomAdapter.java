package android518.qwnasfirebasequotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Class responsible for creating a custom ListAdapter.
 * It displays both an image and text for element in list.
 *
 * @author William Ngo
 * @author Alena Shulzhenko
 * @version 2016-11-05
 */
public class CustomAdapter extends BaseAdapter {
    private String[] categories;
    private int[] images;
    private int layout;
    private final LayoutInflater inflater;

    /**
     * Instantiates the CustomAdapter object.
     * @param mainActivity the reference to the MainActivity.
     * @param layout the layout for one element of the list.
     * @param categories the array with categories names.
     * @param images the array with id's for images corresponding to categories.
     */
    public CustomAdapter(MainActivity mainActivity, int layout, String[] categories, int[] images) {
        this.categories = categories;
        this.images = images;
        this.layout = layout;

        MainActivity context = mainActivity;
        inflater = LayoutInflater.from(context);
    }

    /**
     * Returns the number of elements in the adapter.
     * @return the number of elements in the adapter.
     */
    @Override
    public int getCount() {
        return categories.length;
    }

    /**
     * Returns the item in the list at the specified position.
     * @param i the position of the item.
     * @return the item in the list at the specified position.
     */
    @Override
    public Object getItem(int i) {
        return categories[i];

    }

    /**
     * Get the row id associated with the specified position in the list.
     * @param i the position of the item within the adapter.
     * @return the id of the item at the specified position.
     */
    @Override
    public long getItemId(int i) {
        return images[i];
    }

    /**
     * Gets the view that displays the data at the specified position.
     * @param position the position of the item within the adapter.
     * @param view the old view to reuse.
     * @param parent the parent that this view will eventually be attached to.
     * @return a View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        //inflate the view element with the given layout
        View rowView = inflater.inflate(layout, parent, false);
        rowView.setId(position);
        TextView tv = (TextView) rowView.findViewById(R.id.tv);
        ImageView img = (ImageView) rowView.findViewById(R.id.img);
        //set text of the view
        tv.setText(categories[position]);
        //set image if the view
        img.setImageResource(images[position]);
        return rowView;
    }
}
