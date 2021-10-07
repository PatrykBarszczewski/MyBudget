package com.example.mybudget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OneTimeDialog extends AppCompatActivity {

    private EditText userName;
    private EditText userEarnings;
    private EditText userPlannedSavings;
    private Button saveUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time_dialog);

        userName = findViewById(R.id.edit_name);
        userEarnings = findViewById(R.id.edit_earnings);
        userPlannedSavings = findViewById(R.id.edit_savings);
        saveUserData = findViewById(R.id.save_button);

        saveUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Integer getUserEarnings = Integer.valueOf(userEarnings.getText().toString());
                Integer getUserPlannedSavings = Integer.valueOf(userPlannedSavings.getText().toString());

                if(getUserEarnings > 0 && getUserPlannedSavings > 0 && getUserEarnings > getUserPlannedSavings) {

                    User userUser;

                    try {

                        userUser = new User(userName.getText().toString(), getUserEarnings, getUserPlannedSavings);

                    }catch (Exception e) {

                        userUser = new User("error", getUserEarnings, getUserPlannedSavings);
                    }

                    DataBaseHelper dataBaseHelper = new DataBaseHelper(OneTimeDialog.this);

                    boolean success = dataBaseHelper.addUser(userUser);

                    Intent intent = new Intent(OneTimeDialog.this, MainActivity.class);
                    startActivity(intent);

                }else Toast.makeText(OneTimeDialog.this, "Wprowadź poprawne dane!", Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPreferences = getSharedPreferences("preferences", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("firstRun", false);
                editor.apply();
                finish();
            }
        });
    }
}