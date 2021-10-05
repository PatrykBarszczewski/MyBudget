package com.example.mybudget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button addNewAmount;
    private ListView expensesList;
    ArrayAdapter customArrayAdapter;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataBaseHelper = new DataBaseHelper(MainActivity.this);

        expensesList = findViewById(R.id.expenses_list);
        addNewAmount = findViewById(R.id.add_new_amount);
        addNewAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent amount = new Intent(getApplicationContext(), NewAmount.class);
                startActivity(amount);
            }
        });
        refreshList(dataBaseHelper);

        ArrayAdapter customArrayAdapter = new ArrayAdapter<Amount>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper.showEverything());
        expensesList.setAdapter(customArrayAdapter);

        expensesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Amount clickedExpens = (Amount) parent.getItemAtPosition(position);
                dataBaseHelper.deleteOne(clickedExpens);
                refreshList(dataBaseHelper);
            }
        });
    }

    private void refreshList(DataBaseHelper dataBaseHelper2) {
        customArrayAdapter = new ArrayAdapter<Amount>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper.showEverything());
        expensesList.setAdapter(customArrayAdapter);
    }
}