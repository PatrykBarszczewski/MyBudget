package com.example.mybudget;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button addNewAmount;
    private ListView expensesList;
    private TextView textView_month;
    private TextView textView_analysis;
    private Integer month;
    private Integer expensesSum;
    ArrayAdapter customArrayAdapter;
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("preferences", 0);
        boolean firstRun = sharedPreferences.getBoolean("firstRun", true);
        if(firstRun){

            startActivity(new Intent(MainActivity.this, OneTimeDialog.class));
        }

        dataBaseHelper = new DataBaseHelper(MainActivity.this);

        textView_month = findViewById(R.id.month);

//        textView_month.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, OneTimeDialog.class);
//                startActivity(intent);
//            }
//        });

        Date data = new Date();
        month = data.getMonth()+1;
        if (month == 1) textView_month.setText("STYCZEŃ");
        else if (month == 2) textView_month.setText("LUTY");
        else if (month == 3) textView_month.setText("MARZEC");
        else if (month == 4) textView_month.setText("KWIECIEŃ");
        else if (month == 5) textView_month.setText("MAJ");
        else if (month == 6) textView_month.setText("CZERWIEC");
        else if (month == 7) textView_month.setText("LIPIEC");
        else if (month == 8) textView_month.setText("SIERPIEŃ");
        else if (month == 9) textView_month.setText("WRZESIEŃ");
        else if (month == 10) textView_month.setText("PAŹDZIERNIK");
        else if (month == 11) textView_month.setText("LISTOPAD");
        else if (month == 12) textView_month.setText("GRUDZIEŃ");

        textView_analysis = findViewById(R.id.analysis_view);
        DataBaseHelper sqLiteDatabase = DataBaseHelper.getInstance(this);
        textView_analysis.setText("" + sqLiteDatabase.sumOfExpenses() + " " + sqLiteDatabase.getMonthlyIncome() + " " + sqLiteDatabase.getPlannedSavings());


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

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Usunięcie wydatku");
                builder.setMessage("Czy chcesz usunąć ten wydatek?");
                builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dataBaseHelper.deleteOne(clickedExpens);
                        refreshList(dataBaseHelper);
                        textView_analysis.setText("" + sqLiteDatabase.sumOfExpenses());
                    }
                });
                builder.setNegativeButton("Nie", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void refreshList(DataBaseHelper dataBaseHelper2) {
        customArrayAdapter = new ArrayAdapter<Amount>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper.showEverything());
        expensesList.setAdapter(customArrayAdapter);
    }
}