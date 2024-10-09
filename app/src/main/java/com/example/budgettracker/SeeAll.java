package com.example.budgettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SeeAll extends AppCompatActivity {

    ListView see_l;
    Button back_b;
    SQLiteDatabase db;

    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> amounts = new ArrayList<>();
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all);
        back_b = findViewById(R.id.back_3);
        db = openOrCreateDatabase("finalProject",MODE_PRIVATE,null);
        Intent i = getIntent();
        username = i.getStringExtra("username");

        back_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),Summary.class);
                i.putExtra("username",username);
                startActivity(i);
            }
        });

        Cursor c1 = db.rawQuery("select title, amount from transactions where username="+"'"+username+"'"+" order by id desc"+";",null);
        Cursor c2 = db.rawQuery("select title, amount from income where username="+"'"+username+"'"+" order by id desc"+";",null);

        viewAll(c1,c2);

        c1.close(); c2.close();

        CustomListAdapter adapter = new CustomListAdapter(this,titles,amounts);
        see_l = findViewById(R.id.list_all);
        see_l.setEmptyView(findViewById(android.R.id.empty));
        see_l.setAdapter(adapter);

    }

    public void viewAll(Cursor cursor1, Cursor cursor2) {
        if (cursor1.getCount()==0 && cursor2.getCount()==0) {
            System.out.println("1: "+cursor1.getCount()+" | "+"2: "+cursor2.getCount());
            return;
        }
        else if (cursor1.getCount()==0) {
            System.out.println("1: "+cursor1.getCount()+" | "+"2: "+cursor2.getCount());
            while(cursor2.moveToNext()) {
                titles.add(cursor2.getString(0));
                amounts.add(cursor2.getString(1));
            }
        }
        else if (cursor2.getCount()==0) {
            System.out.println("1: "+cursor1.getCount()+" | "+"2: "+cursor2.getCount());
            while(cursor1.moveToNext()) {
                titles.add(cursor1.getString(0));
                amounts.add(cursor1.getString(1));
            }
        }
        else {
            System.out.println("1: "+cursor1.getCount()+" | "+"2: "+cursor2.getCount());
            boolean flag1 = true;
            boolean flag2 = true;

            while((flag1 || flag2)) {
                if (cursor1.moveToNext()) {
                    titles.add(cursor1.getString(0));
                    amounts.add(cursor1.getString(1));
                }
                else {
                    flag1 = false;
                }
                if (cursor2.moveToNext()) {
                    titles.add(cursor2.getString(0));
                    amounts.add(cursor2.getString(1));
                }
                else {
                    flag2 = false;
                }
            }
        }
    }
}