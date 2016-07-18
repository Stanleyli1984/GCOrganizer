package com.example.stanl.gcorganizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import net.sqlcipher.database.SQLiteDatabase;

public class AddCardActivity extends AppCompatActivity {

    private View card_number_view;
    private View store_name_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteDatabase.loadLibs(this);
        setContentView(R.layout.activity_add_card);

        card_number_view = findViewById(R.id.edit_card_number);
        ((TextView) card_number_view.findViewById(R.id.title)).setText("Card Number");
        ((EditText) card_number_view.findViewById(R.id.value)).setHint(R.string.add_card_number);

        store_name_view = findViewById(R.id.edit_store_name);
        ((TextView) store_name_view.findViewById(R.id.title)).setText("Store Name");
        ((EditText) store_name_view.findViewById(R.id.value)).setHint(R.string.add_store_name);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void AddCard(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText card_number = (EditText) card_number_view.findViewById(R.id.value);
        EditText store_name = (EditText) store_name_view.findViewById(R.id.value);

        String card_number_msg = card_number.getText().toString();
        String store_name_msg = store_name.getText().toString();

        intent.putExtra("card_number", card_number_msg);
        intent.putExtra("store_name", store_name_msg);
        startActivity(intent);
    }

}
