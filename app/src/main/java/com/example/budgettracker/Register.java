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
public class Register extends AppCompatActivity {

    EditText username_e;
    EditText password_e;
    EditText confirm_e;
    Button back_b;
    Button register_b;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        back_b = findViewById(R.id.back_1);
        register_b = findViewById(R.id.register);
        db = openOrCreateDatabase("finalProject",MODE_PRIVATE,null);

        back_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
                Intent i = new Intent(v.getContext(),MainActivity.class);
                startActivity(i);
            }
        });

        register_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username_e = findViewById(R.id.username_reg);
                password_e = findViewById(R.id.password_reg);
                confirm_e = findViewById(R.id.confirm_reg);
                String username = username_e.getText().toString();
                String password = password_e.getText().toString();
                String confirm = confirm_e.getText().toString();
                if (!valid(username,password)) {
                    ShowMessage("ERROR:","Enter a valid username and password!");
                }
                else if (!password.equals(confirm)) {
                    ShowMessage("ERROR:","Passwords must match!");
                }
                else if (!exists(username,password)) {
                    db.execSQL("insert into users(username,password) values(" + "'" + username + "'" + "," + "'" + password + "'" + ");");
                    clear();
                    Intent i = new Intent(v.getContext(), MainActivity.class);
                    startActivity(i);
                    return;
                }
                else {
                    ShowMessage("ERROR:","User already exists.");
                }

            }
        });
    }
    public boolean valid(String user, String pass) {
        return (user.length() != 0) && (pass.length() != 0);
    }
    public boolean exists(String user,String pass) {
        Cursor c = db.rawQuery("select * from users where username="+"'"+user+"'"+";",null);
        boolean flag = (c.getCount() != 0);
        c.close();
        return flag;
    }
    public void ShowMessage(String title, String message) {
        AlertDialog.Builder messBr = new AlertDialog.Builder(this);
        messBr.setCancelable(true);
        messBr.setTitle(title);
        messBr.setMessage(message);
        messBr.show();
    }
    public void clear() {
        username_e = findViewById(R.id.username_reg);
        password_e = findViewById(R.id.password_reg);
        confirm_e = findViewById(R.id.confirm_reg);
        username_e.setText("");
        password_e.setText("");
        confirm_e.setText("");
    }
}