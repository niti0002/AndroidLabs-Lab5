package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lab3);

        EditText editText = findViewById(R.id.userEmail);

        Button loginButton = findViewById(R.id.loginButton);


        SharedPreferences prefs = getSharedPreferences("FileName", MODE_PRIVATE);
        String previous = prefs.getString("ReserveName", "Default Value");

        editText.setText(previous);


        loginButton.setOnClickListener(clk -> {
            Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);


            goToProfile.putExtra("ReserveName", editText.getText().toString());

            startActivity(goToProfile);



        });
    }


    @Override
    protected void onPause() {
        super.onPause();

        EditText editText = findViewById(R.id.userEmail);

        SharedPreferences prefs = getSharedPreferences("FileName", MODE_PRIVATE);
        String previous = prefs.getString("ReserveName", "Default Value");

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ReserveName", editText.getText().toString());

        editor.commit();


   }
}
