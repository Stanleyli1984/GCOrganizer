package com.example.stanl.gcorganizer;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DisplayCardActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    private Bundle cardinfo_bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_card);

        //TextView textView = new TextView(this);
        //textView.setTextSize(40);

        cardinfo_bundle = this.getIntent().getBundleExtra(EXTRA_MESSAGE);
        if (cardinfo_bundle != null) {
            Card card = cardinfo_bundle.getParcelable("Card");
            View store_name_view = findViewById(R.id.dis_store_name);
            ((TextView) store_name_view.findViewById(R.id.title1)).setText("Store Name");
            ((TextView) store_name_view.findViewById(R.id.value1)).setText(card.store_name);
            View card_number_view = findViewById(R.id.dis_card_number);
            ((TextView) card_number_view.findViewById(R.id.title1)).setText("Card Number");
            ((TextView) card_number_view.findViewById(R.id.value1)).setText(card.card_number);
        }
    }

    public void EditCard(View view) {
        Intent intent = new Intent(getApplicationContext(), AddCardActivity.class);
        intent.putExtra(EXTRA_MESSAGE, cardinfo_bundle);
        startActivity(intent);
    }
}
