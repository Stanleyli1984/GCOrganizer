package com.example.stanl.gcorganizer;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
//import android.content.CursorLoader;
//import android.content.Loader;
//import android.app.LoaderManager;
//import android.widget.SimpleCursorAdapter;

//import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

public class MainActivity extends FragmentActivity
implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";

    private SimpleCursorAdapter dataAdapter;
    private EditText filterText = null;
    private static Uri db_uri;
    private static Uri db_uri_raw_query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteDatabase.loadLibs(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here
                startActivity(new Intent(MainActivity.this, AddCardActivity.class));
            }
        });

        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                .setAction("Action", null).show();
        //    }
        //});

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        filterText = (EditText)findViewById(R.id.edit_message);
        filterText.addTextChangedListener(filterTextWatcher);

        display_table();
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
        }

        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            //String whereClause = DBHandler.STORE_NAME + " LIKE ? OR " +
            //        DBHandler.CARD_NUMBER + " LIKE ?";
            //String[] whereArgs = new String[] {
            //        "%" + s.toString() + "%",
            //        "%" + s.toString() + "%"
            //};

            // "SELECT * FROM mytable WHERE REPLACE(username, ' ', '') LIKE '%TO_QUERY%' ;"
            String rawClause = String.format("SELECT * FROM %1$s WHERE REPLACE(%2$s, ' ', '')" +
                    " LIKE '%%%4$s%%' OR REPLACE(%3$s, ' ', '') LIKE '%%%4$s%%'",
                    DBHandler.TABLE_CARDS, DBHandler.STORE_NAME, DBHandler.CARD_NUMBER,
                    s.toString());
            Cursor cur = getContentResolver().query(db_uri_raw_query, null, rawClause, null, null);
            dataAdapter.changeCursor(cur);
        }
    };

    private void display_table() {
        ListView tl = (ListView) findViewById(R.id.main_table);
        tl.setTextFilterEnabled(true);

        // Need to be the same as the column names in DB
        String[] columns = new String[] {
                "card_number",
                "store_name"
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.card_number,
                R.id.store_name,
        };

        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.main_dis_card_row_layout,
                null,
                columns,
                to,
                0);

        //dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
        //    public Cursor runQuery(CharSequence constraint) {
                //db_uri, projection, and sortOrder might be the same as previous
                //but you might want a new selection, based on your filter content (constraint)
                //String whereClause = DBHandler.STORE_NAME + " LIKE '%?%' OR " +
                //        DBHandler.CARD_NUMBER + " LIKE '%?%'";
                //String[] whereArgs = new String[] {
                //        constraint.toString(),
                //        constraint.toString()
                //};
                //Cursor cur = getContentResolver().query(db_uri, null, whereClause, whereArgs, null);
                //dataAdapter.changeCursor(cur);
                //return cur;
            //}
        //});
        tl.setAdapter(dataAdapter);

        tl.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                Card card = new Card();
                card._id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                card.card_number = cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.CARD_NUMBER));
                card.store_name = cursor.getString(cursor.getColumnIndexOrThrow(DBHandler.STORE_NAME));
                Intent intent = new Intent(getApplicationContext(), DisplayCardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("Card", card);
                intent.putExtra(EXTRA_MESSAGE, bundle);
                startActivity(intent);
                // Get the state's capital from this row in the database.
                //String countryCode =
                //        cursor.getString(cursor.getColumnIndexOrThrow("code"));
                //Toast.makeText(getApplicationContext(),
                  //      countryCode, Toast.LENGTH_SHORT).show();
            }
        });
        // Prepare the loader.  Either re-connect with an existing one,
        // or start a new one.
        getSupportLoaderManager().initLoader(0, null, this);
    }

    // TODO: pass in search args from here in future
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // This is called when a new Loader needs to be created.  This
        // sample only has one Loader, so we don't care about the ID.
        // First, pick the base URI to use depending on whether we are
        // currently filtering.

        return new CursorLoader(this,
                db_uri,
                null,
                null,
                null,
                null);
    }

    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        dataAdapter.swapCursor(null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        dataAdapter.swapCursor(data);
        // The list should now be shown.
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        filterText.removeTextChangedListener(filterTextWatcher);
    }

    static {
        Uri.Builder builder = new Uri.Builder();
        db_uri = builder.scheme("content").authority(DBContentProvider.AUTHORITY).
                appendPath(DBHandler.TABLE_CARDS).build();
        Uri.Builder builder1 = new Uri.Builder();
        db_uri_raw_query = builder1.scheme("content").authority(DBContentProvider.AUTHORITY).
                appendPath(DBHandler.TABLE_CARDS).appendPath(DBHandler.RAW_QUERY).build();
    }

}
