package com.example.budgettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.AlertDialog;

public class MainActivity extends AppCompatActivity {

    EditText username_e;
    EditText password_e;
    Button sign_in_b;
    TextView create_t;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sign_in_b = findViewById(R.id.sign_in);
        create_t = findViewById(R.id.create);
        db = openOrCreateDatabase("finalProject",MODE_PRIVATE,null);
        db.execSQL("create table if not exists users (\n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "username VARCHAR(50),\n" +
                "password VARCHAR(25)\n" +
                ");");

        sign_in_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_e = findViewById(R.id.username_sign);
                password_e = findViewById(R.id.password_sign);
                String username_s = username_e.getText().toString();
                String password_s = password_e.getText().toString();
                Cursor c = db.rawQuery("select * from users where username="+"'"+username_s+"'"+" AND password="+"'"+password_s+"'"+";",null);
                if (c.getCount()==0) {
                    ShowMessage("ERROR:","User is not found.");
                    username_e.setText("");
                    password_e.setText("");
                    c.close();
                    return;
                }
                c.close();
                db.close();
                Intent i = new Intent(v.getContext(),Summary.class);
                i.putExtra("username",username_s);
                startActivity(i);
            }
        });

        create_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), Register.class);
                startActivity(i);
            }
        });
    }

    public void ShowMessage(String title, String message) {
        AlertDialog.Builder messBr = new AlertDialog.Builder(this);
        messBr.setCancelable(true);
        messBr.setTitle(title);
        messBr.setMessage(message);
        messBr.show();
    }
}
