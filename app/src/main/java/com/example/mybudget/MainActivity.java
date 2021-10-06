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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button addNewAmount;
    private ListView expensesList;
    private TextView textView_month;
    private TextView textView_analysis;
    private Integer month;
    private Integer expensesSum, differInt, differInt2;
    private Double num1, num2, num3, difference, difference2, percent, over;
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

        Date data = new Date();
        month = data.getMonth()+1;
        Log.d("miesiac", "onCreate: " + month);
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
        num1 = Double.valueOf(sqLiteDatabase.sumOfExpenses());
        num2 = Double.valueOf(sqLiteDatabase.getMonthlyIncome());
        num3 = Double.valueOf(sqLiteDatabase.getPlannedSavings());
        textView_analysis.setText("" + sqLiteDatabase.sumOfExpenses() + " " + sqLiteDatabase.getMonthlyIncome() + " " + sqLiteDatabase.getPlannedSavings());

        setTextInAnalyseView(sqLiteDatabase);


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
                        //textView_analysis.setText("" + sqLiteDatabase.sumOfExpenses());
                        setTextInAnalyseView(sqLiteDatabase);
                    }
                });
                builder.setNegativeButton("Nie", null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void setTextInAnalyseView(DataBaseHelper sqLiteDatabase) {
        if(num1 < 1) {
            textView_analysis.setText("Brak wydatków w tym miesiącu");

        } else if((num2-num3) > num1){

            difference = ((num1 / (num2 - num3)) * 100);
            percent = 100 - difference;
            differInt = (int) Math.round(percent);
            textView_analysis.setText("W tym miesiącu zostało niewykorzystane " + differInt + "% budżetu");
            textView_analysis.invalidate();
            //Log.d("czemu", "onCreate: "+ num1 + " " + num2 + " " + num3 + " " + difference + " " + differInt);

        }else if (num1 == (num2-num3)) {

            textView_analysis.setText("Wykorzystałeś 100% swojego miesięcznego budżetu");

        }else if (num1 > (num2-num3)) {

            over = num1 - (num2 - num3);
            difference2 = (over / (num2 - num3) * 100);
            differInt2 = (int) Math.round(difference2);
            textView_analysis.setText("Twój miesięczny budżet został przekroczony o " + differInt2 +"%");
            textView_analysis.invalidate();
        }else{}
    }

    private void refreshList(DataBaseHelper dataBaseHelper2) {
        customArrayAdapter = new ArrayAdapter<Amount>(MainActivity.this, android.R.layout.simple_list_item_1, dataBaseHelper.showEverything());
        expensesList.setAdapter(customArrayAdapter);
    }
}