package com.example.edward.todo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView lvItems;

    private TodoListDbHelper dbHelper = new TodoListDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // a line of code that you pretty much always do in onCreate, idk what it does
        super.onCreate(savedInstanceState);
        // set the view to the main layout (see mobile/res/layout/activity_main.xml)
        setContentView(R.layout.activity_main);

        // initialize lvItems by setting it to the correct view in the xml file (activity_main.xml)
        lvItems = (ListView)findViewById(R.id.lvItems);

        // create a new ArrayList of Strings that will hold our list items
        items = new ArrayList<String>();

        // Adapter will create the actual thing that is displayed on the screen
        itemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, items);
        // set the list view adapter to itemsAdapter so the list view will display the data
        // in the adapter
        lvItems.setAdapter(itemsAdapter);

        // get the list of items from the database
        getListItems();

        // add the long-touch to delete function to the listView in a separate method
        setupListViewListener();
    }

    // Attaches a long click listener to the list View
    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        // Remove the item within array at position
                        items.remove(pos);
                        // Refresh the adapter
                        itemsAdapter.notifyDataSetChanged();
                        // Return true consumes the long click event (marks it handled)
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View v) {
        // get the EditText item in the main layout called etNewItem
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        // set the item text to the text in the field
        String itemText = etNewItem.getText().toString();

        // get the database in write mode
        // TODO: call this function asynchronously because it is slow!
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // create a new map of values, where column names are keys
        ContentValues values = new ContentValues();
        values.put(TodoListDbHelper.COLUMN_NAME_DESCRIPTION, itemText);
        // insert row
        db.insert(TodoListDbHelper.TABLE_NAME, null, values);
        db.close(); // Close database connection

        // refresh the list of items to include the new item
        getListItems();

        // clear the text field by setting it to an empty string
        etNewItem.setText("");
    }

    public void getListItems()
    {
        // select all query
        String selectQuery = "SELECT * FROM " + TodoListDbHelper.TABLE_NAME;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
        {
            while (c.moveToNext())
            {
                if (c.getColumnIndex(TodoListDbHelper.COLUMN_NAME_DESCRIPTION) > -1)
                {
                    String item = c.getString(c.getColumnIndex(TodoListDbHelper.COLUMN_NAME_DESCRIPTION));
                    items.add(item);
                }
            }
            c.moveToNext();
            itemsAdapter.notifyDataSetChanged();
        }
    }
}
