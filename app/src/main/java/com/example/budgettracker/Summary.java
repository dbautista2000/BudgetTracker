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

public class Summary extends AppCompatActivity {

    TextView expenses_t;
    TextView income_t;
    TextView net_t;
    ListView recent_l;
    Button add_b;
    Button see_b;
    Button sign_out_b;

    String username;
    double expenses;
    double income;
    double net;

    ArrayList<String> titles = new ArrayList<>();
    ArrayList<String> amounts = new ArrayList<>();
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        income_t = findViewById(R.id.income);
        expenses_t = findViewById(R.id.expenses);
        net_t = findViewById(R.id.net);
        sign_out_b = findViewById(R.id.sign_out);
        add_b = findViewById(R.id.add_button);
        see_b = findViewById(R.id.see_button);

        Intent i = getIntent();
        username = i.getStringExtra("username");

        add_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                username = i.getStringExtra("username");
                Intent i_add = new Intent(v.getContext(), Add.class);
                i_add.putExtra("username",username);
                startActivity(i_add);
            }
        });

        see_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = getIntent();
                username = i.getStringExtra("username");
                Intent i_see = new Intent(v.getContext(), SeeAll.class);
                i_see.putExtra("username",username);
                startActivity(i_see);
            }
        });

        sign_out_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = null;
                Intent i_out = new Intent(v.getContext(),MainActivity.class);
                startActivity(i_out);
            }
        });

        db = openOrCreateDatabase("finalProject",MODE_PRIVATE,null);
        db.execSQL("create table if not exists transactions (\n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "title VARCHAR(50),\n" +
                "amount DECIMAL(8,2),\n" +
                "category VARCHAR(20), \n" +
                "username VARCHAR(50)" +
                ");");
        db.execSQL("create table if not exists income (\n" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "title VARCHAR(50),\n" +
                "amount DECIMAL(8,2),\n" +
                "category VARCHAR(20), \n" +
                "username VARCHAR(50)" +
                ");");

        Cursor c1 = db.rawQuery("select title, amount from transactions where username="+"'"+username+"'"+" order by id desc"+";",null);
        Cursor c2 = db.rawQuery("select title, amount from income where username="+"'"+username+"'"+" order by id desc"+";",null);

        if (c1.getCount()==0) expenses = 0;
        else expenses = sum("expenses");
        if (c2.getCount()==0) income = 0;
        else income = sum("income");

        viewRecent(c1,c2);

        c1.close(); c2.close();

        CustomListAdapter adapter = new CustomListAdapter(this,titles,amounts);
        recent_l = findViewById(R.id.list);
        recent_l.setEmptyView(findViewById(android.R.id.empty));
        recent_l.setAdapter(adapter);

        net = income - expenses;
        String income_s = getString(R.string.income)+income;
        String expenses_s = getString(R.string.expenses)+expenses;
        String net_s = getString(R.string.net)+net;

        income_t.setText(income_s);
        expenses_t.setText(expenses_s);
        net_t.setText(net_s);
    }

    public double sum(String type) {
        double total = 0;
        if (type.equals("expenses")) {
            Cursor c = db.rawQuery("select username, sum(amount) " +
                    "from transactions " +
                    "where username=" + "'" + username + "'" +
                    " group by username" + ";", null);
            c.moveToNext();
            total = c.getDouble(1);
            c.close();
        }
        else if (type.equals("income")) {
            Cursor c = db.rawQuery("select username, sum(amount) " +
                    "from income " +
                    "where username=" + "'" + username + "'" +
                    " group by username" + ";",null);
            c.moveToNext();
            total = c.getDouble(1);
            c.close();
        }
        return total;
    }

    public void viewRecent(Cursor cursor1, Cursor cursor2) {
        if (cursor1.getCount()==0 && cursor2.getCount()==0) {
            System.out.println("1: "+cursor1.getCount()+" | "+"2: "+cursor2.getCount());
            return;
        }
        else if (cursor1.getCount()==0) {
            System.out.println("1: "+cursor1.getCount()+" | "+"2: "+cursor2.getCount());
            int i = 0;
            while(cursor2.moveToNext() && i < 4) {
                titles.add(cursor2.getString(0));
                amounts.add(cursor2.getString(1));
                i++;
            }
        }
        else if (cursor2.getCount()==0) {
            System.out.println("1: "+cursor1.getCount()+" | "+"2: "+cursor2.getCount());
            int i = 0;
            while(cursor1.moveToNext() && i < 4) {
                titles.add(cursor1.getString(0));
                amounts.add(cursor1.getString(1));
                i++;
            }
        }
        else {
            System.out.println("1: "+cursor1.getCount()+" | "+"2: "+cursor2.getCount());
            boolean flag1 = true;
            boolean flag2 = true;
            int i = 0;

            while((flag1 || flag2) && i < 4) {
                if (cursor1.moveToNext()) {
                    titles.add(cursor1.getString(0));
                    amounts.add(cursor1.getString(1));
                    i++;
                }
                else {
                    flag1 = false;
                }
                if (cursor2.moveToNext()) {
                    titles.add(cursor2.getString(0));
                    amounts.add(cursor2.getString(1));
                    i++;
                }
                else {
                    flag2 = false;
                }
            }
        }
    }

    public void ShowMessage(String title, String message) {
        AlertDialog.Builder messBr = new AlertDialog.Builder(this);
        messBr.setCancelable(true);
        messBr.setTitle(title);
        messBr.setMessage(message);
        messBr.show();
    }
}