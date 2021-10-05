package com.example.mybudget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewAmount extends AppCompatActivity {

    private Button addAmount;
    private int id;
    private EditText name;
    private Spinner category;
    private Spinner period;
    private EditText amount;
    private Integer day;
    private Integer month;
    private Integer year;
    private String date;
    private String selectedCategory;
    private String selectedPeriod;
    Double sum=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_amount);

        name = findViewById(R.id.name);
        category = findViewById(R.id.spinner_category);
        period = findViewById(R.id.spinner_period);
        amount =  findViewById(R.id.amount);

        String[] elements = {"opłaty", "zakupy", "przyjemności", "hobby"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, elements);
        category.setAdapter(adapter1);

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCategory = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        String[] elements2 = {"jednorazowy", "miesięczny", "kwartalny", "roczny"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, elements2);
        period.setAdapter(adapter2);

        period.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPeriod = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        Date data= new Date();
        final Calendar c =Calendar.getInstance();
        int dzien = c.get(Calendar.DAY_OF_MONTH);
        day= dzien;
        month=data.getMonth()+1;
        year=data.getYear()+1900;
        date= String.valueOf(day)+"."+String.valueOf(month)+"."+String.valueOf(year);

        addAmount = findViewById(R.id.add_amount);
        addAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Integer getAmount = Integer.valueOf(amount.getText().toString());

                if(getAmount > 0) {

                    Amount amountAmount;

                    try {
                        amountAmount = new Amount(id, name.getText().toString(), date, selectedCategory, selectedPeriod, amount.getText().toString());
                        Toast.makeText(NewAmount.this, amountAmount.toString(), Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        amountAmount = new Amount(id, name.getText().toString(), "error", selectedCategory, "error", amount.getText().toString());
                    }

                    DataBaseHelper dataBaseHelper = new DataBaseHelper(NewAmount.this);

                    boolean success = dataBaseHelper.addOne(amountAmount);

                    Toast.makeText(NewAmount.this, "Success= "+ success, Toast.LENGTH_LONG).show();

                    Intent wroc = new Intent(NewAmount.this, MainActivity.class);
                    startActivity(wroc);
                }else {
                    Toast.makeText(NewAmount.this, "Wprowadź prawidłową kwotę", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}