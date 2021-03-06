package com.example.stanl.gcorganizer;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.sqlcipher.database.SQLiteDatabase;

public class AddCardActivity extends AppCompatActivity {

    private View card_number_view;
    private View store_name_view;
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    private Bundle cardinfo_bundle;
    private Card card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteDatabase.loadLibs(this);
        setContentView(R.layout.activity_add_card);

        card_number_view = findViewById(R.id.edit_card_number);
        ((TextView) card_number_view.findViewById(R.id.title)).setText("Card Number");

        store_name_view = findViewById(R.id.edit_store_name);
        ((TextView) store_name_view.findViewById(R.id.title)).setText("Store Name");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cardinfo_bundle = this.getIntent().getBundleExtra(EXTRA_MESSAGE);
        if (cardinfo_bundle != null) {
            ((Button) findViewById(R.id.addbutton)).setText(R.string.button_addnewcard);
            ((Button) findViewById(R.id.updatebutton)).setText(R.string.button_updatecard);
            ((Button) findViewById(R.id.deletebutton)).setText(R.string.button_deletecard);
            card = cardinfo_bundle.getParcelable("Card");
            ((EditText) card_number_view.findViewById(R.id.value)).setText(card.card_number);
            ((EditText) store_name_view.findViewById(R.id.value)).setText(card.store_name);
        }
        else {
            findViewById(R.id.updatebutton).setVisibility(View.GONE);
            findViewById(R.id.deletebutton).setVisibility(View.GONE);
            ((Button) findViewById(R.id.deletebutton)).setText(R.string.button_deletecard);
            ((EditText) card_number_view.findViewById(R.id.value)).setHint(R.string.add_card_number);
            ((EditText) store_name_view.findViewById(R.id.value)).setHint(R.string.add_store_name);
        }
    }

    public void AddCard(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        Toast.makeText(getApplicationContext(), "Card Added!", Toast.LENGTH_LONG).show();
        ContentValues values = this.generate_content();
        getContentResolver().insert(DBContentProvider.CONTENT_URI, values);
        startActivity(intent);
    }

    public void UpdateCard(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        Toast.makeText(getApplicationContext(), "Card Updated!", Toast.LENGTH_LONG).show();
        ContentValues values = this.generate_content();
        Uri row_uri = DBContentProvider.CONTENT_URI.buildUpon().appendPath(String.valueOf(card._id)).build();
        getContentResolver().update(row_uri, values, null, null);
        startActivity(intent);
    }


    public void DeleteCard(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        Toast.makeText(getApplicationContext(), "Card Deleted!", Toast.LENGTH_LONG).show();
        ContentValues values = this.generate_content();
        Uri row_uri = DBContentProvider.CONTENT_URI.buildUpon().appendPath(DBHandler.TABLE_CARDS).
                appendPath(String.valueOf(card._id)).build();
        getContentResolver().delete(row_uri, null, null);
        startActivity(intent);
    }

    private ContentValues generate_content() {
        ContentValues values = new ContentValues();
        EditText card_number = (EditText) card_number_view.findViewById(R.id.value);
        EditText store_name = (EditText) store_name_view.findViewById(R.id.value);

        String card_number_msg = card_number.getText().toString();
        String store_name_msg = store_name.getText().toString();

        values.put(DBHandler.STORE_NAME, card_number_msg);
        values.put(DBHandler.CARD_NUMBER, store_name_msg);
        return values;
    }
}
