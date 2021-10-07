package com.example.mybudget;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final String USER_TABLE = "USER_TABLE";
    public static final String USER_EXPENSES = "USER_EXPENSES";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_MONTHLY_INCOME = "monthly_income";
    public static final String COLUMN_PLANNED_SAVINGS = "planned_savings";
    public static final String COLUMN_EXPENS_NAME = "expens_name";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_PERIOD = "period";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_ID = "ID";
    private String month;

    private static DataBaseHelper databaseHelper;

    public DataBaseHelper(@Nullable Context context) {
        super(context, "myBudget_DB1", null, 1);
    }

    public static DataBaseHelper getInstance(Context context) {
        if (databaseHelper == null) {
            databaseHelper = new DataBaseHelper(context);
        }
        return databaseHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

//        final Calendar c = Calendar.getInstance();
//        int miesiac = c.get(Calendar.MONTH);
//        month = miesiac + 1;

        String createTableStatement1 = "CREATE TABLE " + USER_TABLE + " (" + COLUMN_USER_NAME + " TEXT, "
                + COLUMN_MONTHLY_INCOME + " INT, " + COLUMN_PLANNED_SAVINGS + " INT)";
        String createTableStatement2 = "CREATE TABLE " + USER_EXPENSES + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_EXPENS_NAME + " TEXT, "
                + COLUMN_DATE + " TEXT, " + COLUMN_CATEGORY + " TEXT, " + COLUMN_PERIOD + " TEXT, " + COLUMN_AMOUNT + " INT)";

        sqLiteDatabase.execSQL(createTableStatement1);
        sqLiteDatabase.execSQL(createTableStatement2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(Amount amountAmount){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_EXPENS_NAME, amountAmount.getName());
        cv.put(COLUMN_DATE, amountAmount.getDate());
        cv.put(COLUMN_CATEGORY, amountAmount.getCategory());
        cv.put(COLUMN_PERIOD, amountAmount.getPeriod());
        cv.put(COLUMN_AMOUNT, amountAmount.getAmount());

        long insert = sqLiteDatabase.insert(USER_EXPENSES, null, cv);
        if(insert == -1){
            return false;
        }else {
            return true;
        }
    }
    public boolean deleteOne(Amount amountAmount){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String queryString = "DELETE FROM " + USER_EXPENSES + " WHERE " + COLUMN_ID + " = " + amountAmount.getId();
        Cursor cursor = sqLiteDatabase.rawQuery(queryString, null);

        if(cursor.moveToFirst()) {
            return true;
        }else {
            return false;
        }
    }

    public List<Amount> showEverything(){
        List<Amount> returnList = new ArrayList<>();

        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        month = String.valueOf(dateFormat.format(date));

        String queryString = "SELECT * FROM " + USER_EXPENSES;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ USER_EXPENSES +" WHERE strftime('%m', "+COLUMN_DATE+")"+ "LIKE " +  month,null);

//        Cursor cursor = sqLiteDatabase.query(USER_EXPENSES, null, null,
//                null, null, null, COLUMN_DATE + " DESC", null);

        if(cursor.moveToFirst()){
            do {
                int expensId = cursor.getInt(0);
                String expensName = cursor.getString(1);
                String expensDate = cursor.getString(2);
                String expensCategory = cursor.getString(3);
                String expensPeriod = cursor.getString(4);
                String expensAmount = cursor.getString(5);

                Amount newAmount = new Amount(expensId, expensName, expensDate, expensCategory, expensPeriod, expensAmount);
                returnList.add(newAmount);

            } while (cursor.moveToNext());
        }

        return returnList;
    }
        public int sumOfExpenses(){
        int result = 0;
            DateFormat dateFormat = new SimpleDateFormat("MM");
            Date date = new Date();
            month = String.valueOf(dateFormat.format(date));

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        /// to nizej dziala
        //Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM("+ COLUMN_AMOUNT +") FROM " + USER_EXPENSES , null);
            //Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ USER_EXPENSES +" WHERE strftime('%m', "+COLUMN_DATE+")"+ "LIKE " +  month,null);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM("+ COLUMN_AMOUNT +") FROM "+
                USER_EXPENSES +" WHERE strftime('%m', "+COLUMN_DATE+")"+ "LIKE " +  month,null);
        if(cursor.moveToFirst()) result = cursor.getInt(0);
            cursor.close();
            sqLiteDatabase.close();
            return result;
        }

        public boolean addUser(User userUser){

            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put(COLUMN_USER_NAME, userUser.getUserName());
            cv.put(COLUMN_MONTHLY_INCOME, userUser.getMonthlyIncome());
            cv.put(COLUMN_PLANNED_SAVINGS, userUser.getPlannedSavings());

            long insert = sqLiteDatabase.insert(USER_TABLE, null, cv);
            if(insert == -1){
                return false;
            }else {
                return true;
            }
        }

        public int getMonthlyIncome(){

        int monthlyIncome = 0;
        String queryString = "SELECT " + COLUMN_MONTHLY_INCOME + "FROM " + USER_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(USER_TABLE, new String[] {COLUMN_MONTHLY_INCOME}, null, null, null, null, null);
            if(cursor.moveToFirst()) monthlyIncome = cursor.getInt(0);
            cursor.close();
            sqLiteDatabase.close();

        return monthlyIncome;
        }

        public int getPlannedSavings(){

        int plannedSavings = 0;
        String queryString = "SELECT " + COLUMN_PLANNED_SAVINGS + "FROM " + USER_TABLE;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(USER_TABLE, new String[] {COLUMN_PLANNED_SAVINGS}, null, null, null, null, null);
            if(cursor.moveToFirst()) plannedSavings = cursor.getInt(0);
            cursor.close();
            sqLiteDatabase.close();

        return plannedSavings;
    }

        public int getPercentShopping(){

        int percentShopping = 0;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM("+ COLUMN_AMOUNT +") FROM "+ USER_EXPENSES +" WHERE "+COLUMN_CATEGORY+" LIKE 'zakupy' ", null);
        if(cursor.moveToFirst()) percentShopping = cursor.getInt(0);
        cursor.close();
        sqLiteDatabase.close();
        return percentShopping;
        }

    public int getPercentCharges(){

        int percentCharges = 0;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM("+ COLUMN_AMOUNT +") FROM "+ USER_EXPENSES +" WHERE "+COLUMN_CATEGORY+" LIKE 'opłaty' ", null);
        if(cursor.moveToFirst()) percentCharges = cursor.getInt(0);
        cursor.close();
        sqLiteDatabase.close();
        return percentCharges;
    }

    public int getPercentPleasures(){

        int percentPleasures = 0;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM("+ COLUMN_AMOUNT +") FROM "+ USER_EXPENSES +" WHERE "+COLUMN_CATEGORY+" LIKE 'przyjemności' ", null);
        if(cursor.moveToFirst()) percentPleasures = cursor.getInt(0);
        cursor.close();
        sqLiteDatabase.close();
        return percentPleasures;
    }

    public int getPercentHobby(){

        int percentHobby = 0;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT SUM("+ COLUMN_AMOUNT +") FROM "+ USER_EXPENSES +" WHERE "+COLUMN_CATEGORY+" LIKE 'hobby' ", null);
        if(cursor.moveToFirst()) percentHobby = cursor.getInt(0);
        cursor.close();
        sqLiteDatabase.close();
        return percentHobby;
    }
}